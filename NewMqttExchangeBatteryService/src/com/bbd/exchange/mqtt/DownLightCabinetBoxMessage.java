package com.bbd.exchange.mqtt;

public class DownLightCabinetBoxMessage extends DownstreamCabinetMessage {
	final static public String RED = "r";
	final static public String GREEN = "g";
	//status : true, on; false£¬ off;
	//full : true; false
	public DownLightCabinetBoxMessage(String text, int parseInt, boolean status, boolean  full) {
		super(text, InteractionCommand.DOWN_MODIFY, encodeLightSubCommand(status, full ? GREEN : RED), parseInt);
	}

	static String encodeLightSubCommand(boolean status, String color) {
		StringBuffer content = new StringBuffer(); 
		
		content.append(status ? InteractionCommand.DOWN_SUB_LIGHTON : InteractionCommand.DOWN_SUB_LIGHTOFF);
		if(status) {
			content.append(",");
			content.append(color);
		}

		return content.toString();
	}	
}
