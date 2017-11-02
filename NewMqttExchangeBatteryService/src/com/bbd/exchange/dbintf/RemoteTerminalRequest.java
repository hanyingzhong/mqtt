package com.bbd.exchange.dbintf;

import com.bbd.exchange.control.CabinetBoxObject;
import com.bbd.exchange.mobile.ClientRequestMessage;

public class RemoteTerminalRequest {
	CabinetBoxObject associatedBox;
	
	ClientRequestMessage message;

	public RemoteTerminalRequest(CabinetBoxObject associatedBox, ClientRequestMessage message) {
		super();
		this.associatedBox = associatedBox;
		this.message = message;
	}

	public CabinetBoxObject getAssociatedBox() {
		return associatedBox;
	}

	public void setAssociatedBox(CabinetBoxObject associatedBox) {
		this.associatedBox = associatedBox;
	}

	public ClientRequestMessage getMessage() {
		return message;
	}

	public void setMessage(ClientRequestMessage message) {
		this.message = message;
	}
}
