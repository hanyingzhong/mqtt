package com.bbd.exchange.mobile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.control.CabinetBoxObject;
import com.bbd.exchange.control.CabinetControlObject;
import com.bbd.exchange.control.CabinetMgrContainer;
import com.bbd.exchange.control.DeviceMgrContainer;
import com.bbd.exchange.mqtt.DownstreamCabinetMessage;
import com.bbd.exchange.mqtt.InteractionCommand;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;

public class MobileExchangeMqttMessageHandling implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(UpstreamCabinetMessage.class);

	ClientRequestMessage message;

	public MobileExchangeMqttMessageHandling(ClientRequestMessage message) {
		this.message = message;
	}

	@Override
	public void run() {
		handling();
	}

	@SuppressWarnings("static-access")
	void handling() {
		String cabinetID = message.getCabinetID();

		String device = DeviceMgrContainer.getInstance().getDeviceByCabinet(cabinetID);
		if (device == null) {
			logger.error("{} not associated. please check GPRS/NB device if works normally", message.getCabinetID());
			return;
		}

		CabinetControlObject mgrObj = CabinetMgrContainer.getInstance().getCabinetControlObject(message.getCabinetID());
		if (mgrObj == null) {
			logger.info("{} not found.", message.getCabinetID());
			return;
		}

		/*
		 * choose the box being empty
		 */
		CabinetBoxObject emptyBoxObj = CabinetMgrContainer.getInstance().getEmptyCabinetBox(cabinetID);
		if (emptyBoxObj == null) {
			logger.info("no emtpy cabinet box found. ERROR");
			return;
		}

		CabinetBoxObject fullEnergyBoxObj = CabinetMgrContainer.getInstance().getCabinetBoxByBatteryType(cabinetID,
				message.getBatteryType());
		if (null == fullEnergyBoxObj) {
			mgrObj.move2EmptyBoxHashSet(emptyBoxObj);
			logger.info("There is no full energy battery{}", message.getBatteryType());
			return;
		}

		/**
		 * set box state.....
		 * 
		 */
		emptyBoxObj.newState(CabinetBoxObject.EMPTY_W4OPENED);
		fullEnergyBoxObj.newState(CabinetBoxObject.FULL_W4OPENED);
		
		emptyBoxObj.storeSingleAttr2Redis(CabinetBoxObject.STATE);
		fullEnergyBoxObj.storeSingleAttr2Redis(CabinetBoxObject.STATE);
		
		/*
		 * store the empty-full-request to the manager......
		 * */
		ExchangeRequestMgr.getInstance().addRequestInstance(emptyBoxObj, fullEnergyBoxObj, message);
		
		/*
		 * modify database state....
		 * 
		 */

		/* publish to device/cabinetID */
		DownstreamCabinetMessage message = new DownstreamCabinetMessage(cabinetID, InteractionCommand.DOWN_MODIFY,
				InteractionCommand.DOWN_SUB_OPEN, emptyBoxObj.getID() + 1);
		message.checkAndSetDeviceID();

		message.publish(message);
	}

}
