package com.bbd.exchange.mqtt;

public class DownCabinetStateSyncMessage extends DownstreamCabinetMessage {
	public DownCabinetStateSyncMessage(String text, int parseInt) {
		super(text, InteractionCommand.DOWN_MODIFY, InteractionCommand.DOWN_SUB_SYNCBOXSTATUS, parseInt);
	}
}
