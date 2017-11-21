package com.bbd.exchange.service;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.control.BoxTimerMessage;
import com.bbd.exchange.control.CabinetBoxObject;
import com.bbd.exchange.control.CabinetMgrContainer;
import com.bbd.exchange.mqtt.CabinetBoxContainer;
import com.bbd.exchange.mqtt.CommonClientMqttMsgCallback;
import com.bbd.exchange.mqtt.CommonExchangeMqttClient;
import com.bbd.exchange.mqtt.DownstreamCabinetMessage;
import com.bbd.exchange.mqtt.InteractionCommand;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;
import com.bbd.exchange.util.MqttCfgUtil;

/*
 * manage exchange request 
 * store the request information...
 * generate request record : succeed or failed...exchange time....etc..
 * 
 * */

public class ExchangeServiceRequestMgr {
	private static final Logger logger = LoggerFactory.getLogger(UpstreamCabinetMessage.class);

	private static final ExchangeServiceRequestMgr INSTANCE = new ExchangeServiceRequestMgr();

	public static final ExchangeServiceRequestMgr getInstance() {
		return ExchangeServiceRequestMgr.INSTANCE;
	}

	static ConcurrentHashMap<String, RemoteExchangeRequest> requestMgr = new ConcurrentHashMap<String, RemoteExchangeRequest>();
	static CommonExchangeMqttClient mqttClient;

