package com.bbd.exchange.control;

import java.util.ArrayList;

public class CabinetControlObject implements ExchangeControlObject{
	String cabinetID;
	ArrayList<CabinetBoxObject> boxList = new ArrayList<CabinetBoxObject>();
	private int boxNumberOfCabinet;
	
	public CabinetControlObject(String cabinetID, int boxNumberOfCabinet) {
		this.cabinetID = cabinetID;
		this.boxNumberOfCabinet = boxNumberOfCabinet;
		
		for(int idx = 1;idx < boxNumberOfCabinet+1; idx++) {
			set(idx, new CabinetBoxObject(cabinetID, idx));
		}
	}

	public CabinetControlObject(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	void set(int id, CabinetBoxObject obj) {
		boxList.set(id,  obj);
	}

	public int getBoxNumberOfCabinet() {
		return boxNumberOfCabinet;
	}

}
