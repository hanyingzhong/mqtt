package com.bbd.exchange.service;

import com.bbd.exchange.control.CabinetBoxObject;

public class RemoteExchangeRequest {
	CabinetBoxObject associatedBox;
	
	ExchangeRequestMessage message;

	public RemoteExchangeRequest(CabinetBoxObject associatedBox, ExchangeRequestMessage message) {
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

	public ExchangeRequestMessage getMessage() {
		return message;
	}

	public void setMessage(ExchangeRequestMessage message) {
		this.message = message;
	}
}
