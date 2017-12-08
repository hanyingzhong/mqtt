package com.bbd.exchange.execute;

import com.bbd.exchange.platform.CommonClientMqttMsgCallback;
import com.bbd.exchange.platform.CommonExchangeMqttClient;
import com.bbd.exchange.platform.RedisIntf;
import com.bbd.exchange.platform.ServiceMessageHandling;
import com.bbd.exchange.table.CabinetManager;
import com.bbd.exchange.table.IoTDeviceManager;
import com.bbd.exchange.util.MqttCfgUtil;

public class ExchangeMainEntrance {
	static CommonExchangeMqttClient newMqttClient;

	@SuppressWarnings("static-access")
	public static void main(String[] args) {

		newMqttClient = new CommonExchangeMqttClient(MqttCfgUtil.getServerUri(), MqttCfgUtil.getUsername(),
				MqttCfgUtil.getPassword(), "DSEX-CLIENT002R", null);

		try {
			newMqttClient.initClient(new CommonClientMqttMsgCallback(newMqttClient));
			newMqttClient.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		new ServiceMessageHandling().start();

		if (newMqttClient.isConnected()) {
			newMqttClient.sendSubscribe("exchangeResponse/#");
			newMqttClient.sendSubscribe("exbattery/#");
			System.out.println("connected.");
		}

		IoTDeviceManager.getInstance().loadFromRedis();
		CabinetManager.getInstance().loadFromRedis();
		
/*		System.out.println(RedisIntf.getMap(2, "EB000001").toString());
		System.out.println(RedisIntf.getSet(1, "HDG-000011").toString());
*/		
		while (true) {
			try {
				Thread.currentThread().sleep(1000);
				if (!newMqttClient.isConnected()) {
					System.out.println("connection lost.....reconnect");
					newMqttClient.connect();
					newMqttClient.sendSubscribe("exchangeResponse/#");
					newMqttClient.sendSubscribe("exbattery/#");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
