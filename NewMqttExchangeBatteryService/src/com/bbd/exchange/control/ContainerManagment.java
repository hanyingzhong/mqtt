package com.bbd.exchange.control;

public interface ContainerManagment {
	public boolean insertCabinet(String deviceID, String cabinetID);
	public boolean removeCabinet(String deviceID, String cabinetID);
	public boolean removeCabinet(String cabinetID);
	public boolean removeDevice(String deviceID);
	
	public String getDeviceByCabinet(String cabinetID);
}
