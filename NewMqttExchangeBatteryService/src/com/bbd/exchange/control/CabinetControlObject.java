package com.bbd.exchange.control;

import java.util.HashSet;
import java.util.Set;

import com.bbd.exchange.mqtt.CabinetBoxContainer;

public class CabinetControlObject implements ExchangeControlObject {
	final static int defaultBoxNumberOfCabinet = 12;

	String cabinetID;
	CabinetBoxObject[] boxArray;
	private int boxNumberOfCabinet;

	Set<CabinetBoxObject> emptyBoxHashSet = new HashSet<CabinetBoxObject>();
	Set<CabinetBoxObject> nonEmptyBoxHashSet = new HashSet<CabinetBoxObject>();
	Set<CabinetBoxObject> exceptionBoxHashSet = new HashSet<CabinetBoxObject>();
	
	public CabinetControlObject(String cabinetID, int boxNumberOfCabinet) {
		this.cabinetID = cabinetID;
		this.boxNumberOfCabinet = boxNumberOfCabinet;

		boxArray = new CabinetBoxObject[boxNumberOfCabinet];

		for (int idx = 0; idx < boxNumberOfCabinet; idx++) {
			CabinetBoxObject e = new CabinetBoxObject(cabinetID, idx);
			set(idx, e);
			emptyBoxHashSet.add(e);
		}
	}

	public CabinetControlObject(String cabinetID) {
		this.cabinetID = cabinetID;
		this.boxNumberOfCabinet = defaultBoxNumberOfCabinet;
	}

	void set(int id, CabinetBoxObject obj) {
		boxArray[id] = obj;
	}

	public int getBoxNumberOfCabinet() {
		return boxNumberOfCabinet;
	}

	public CabinetBoxObject getEmptyCabinetBox() {
		CabinetBoxObject boxObj = null;
		for (CabinetBoxObject s : emptyBoxHashSet) {
			boxObj = s;
			break;
		}

		emptyBoxHashSet.remove(boxObj);
		return boxObj;
	}

	public CabinetBoxObject getFullEnergyCabinetBox(String batteryType) {
		CabinetBoxObject boxObj = null;
		for (CabinetBoxObject s : nonEmptyBoxHashSet) {
			if(s.getCapacity() > 80) {
				boxObj = s;
				nonEmptyBoxHashSet.remove(boxObj);
				return boxObj;		
			}
		}

		return null;
	}

	
	public void move2EmptyBoxHashSet(CabinetBoxObject e) {
		emptyBoxHashSet.add(e);
		nonEmptyBoxHashSet.remove(e);
		exceptionBoxHashSet.remove(e);
	}
	
	public void move2NonEmptyBoxHashSet(CabinetBoxObject e) {
		nonEmptyBoxHashSet.add(e);
		emptyBoxHashSet.remove(e);
		exceptionBoxHashSet.remove(e);
	}
	
	public void move2ExceptionBoxHashSet(CabinetBoxObject e) {
		exceptionBoxHashSet.add(e);
		emptyBoxHashSet.remove(e);
		nonEmptyBoxHashSet.remove(e);
	}
	
	public void moveBox2HashSet(CabinetBoxObject e, CabinetBoxContainer ele) {
		if(ele.isDoorClosed() && (ele.isBatteryExist() == false)) {
			move2EmptyBoxHashSet(e);
			return;
		}
		
		if(ele.isDoorClosed() && (ele.isBatteryExist() == true)) {
			move2NonEmptyBoxHashSet(e);
			return;
		}
		
	}
	
	public CabinetBoxObject getCabinetBox(int id) {
		if (id >= 0 && id < boxNumberOfCabinet) {
			return boxArray[id];
		}
		
		return null;
	}

	public void show() {
		for (int idx = 0; idx < boxNumberOfCabinet; idx++) {
			CabinetBoxObject e = boxArray[idx];
			System.out.println(e.toString());
		}
	}

}