	static {
		if (mqttClient == null) {
			mqttClient = new CommonExchangeMqttClient(MqttCfgUtil.getServerUri(), "parry", "parry123",
					"ExchangeServiceRequestMgr", null);

			try {
				mqttClient.initClient(new CommonClientMqttMsgCallback(mqttClient));
				mqttClient.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void addRequestInstance(CabinetBoxObject empty, CabinetBoxObject full, ExchangeRequestMessage request) {
		requestMgr.put(empty.getBoxID(), new RemoteExchangeRequest(full, request));
		requestMgr.put(full.getBoxID(), new RemoteExchangeRequest(empty, request));
	}

	/* only open the box...for.... */
	public void addRequestInstance(CabinetBoxObject box, ExchangeRequestMessage request) {
		requestMgr.put(box.getBoxID(), new RemoteExchangeRequest(null, request));
	}

	public CabinetBoxObject getAssociatedBox(CabinetBoxObject closed) {
		if (requestMgr.get(closed.getBoxID()) != null) {
			return requestMgr.get(closed.getBoxID()).getAssociatedBox();
		}

		return null;
	}

	public void removeRequestInstance(CabinetBoxObject closed) {
		requestMgr.remove(closed.getBoxID());
	}

	RemoteExchangeRequest getExchangeRequestMessage(CabinetBoxObject boxObj) {
		return requestMgr.get(boxObj.getBoxID());
	}

	void wait4EmptyBoxOpenedFailed(CabinetBoxObject empty) {
		CabinetBoxObject full = getAssociatedBox(empty);
		requestMgr.remove(empty.getBoxID());
		if (full != null) {
			requestMgr.remove(full.getBoxID());
		}
	}

	void wait4EmptyBoxClosedFailed(CabinetBoxObject empty) {
		CabinetBoxObject full = getAssociatedBox(empty);

		if (full != null) {
			full.killTimer(CabinetBoxObject.WAIT4EDOOROPENED);
		}
		requestMgr.remove(empty.getBoxID());

		if (full != null) {
			requestMgr.remove(full.getBoxID());
		}
	}

	void wait4FullBoxOpenedFailed(CabinetBoxObject full) {
		full.killTimer(CabinetBoxObject.WAIT4FDOOROPENED);
		requestMgr.remove(full.getBoxID());
	}

	@SuppressWarnings("static-access")
	void sendEmptyExchangeFailedNotify(CabinetBoxObject empty, String result) {
		RemoteExchangeRequest request = getExchangeRequestMessage(empty);
		ExchangeServiceResponseMessage response = new ExchangeServiceResponseMessage(
				request.getMessage().getRequestID(), request.getMessage().getCabinetID(),
				request.getMessage().getNotifyTopic(), result);

		logger.info(request.getMessage().getNotifyTopic() + "===>" + response.encode2Json(response));
		mqttClient.sendPublish(request.getMessage().getNotifyTopic(), response.encode2Json(response));
	}

	void sendExchangeSucceededNotify(CabinetBoxObject full, String result) {
		RemoteExchangeRequest request = getExchangeRequestMessage(full);
		ExchangeServiceResponseMessage response = new ExchangeServiceResponseMessage(
				request.getMessage().getRequestID(), request.getMessage().getCabinetID(),
				request.getMessage().getNotifyTopic(), result);

		logger.info(request.getMessage().getNotifyTopic() + "===>" + response.encode2Json(response));
		mqttClient.sendPublish(request.getMessage().getNotifyTopic(), response.encode2Json(response));
	}

	public void exchangeRequestMessageHandling(ExchangeRequestMessage message) {
		CabinetBoxObject empty = CabinetMgrContainer.getInstance().getCabinetBox(message.getCabinetID(),
				Integer.parseInt(message.getEmptyBoxID()) - 1);
		CabinetBoxObject full = CabinetMgrContainer.getInstance().getCabinetBox(message.getCabinetID(),
				Integer.parseInt(message.getFullEnergyBoxID()) - 1);

		if (empty == null) {
			logger.info(message.getEmptyBoxID() + " is not configured.");
			return;
		}

		if (full == null) {
			logger.info(message.getFullEnergyBoxID() + " is not configured.");
			return;
		}
		logger.info("receive a valid exchange request. empty box is {}, full box is {}", empty.getBoxID(),
				full.getBoxID());
		addRequestInstance(empty, full, message);

		/* send publish to device/cabinetID */
		DownstreamCabinetMessage open = new DownstreamCabinetMessage(message.getCabinetID(),
				InteractionCommand.DOWN_MODIFY, InteractionCommand.DOWN_SUB_OPEN, empty.getID() + 1);
		if (false == open.checkAndSetDeviceID()) {
			/* return false message to .... */
			logger.warn("{}'s not associated.", message.getCabinetID());
			return;
		}
		open.publish(open);

		empty.newState(CabinetBoxObject.EMPTY_W4OPENED);
		full.newState(CabinetBoxObject.FULL_W4OPENED);

		/* set timer */
		/*
		 * ScheduledFuture<?> timer = TimerMgr.setTimer(new
		 * BoxTimerMessage(empty.getCabinetID(), Integer.toString(empty.getID() + 1),
		 * CabinetBoxObject.WAIT4EDOOROPENED), 15);
		 * empty.getTimerMap().put(CabinetBoxObject.WAIT4EDOOROPENED, timer);
		 */
		empty.setTimer(CabinetBoxObject.WAIT4EDOOROPENED, 30);
	}

	void boxTimerExpire(CabinetBoxObject boxObj, String timer) {
		if (boxObj.getBoxState().equals(CabinetBoxObject.EMPTY_W4OPENED)
				&& timer.equals(CabinetBoxObject.WAIT4EDOOROPENED)) {
			RemoteExchangeRequest request = getExchangeRequestMessage(boxObj);

			logger.info("Box {}, timer {} expire.", boxObj.getBoxID(), timer);
			if (request == null) {
				return;
			}
			/* send exchange failed notify */
			logger.info("request {} failed", request.getMessage().getRequestID());
			sendEmptyExchangeFailedNotify(boxObj, "wait for empty box open-ack failed");
			wait4EmptyBoxOpenedFailed(boxObj);
			return;
		}

		if (boxObj.getBoxState().equals(CabinetBoxObject.FULL_W4OPENED)
				&& timer.equals(CabinetBoxObject.WAIT4FDOOROPENED)) {
			RemoteExchangeRequest request = getExchangeRequestMessage(boxObj);

			/* send exchange failed notify */
			logger.info("request {} failed", request.getMessage().getRequestID());
			sendEmptyExchangeFailedNotify(boxObj, "wait for full box open-ack failed");
			wait4EmptyBoxOpenedFailed(boxObj);
			return;
		}

		if (boxObj.getBoxState().equals(CabinetBoxObject.EMPTY_W4CLOSED)
				&& timer.equals(CabinetBoxObject.WAIT4EDOORCLOSED)) {
			RemoteExchangeRequest request = getExchangeRequestMessage(boxObj);

			/* send exchange failed notify */
			logger.info("request {} failed", request.getMessage().getRequestID());
			wait4EmptyBoxClosedFailed(boxObj);
			return;
		}

		logger.info("Box {}, timer {} expire." , boxObj.getBoxID(), timer);
		logger.info("timer {}@{}, abnormal", timer, boxObj.getBoxState());
	}

	public void timerExpireHandling(BoxTimerMessage timer) {
		logger.info("Timer expire++ : " + timer.toString());
		CabinetBoxObject boxObj = CabinetMgrContainer.getInstance().getCabinetBox(timer.getCabinetID(),
				Integer.parseInt(timer.getBoxId()) - 1);
		if (boxObj != null) {
			// boxObj.timerExipre(timer.getTimerID());
			boxTimerExpire(boxObj, timer.getTimerID());
		}
	}

	public void notifyDoorOpenedSucceed(CabinetBoxObject boxObj, CabinetBoxContainer ele) {
		logger.info("{}@{} received door open succeeded message", boxObj.getBoxID(), boxObj.getBoxState());

		if (boxObj.getBoxState().equals(CabinetBoxObject.EMPTY_W4OPENED)) {
			boxObj.newState(CabinetBoxObject.EMPTY_W4CLOSED);
			boxObj.killTimer(CabinetBoxObject.WAIT4EDOOROPENED);
			boxObj.setTimer(CabinetBoxObject.WAIT4EDOORCLOSED, 30);
		} else if (boxObj.getBoxState().equals(CabinetBoxObject.FULL_W4OPENED)) {
			boxObj.newState(CabinetBoxObject.FULL_W4CLOSED);
			boxObj.killTimer(CabinetBoxObject.WAIT4FDOOROPENED);
			boxObj.setTimer(CabinetBoxObject.WAIT4FDOORCLOSED, 30);
		} else {
			logger.error("abnormal state:" + boxObj.toString());
		}
		boxObj.storeSingleAttr2Redis(CabinetBoxObject.STATE);
	}

	public void notifyDoorOpenedFailed(CabinetBoxObject boxObj, CabinetBoxContainer ele) {
		logger.info("{}@{} received door open failed message", boxObj.getBoxID(), boxObj.getBoxState());

		if (boxObj.getBoxState().equals(CabinetBoxObject.EMPTY_W4OPENED)) {
			/* generate warning ....the box can't work.. */
			wait4EmptyBoxOpenedFailed(boxObj);
			boxObj.newState(CabinetBoxObject.OPENFAULT);
		} else if (boxObj.getBoxState().equals(CabinetBoxObject.FULL_W4OPENED)) {
			/* generate warning ....the box can't work.. */
			wait4FullBoxOpenedFailed(boxObj);
			boxObj.newState(CabinetBoxObject.OPENFAULT);
		} else {
			logger.error("abnormal state:" + boxObj.toString());
		}
	}

	public void notifyWait4DoorClosedExpire(CabinetBoxObject boxObj, CabinetBoxContainer ele) {
		logger.info("{}@{} received W4ClosedExpire message", boxObj.getBoxID(), boxObj.getBoxState());

		if (boxObj.getBoxState().equals(CabinetBoxObject.EMPTY_W4CLOSED)) {
			return;
		}

		if (boxObj.getBoxState().equals(CabinetBoxObject.FULL_W4CLOSED)) {
			return;
		}

		logger.error("{} should not receive the notify", boxObj.getBoxID());
	}

	public void notifyDoorClosed(CabinetBoxObject boxObj, CabinetBoxContainer ele) {
		logger.info("{}@{} received door closed message", boxObj.getBoxID(), boxObj.getBoxState());

		if (boxObj.getBoxState().equals(CabinetBoxObject.EMPTY_W4CLOSED)) {
			if (ele.isBatteryExist()) {
				CabinetBoxObject associatedBox = null;

				/* the battery is returned successfully. */
				logger.info("box " + boxObj.toString());
				associatedBox = getAssociatedBox(boxObj);
				removeRequestInstance(boxObj);
				boxObj.killTimer(CabinetBoxObject.WAIT4EDOORCLOSED);

				if (ele.isBatteryExist()) {
					boxObj.newState(CabinetBoxObject.IDLE);
				} else {
					/* exception generate warn...? */
					boxObj.newState(CabinetBoxObject.IDLE);
				}

				if (associatedBox != null) {
					/* publish to device/cabinetID */
					DownstreamCabinetMessage message = new DownstreamCabinetMessage(associatedBox.getCabinetID(),
							InteractionCommand.DOWN_MODIFY, InteractionCommand.DOWN_SUB_OPEN,
							associatedBox.getID() + 1);
					message.checkAndSetDeviceID();
					message.publish(message);

					associatedBox.newState(CabinetBoxObject.FULL_W4OPENED);
					associatedBox.setTimer(CabinetBoxObject.WAIT4FDOOROPENED, 30);
				}

				return;
			} else {
				boxObj.killTimer(CabinetBoxObject.WAIT4EDOORCLOSED);
				/* send exchange failed message */

				sendExchangeSucceededNotify(boxObj, "Exchange failed, no battery....");
				wait4EmptyBoxClosedFailed(boxObj);
				return;
			}
		}

		if (boxObj.getBoxState().equals(CabinetBoxObject.FULL_W4CLOSED)) {
			if (ele.isBatteryNotExist()) {
				/* exchange succeeded!!!!!!!!!!!!!!!!! */
				boxObj.newState(CabinetBoxObject.IDLE);
				boxObj.killTimer(CabinetBoxObject.WAIT4FDOORCLOSED);
				/* send exchange succeed message */

				sendExchangeSucceededNotify(boxObj, "Exchange succeeded");
				removeRequestInstance(boxObj);
			} else {
				/* exception generate warn...? */
				boxObj.newState(CabinetBoxObject.IDLE);
				boxObj.killTimer(CabinetBoxObject.WAIT4FDOORCLOSED);
				sendExchangeSucceededNotify(boxObj, "Exchange failed, battery should not exist.");
			}
		}

		boxObj.store2Redis();
		removeRequestInstance(boxObj);
	}

}
