package com.bbd.exchange.test;

import com.bbd.exchange.control.DeviceMgrContainer;

public class DeviceCabinetContainerTest {

	public static void main(String[] args) {
		DeviceMgrContainer container = DeviceMgrContainer.getInstance();
		String cabinet = "HDG-00001238";
		
		container.insertCabinet("DEVICE-000011", cabinet);
		container.insertCabinet("DEVICE-000011", cabinet);
		container.insertCabinet("DEVICE-000011", "HDG-00001238");
		String device = container.getDeviceByCabinet(cabinet);
		
		
		System.out.println(cabinet + " in " + device);
	}

}
