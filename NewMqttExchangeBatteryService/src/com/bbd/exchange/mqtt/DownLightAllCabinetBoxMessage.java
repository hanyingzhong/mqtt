package com.bbd.exchange.mqtt;

import java.util.List;

import com.bbd.exchange.util.NumberUtil;

public class DownLightAllCabinetBoxMessage extends DownstreamCabinetMessage{
	List<CabinetBoxContainer> boxList;
	
	public DownLightAllCabinetBoxMessage(String cabinet, List<CabinetBoxContainer> boxList) {
		super(cabinet, InteractionCommand.DOWN_MODIFY, "", 0);
		this.boxList = boxList;
	}

	int encodeLightBoxInfo(int boxId, String subCmd, byte[] buffer, int pos) {
		int posCurr = pos;
		int id = 0xA000 + boxId;
		//String command = InteractionCommand.getDownSubCommandValue(verb);
		String command = subCmd;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(id), 0, buffer, posCurr, 2);
		posCurr += 2;

		bytesArrayCopy(NumberUtil.unsignedShortToByte2(command.length()), 0, buffer, posCurr, 2);
		posCurr += 2;
		bytesArrayCopy(command.getBytes(), 0, buffer, posCurr, command.length());
		posCurr += command.length();

		return posCurr;
	}
	
	@Override
	public byte[] encode() {
		int pos = 0;
		byte[] msg = new byte[512];

		pos = encodeCabinetID(msg, pos);
		pos = encodeCommand(msg, pos);

		for (CabinetBoxContainer ele : boxList) {
			if (true == ele.isBatteryExist()) {
				pos = encodeLightBoxInfo(ele.getId(), "5,g", msg, pos);
			}
		}
		
		byte[] retMsg = new byte[pos];
		bytesArrayCopy(msg, 0, retMsg, 0, pos);
		return retMsg;
	}

}
