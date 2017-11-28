package com.bbd.exchange.mqtt;

public class DownOpenCabinetBoxMessage extends DownstreamCabinetMessage {
	public DownOpenCabinetBoxMessage(String text, int parseInt) {
		super(text, InteractionCommand.DOWN_MODIFY, InteractionCommand.DOWN_SUB_OPEN, parseInt);
	}
}
