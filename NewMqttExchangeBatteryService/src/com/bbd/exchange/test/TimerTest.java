package com.bbd.exchange.test;

import com.bbd.exchange.control.BoxTimerMessage;
import com.bbd.exchange.control.CabinetBoxObject;
import com.bbd.exchange.control.TimerMgr;
import com.bbd.exchange.service.ExchangeTimerLengthMgr;
import com.bbd.exchange.util.IdGenerator;

public class TimerTest {

	public static void main(String[] args) {
/*		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(6);
		BoxTimerTask task = new BoxTimerTask(new BoxTimerMessage("aaa","bbb","w4boxopened"));
		newScheduledThreadPool.schedule(task, 1000, TimeUnit.MILLISECONDS);
*/
		IdGenerator clientIdGenerator = new IdGenerator("Client");
		
		ExchangeTimerLengthMgr.setTimerLength(CabinetBoxObject.WAIT4EDOOROPENED, 50);
		System.out.println(ExchangeTimerLengthMgr.getTimerLength(CabinetBoxObject.WAIT4EDOOROPENED));
		System.out.println(clientIdGenerator.generateId());
		System.out.println(clientIdGenerator.generateId());
			
		TimerMgr.setTimer(new BoxTimerMessage("HDG-00001238","1","ew4boxopened"), 2);
	}

}
