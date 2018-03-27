package com.bbd.exchange.control;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.mqtt.CabinetBoxContainer;
import com.bbd.exchange.mqtt.DownLightAllCabinetBoxMessage;
import com.bbd.exchange.mqtt.DownLightCabinetBoxMessage;
import com.bbd.exchange.mqtt.DownstreamAssociationAckMessage;
import com.bbd.exchange.mqtt.InteractionCommand;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;
import com.bbd.exchange.util.PropertyUtil;

public class AssociatedCabinetMessageHandling implements CabinetMessageHandling {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

	private static final AssociatedCabinetMessageHandling INSTANCE = new AssociatedCabinetMessageHandling();

	public static final AssociatedCabinetMessageHandling getInstance() {
		return AssociatedCabinetMessageHandling.INSTANCE;
	}

	void sendAssociateAck(String deviceID, String cabinetID) {
		/*
		 * DownstreamCabinetMessage dmsg = new DownstreamCabinetMessage(cabinetID,
		 * InteractionCommand.UP_ASSOCIATEACK, InteractionCommand.DOWN_SUB_ACK, 0);
		 * dmsg.setDeviceID(deviceID); dmsg.publish(dmsg);
		 * logger.info("send associate ACK to {}/{}", deviceID, cabinetID);
		 */
		DownstreamAssociationAckMessage dmsg = new DownstreamAssociationAckMessage(cabinetID);
		dmsg.setDeviceID(deviceID);
		dmsg.publish(dmsg);
		logger.info("send associate ACK to {}/{}", deviceID, cabinetID);
	}

	void sendDownLightMessage(String deviceID, String cabinetID, int boxId) {
		DownLightCabinetBoxMessage dmsg = new DownLightCabinetBoxMessage(cabinetID, boxId, true, false);
		dmsg.setDeviceID(deviceID);
		dmsg.publish(dmsg);
		logger.info("send down light message to {}/{}"+boxId, deviceID, cabinetID);
	}

	void sendDownLightAllBoxMessage(String deviceID, String cabinetID, List<CabinetBoxContainer> boxList) {
		DownLightAllCabinetBoxMessage dmsg = new DownLightAllCabinetBoxMessage(cabinetID, boxList);
		dmsg.setDeviceID(deviceID);
		dmsg.publish(dmsg);
		logger.info("send down light message to {}/{}", deviceID, cabinetID);
	}
	
	public void handling(UpstreamCabinetMessage msg) {
		DeviceMgrContainer.getInstance().insertCabinet(msg.getDeviceID(), msg.getCabinetID());
		CabinetControlObject cabinetObj = CabinetMgrContainer.getInstance().getCabinetControlObject(msg.getCabinetID());

		sendAssociateAck(msg.getDeviceID(), msg.getCabinetID());

		if (null == cabinetObj) {
			logger.error("{} is not configured.", msg.getCabinetID());
			return;
		}
		cabinetObj.setState(CabinetControlObject.ONLINE);
		cabinetObj.setDeviceId(msg.getDeviceID());

		/**
		 * update cabinet-box status, including box status/battery status etc...
		 * ele.getId() start from 1....
		 * 
		 */
		for (CabinetBoxContainer ele : msg.getBoxList()) {
			CabinetBoxObject boxObj = CabinetMgrContainer.getInstance().getCabinetBox(msg.getCabinetID(),
					ele.getId() - 1);

			boxObj.setBoxAttr(ele);

			if ((false == ele.isDoorOpened()) && (false == ele.isBatteryExist())) {
				cabinetObj.move2EmptyBoxHashSet(boxObj);
			} else if ((false == ele.isDoorOpened()) && (true == ele.isBatteryExist())) {
				cabinetObj.move2NonEmptyBoxHashSet(boxObj);
			} else {
				cabinetObj.move2ExceptionBoxHashSet(boxObj);
			}

			logger.info("{}", boxObj);
			boxObj.store2Redis();
		}
		
		sendDownLightAllBoxMessage(msg.getDeviceID(), msg.getCabinetID(), msg.getBoxList());
/*		for (CabinetBoxContainer ele : msg.getBoxList()) {
			CabinetBoxObject boxObj = CabinetMgrContainer.getInstance().getCabinetBox(msg.getCabinetID(),
					ele.getId() - 1);

			if (true == ele.isBatteryExist()) {
				sendDownLightMessage(msg.getDeviceID(), msg.getCabinetID(), ele.getId());
				break;
			}
		}*/
	}
}
