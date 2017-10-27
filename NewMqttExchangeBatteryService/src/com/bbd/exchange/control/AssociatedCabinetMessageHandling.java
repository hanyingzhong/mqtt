package com.bbd.exchange.control;

import com.bbd.exchange.mqtt.UpstreamCabinetMessage;

public class AssociatedCabinetMessageHandling implements CabinetMessageHandling{
	private static final AssociatedCabinetMessageHandling INSTANCE = new AssociatedCabinetMessageHandling();

	public static final AssociatedCabinetMessageHandling getInstance() {
		return AssociatedCabinetMessageHandling.INSTANCE;
	}
	
	public void handling(UpstreamCabinetMessage msg) {
		DeviceMgrContainer.getInstance().insertCabinet(msg.getDeviceID(), msg.getCabinetID());
	}
}
