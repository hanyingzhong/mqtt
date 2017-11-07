package com.bbd.exchange.mqtt;

public class UpstreamDisassociateMessage extends UpstreamCabinetMessage {

	public UpstreamDisassociateMessage(String verb) {
		super(verb);
	}

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
