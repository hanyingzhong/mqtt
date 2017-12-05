package com.bbd.exchange.platform;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.message.InternalExchangeRequestMessage;
import com.bbd.exchange.message.MobileExchangeRequestMessage;
import com.bbd.exchange.table.CabinetBoxManager;
import com.bbd.exchange.table.CabinetManager;
import com.bbd.exchange.table.CabinetObject;
import com.bbd.exchange.util.MqttCfgUtil;

import net.sf.json.JSONObject;

class MobileClientMqttMsgCallback extends CommonClientMqttMsgCallback {
	public MobileClientMqttMsgCallback(CommonExchangeMqttClient client) {
		super(client);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if (CommandType.FROM_EXCHANGER == getMsgSource(topic)) {
			ServiceMessageQueue.getInstance().add(new CommonExchangeRequest(topic, message.toString()));
		}
	}
}

public class MobileExchangeRequestHandler {
	private static final Logger logger = LoggerFactory.getLogger(MobileExchangeRequestHandler.class);

	private static final MobileExchangeRequestHandler INSTANCE = new MobileExchangeRequestHandler();

	public static final MobileExchangeRequestHandler getInstance() {
		return MobileExchangeRequestHandler.INSTANCE;
	}

	static CommonExchangeMqttClient mqttClient;
	static {
		mqttClient = new CommonExchangeMqttClient(MqttCfgUtil.getServerUri(), MqttCfgUtil.getUsername(),
				MqttCfgUtil.getPassword(), "HDLER-CLIENT003R", null);

		try {
			mqttClient.initClient(new MobileClientMqttMsgCallback(mqttClient));
			mqttClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sendPublish(String topic, String message) {
		if (mqttClient.isConnected() == false) {
			mqttClient.connect();
		}

		logger.info("publish:{},{}", topic, message);
		mqttClient.sendPublish(topic, message);
	}

	/***********************************************************************
	 * 
	 * @param message
	 * @return
	 ***********************************************************************/
	static CabinetBoxManager getEmptyBox(MobileExchangeRequestMessage message) {
		if (message.getDeviceType().equals("cabinet")) {
			CabinetObject cabinet = CabinetManager.getInstance().getCabinetObj(message.getDeviceID());

			return cabinet.getEmptyBox();
		}

		return null;
	}

	static CabinetBoxManager getFullBox(MobileExchangeRequestMessage message) {
		if (message.getDeviceType().equals("cabinet")) {
			CabinetObject cabinet = CabinetManager.getInstance().getCabinetObj(message.getDeviceID());

			return cabinet.getFullBox(message.getBatteryType());
		}

		return null;
	}

	private static void sendExchangeRequestMessage(MobileExchangeRequestMessage message, CabinetBoxManager emptyBoxMgr,
			CabinetBoxManager fullBoxMgr) {
		InternalExchangeRequestMessage exchange = new InternalExchangeRequestMessage();

		exchange.setBatteryType(message.getBatteryType());
		exchange.setEmptyBoxID(emptyBoxMgr.getBoxID());
		exchange.setFullEnergyBoxID(fullBoxMgr.getBoxID());
		exchange.setRequestID(message.getRequestID());

		sendPublish(exchange.destinationTopic(), exchange.encode2Json());
	}

	static void messageHandler(MobileExchangeRequestMessage message) {
		CabinetBoxManager emptyBoxMgr = getEmptyBox(message);
		CabinetBoxManager fullBoxMgr = getFullBox(message);
		if (emptyBoxMgr == null) {
			logger.info("no empty box useful.");
			return;
		}
		if (fullBoxMgr == null) {
			logger.info("no full box useful.");
			return;
		}

		emptyBoxMgr.setWorkState(CabinetBoxManager.WK_INEXCHAGING);
		fullBoxMgr.setWorkState(CabinetBoxManager.WK_INEXCHAGING);

		logger.info("empty box : {} choosed.", emptyBoxMgr.getBoxID());
		logger.info("full  box : {} choosed.", fullBoxMgr.getBoxID());

		sendExchangeRequestMessage(message, emptyBoxMgr, fullBoxMgr);
	}

	public static InternalExchangeRequestMessage encode2Obj(String message) {
		JSONObject jsonObject = JSONObject.fromObject(message);
		InternalExchangeRequestMessage exchange = (InternalExchangeRequestMessage) JSONObject
				.toBean(jsonObject.getJSONObject("exchange"), InternalExchangeRequestMessage.class);
		return exchange;
	}
}
