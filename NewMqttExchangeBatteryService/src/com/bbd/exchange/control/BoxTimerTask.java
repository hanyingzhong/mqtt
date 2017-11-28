package com.bbd.exchange.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.mobile.MobileMqttClientSimnulation;
import com.bbd.exchange.mqtt.CommonClientMqttMsgCallback;
import com.bbd.exchange.mqtt.CommonExchangeMqttClient;
import com.bbd.exchange.simuclient.ExchangeMqttClient;
import com.bbd.exchange.util.MqttCfgUtil;
import com.bbd.exchange.util.PropertyUtil;

public class BoxTimerTask implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
/*	static MobileMqttClientSimnulation mqttClient = MobileMqttClientSimnulation.getInstance();

	static {
		ExchangeMqttClient exchangeMqttClient = new ExchangeMqttClient(MqttCfgUtil.getServerUri(), "parry", "parry123",
				"SIMU-BOX-TIMER");
		mqttClient.setMqttClient(exchangeMqttClient);
	}*/

	static CommonExchangeMqttClient newMqttClient;

	static {
		newMqttClient = new CommonExchangeMqttClient(MqttCfgUtil.getServerUri(), "parry", "parry123",
				"TIMER-T4555662ER", null);

		try {
			newMqttClient.initClient(new CommonClientMqttMsgCallback(newMqttClient));
			newMqttClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	BoxTimerMessage message;

	public BoxTimerTask(BoxTimerMessage message) {
		this.message = message;
	}

	/*
	 * public static String encodeTopic(BoxTimerMessage message) { return
	 * "timer/box/" + message.getBoxId(); }
	 * 
	 * public static String encodeJson(BoxTimerMessage message) { JSONObject
	 * jsonObject = new JSONObject(); jsonObject.put("expire", message); return
	 * jsonObject.toString(); }
	 */

	public static void triggerTimer(BoxTimerMessage timerMsg) {
		/*
		 * String topic = encodeTopic(timerMsg); String message = encodeJson(timerMsg);
		 */
		String topic = BoxTimerMessage.destinationTopic(timerMsg);
		String message = BoxTimerMessage.encode2Json(timerMsg);

		//logger.info("send timer, " + topic + ":" + message);
		// mqttClient.getMqttClient().sendPublish(topic, message);
		newMqttClient.sendPublish(topic, message);
	}

	@Override
	public void run() {
		TimerMgr.decrease();
		triggerTimer(message);
	}
}
