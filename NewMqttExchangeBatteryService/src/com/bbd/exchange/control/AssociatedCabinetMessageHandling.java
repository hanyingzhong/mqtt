package com.bbd.exchange.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.mqtt.CabinetBoxContainer;
import com.bbd.exchange.mqtt.DownstreamCabinetMessage;
import com.bbd.exchange.mqtt.InteractionCommand;
import com.bbd.exchange.mqtt.UpstreamCabinetMessage;
import com.bbd.exchange.util.PropertyUtil;
import com.bbd.exchange.util.RedisUtils;

import redis.clients.jedis.Jedis;

public class AssociatedCabinetMessageHandling implements CabinetMessageHandling {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

	private static final AssociatedCabinetMessageHandling INSTANCE = new AssociatedCabinetMessageHandling();

	public static final AssociatedCabinetMessageHandling getInstance() {
		return AssociatedCabinetMessageHandling.INSTANCE;
	}

	void store2Redis(CabinetBoxObject boxObj) {
		Jedis jedis = RedisUtils.getJedis();
		jedis.select(2);
		jedis.hmset(boxObj.getBoxID(), boxObj.getMap());
		RedisUtils.returnResource(jedis);
	}

	void sendAssociateAck(String deviceID, String cabinetID) {
		DownstreamCabinetMessage dmsg = new DownstreamCabinetMessage(cabinetID, InteractionCommand.UP_ASSOCIATEACK, InteractionCommand.DOWN_SUB_ACK, 0);
		dmsg.setDeviceID(deviceID);
		dmsg.publish(dmsg);
		logger.info("send associate ACK to {}/{}", deviceID, cabinetID);
	}
	
	public void handling(UpstreamCabinetMessage msg) {
		DeviceMgrContainer.getInstance().insertCabinet(msg.getDeviceID(), msg.getCabinetID());
		CabinetControlObject cabinetObj = CabinetMgrContainer.getInstance().getCabinetControlObject(msg.getCabinetID());

		sendAssociateAck(msg.getDeviceID(), msg.getCabinetID());
		
		if (null == cabinetObj) {
			logger.error("{} is not configured.", msg.getCabinetID());
			return;
		}

		/**
		 * update cabinet-box status, including box status/battery status etc...
		 * ele.getId() start from 1....
		 * 
		 */
		for (CabinetBoxContainer ele : msg.getBoxList()) {
			CabinetBoxObject boxObj = CabinetMgrContainer.getInstance().getCabinetBox(msg.getCabinetID(),
					ele.getId() - 1);

			boxObj.setBoxAttr(ele);

			if ((false == ele.isDoorOpened()) && (false == ele.isBatteryExist())) {
				cabinetObj.move2EmptyBoxHashSet(boxObj);
			} else if ((false == ele.isDoorOpened()) && (true == ele.isBatteryExist())) {
				cabinetObj.move2NonEmptyBoxHashSet(boxObj);
			} else {
				cabinetObj.move2ExceptionBoxHashSet(boxObj);
			}

			logger.info("{}", boxObj);
			store2Redis(boxObj);
		}
	}
}
