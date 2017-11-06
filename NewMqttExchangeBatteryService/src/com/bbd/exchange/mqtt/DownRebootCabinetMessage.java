package com.bbd.exchange.mqtt;

public class DownRebootCabinetMessage extends DownstreamCabinetMessage {
	public DownRebootCabinetMessage(String text, int parseInt) {
		super(text, InteractionCommand.DOWN_REBOOT, "", parseInt);
	}

	@Override
	public byte[] encode() {
		int pos = 0;
		byte[] msg = new byte[512];

		pos = encodeCabinetID(msg, pos);
		pos = encodeCommand(msg, pos);

		byte[] retMsg = new byte[pos];
		bytesArrayCopy(msg, 0, retMsg, 0, pos);
		return retMsg;
	}
}
