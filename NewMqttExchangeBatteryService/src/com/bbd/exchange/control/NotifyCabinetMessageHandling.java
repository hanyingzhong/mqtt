package com.bbd.exchange.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.mobile.ExchangeRequestMgr;
import com.bbd.exchange.mqtt.CabinetBoxContainer;
import com.bbd.exchange.mqtt.DownstreamCabinetMessage;
import com.bbd.exchange.mqtt.InteractionCommand;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;
import com.bbd.exchange.service.ExchangeServiceRequestMgr;

public class NotifyCabinetMessageHandling implements CabinetMessageHandling {
	private static final Logger logger = LoggerFactory.getLogger(NotifyCabinetMessageHandling.class);
	private static final NotifyCabinetMessageHandling INSTANCE = new NotifyCabinetMessageHandling();

	public static final NotifyCabinetMessageHandling getInstance() {
		return NotifyCabinetMessageHandling.INSTANCE;
	}

	void doorOpenedHandling(CabinetBoxObject boxObj, CabinetBoxContainer ele) {
		logger.info("{}@{} received door opened message", boxObj.getBoxID(), boxObj.getBoxState());
	}
	
	void doorOpenedSucceedHandling(CabinetBoxObject boxObj, CabinetBoxContainer ele) {

	}
	
	void doorOpenedFailedHandling(CabinetBoxObject boxObj, CabinetBoxContainer ele) {

	}

	void doorClosedHandling(CabinetBoxObject boxObj, CabinetBoxContainer ele) {
		CabinetControlObject mgrObj = CabinetMgrContainer.getInstance().getCabinetControlObject(ele.getCabinetID());

		logger.info("{}@{} received door closed message", boxObj.getBoxID(), boxObj.getBoxState());
		
		if (boxObj.getBoxState().equals(CabinetBoxObject.EMPTY_W4CLOSED)) {
			if (ele.isBatteryExist()) {
				CabinetBoxObject associatedBox = null;

				/* the battery is returned successfully. */
				logger.info("box " + boxObj.toString());
				associatedBox = ExchangeRequestMgr.getInstance().getAssociatedBox(boxObj);
				ExchangeRequestMgr.getInstance().removeRequestInstance(boxObj);
				
				mgrObj.move2EmptyBoxHashSet(boxObj);
				if (ele.isBatteryExist()) {
					boxObj.newState(CabinetBoxObject.IDLE);
				}else {
					/*exception generate warn...?*/
					boxObj.newState(CabinetBoxObject.IDLE);
				}
				
				if (associatedBox != null) {
					/* publish to device/cabinetID */
					DownstreamCabinetMessage message = new DownstreamCabinetMessage(associatedBox.getCabinetID(),
							InteractionCommand.DOWN_MODIFY, InteractionCommand.DOWN_SUB_OPEN,
							associatedBox.getID() + 1);
					message.checkAndSetDeviceID();
					message.publish(message);
				}
			}
		}
		
		if (boxObj.getBoxState().equals(CabinetBoxObject.FULL_W4CLOSED)) {
			if (ele.isBatteryNotExist()) {
				boxObj.newState(CabinetBoxObject.IDLE);
			}else {
				/*exception generate warn...?*/
				boxObj.newState(CabinetBoxObject.IDLE);

			}	
		}
		
		boxObj.store2Redis();
		ExchangeRequestMgr.getInstance().removeRequestInstance(boxObj);	
	}

	void doorW4ClosedExpireHandling(CabinetBoxObject boxObj, CabinetBoxContainer ele) {
	}
	
	@Override
	public void handling(UpstreamCabinetMessage msg) {
		CabinetControlObject mgrObj = CabinetMgrContainer.getInstance().getCabinetControlObject(msg.getCabinetID());

		if (mgrObj == null) {
			logger.error("{} is not configured.", msg.getCabinetID());
			return;
		}

		for (CabinetBoxContainer ele : msg.getBoxList()) {
			CabinetBoxObject boxObj = CabinetMgrContainer.getInstance().getCabinetBox(msg.getCabinetID(),
					ele.getId() - 1);

			logger.info(boxObj.toString());
			boxObj.setBoxAttr(ele);
			logger.info("new:=========>");
			logger.info(boxObj.toString());

			/***
			 * The case only happened when maintaining status
			 * ***/
			if (ele.getResponseCode() == InteractionCommand.RESCODE_OPENED) {
				doorOpenedHandling(boxObj, ele);
				continue;
			}
			
			/* door opened successfully...*/
			if (ele.getResponseCode() == InteractionCommand.RESCODE_OPENSUCC) {
				ExchangeServiceRequestMgr.getInstance().notifyDoorOpenedSucceed(boxObj, ele);
				continue;
			}
			
			/* door wasn't opened. exception...lock is error...*/
			if (ele.getResponseCode() == InteractionCommand.RESCODE_OPENFAIL) {
				ExchangeServiceRequestMgr.getInstance().notifyDoorOpenedFailed(boxObj, ele);
				continue;
			}

			if (ele.getResponseCode() == InteractionCommand.RESCODE_W4CLOSEDEXPIRE) {
				//doorW4ClosedExpireHandling(boxObj, ele);
				ExchangeServiceRequestMgr.getInstance().notifyWait4DoorClosedExpire(boxObj, ele);
				continue;
			}		
			
			if (ele.getResponseCode() == InteractionCommand.RESCODE_CLOSED) {
				//doorClosedHandling(boxObj, ele);
				ExchangeServiceRequestMgr.getInstance().notifyDoorClosed(boxObj, ele);
				continue;
			}
		}
	}

}
