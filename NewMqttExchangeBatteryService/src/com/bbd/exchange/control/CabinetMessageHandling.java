package com.bbd.exchange.control;

import com.bbd.exchange.mqtt.UpstreamCabinetMessage;

public interface CabinetMessageHandling {
	public void handling(UpstreamCabinetMessage msg);
}
