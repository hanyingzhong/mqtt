package com.bbd.exchange.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.mqtt.UpstreamCabinetMessage;
import com.bbd.exchange.util.PropertyUtil;

public class DisassociateCabinetMessageHandling implements CabinetMessageHandling {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

	private static final DisassociateCabinetMessageHandling INSTANCE = new DisassociateCabinetMessageHandling();

	public static final DisassociateCabinetMessageHandling getInstance() {
		return DisassociateCabinetMessageHandling.INSTANCE;
	}

	@Override
	public void handling(UpstreamCabinetMessage msg) {
		DeviceMgrContainer.getInstance().removeCabinet(msg.getDeviceID(), msg.getCabinetID());
		CabinetControlObject cabinetObj = CabinetMgrContainer.getInstance().getCabinetControlObject(msg.getCabinetID());

		logger.info(msg.getCabinetID() + ": receive disassociate message");
		if (null == cabinetObj) {
			logger.error("{} is not configured.", msg.getCabinetID());
			return;
		}
		
		cabinetObj.setState(CabinetControlObject.OFFLINE);
		cabinetObj.setDeviceId("");
	}

}
