package com.bbd.exchange.mqtt;

public class CabinetBoxID {
	String cabinetID;
	int ID;

	public CabinetBoxID(String cabinetID, int iD) {
		super();
		this.cabinetID = cabinetID;
		ID = iD;
	}

	public String getCabinetID() {
		return cabinetID;
	}

	public void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
