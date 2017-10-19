package com.bbd.cloudmqtt;

public class ModifyMessasge implements ExchangeMqttMessage {
	String cabinetID;
	int id;

	@Override
	public void handling() {
		System.out.println("start handlle ModifyMessasge");
	}

	String topic;
	byte[] payload;

	public String getCabinetID() {
		return cabinetID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	void encode(String command) {
		String msg;

		if (command.equals("open")) {
			int totalLength = 5;

			topic = "d/" + cabinetID + "/" + "modi";
			msg = "1";
			payload = new byte[totalLength];

			NumberUtil.bytesArrayCopy(NumberUtil.unsignedShortToByte2(id + 0xa000), 0, payload, 0, 2);
			NumberUtil.bytesArrayCopy(NumberUtil.unsignedShortToByte2(1), 0, payload, 2, 2);
			NumberUtil.bytesArrayCopy(msg.getBytes(), 0, payload, 4, msg.length());
		}
	}
}
