package com.bbd.exchange.mobile;

import com.bbd.exchange.control.CabinetBoxObject;
import com.bbd.exchange.control.CabinetMgrContainer;
import com.bbd.exchange.control.DeviceMgrContainer;
import com.bbd.exchange.mqtt.DownstreamCabinetMessage;
import com.bbd.exchange.mqtt.InteractionCommand;

public class MobileExchangeMqttMessageHandling implements Runnable {
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
			/*
			 * device does not associate...
			 */
			return;
		}

		/*
		 * choose the box being empty
		 */
		CabinetBoxObject boxObj = CabinetMgrContainer.getInstance().getEmptyCabinetBox(cabinetID);
		if (boxObj == null) {
			System.out.println("no emtpy cabinet box found. ERROR");
			return;
		}

		/**
		 * set box state.....
		 * 
		 */
		boxObj.newState(boxObj.SELCTED);
		/*
		 * modify database state....
		 * 
		 */

		/* publish to device/cabinetID */
		DownstreamCabinetMessage message = new DownstreamCabinetMessage(cabinetID, InteractionCommand.DOWN_MODIFY,
				InteractionCommand.DOWN_SUB_OPEN, boxObj.getID() + 1);
		message.checkAndSetDeviceID();
		
		message.publish(message);
	}

}
