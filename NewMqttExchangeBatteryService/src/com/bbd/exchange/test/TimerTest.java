package com.bbd.exchange.test;

import com.bbd.exchange.control.BoxTimerMessage;
import com.bbd.exchange.control.TimerMgr;

public class TimerTest {

	public static void main(String[] args) {
/*		ScheduledExecutorService newScheduledThreadPool = Executors.newScheduledThreadPool(6);
		BoxTimerTask task = new BoxTimerTask(new BoxTimerMessage("aaa","bbb","w4boxopened"));
		newScheduledThreadPool.schedule(task, 1000, TimeUnit.MILLISECONDS);
*/
		TimerMgr.setTimer(new BoxTimerMessage("HDG-00001238","1","ew4boxopened"), 2);
	}

}
