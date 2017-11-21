package com.bbd.exchange.control;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbd.exchange.util.PropertyUtil;

public class TimerMgr {
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

	private static int MAX_TIEMR_THEAD_POOL = 512;
	private final static ScheduledExecutorService newScheduledThreadPool = Executors
			.newScheduledThreadPool(MAX_TIEMR_THEAD_POOL);
	private static int timerUsed = 0;

	private static void increase() {
		timerUsed++;
		if (timerUsed > MAX_TIEMR_THEAD_POOL) {
			logger.error("TimerMgr timer used exceed the max value" + MAX_TIEMR_THEAD_POOL);
		}
	}

	public static void decrease() {
		timerUsed--;
	}

	public static int getTimeUsed() {
		return timerUsed;
	}

	public static ScheduledFuture<?> setTimer(BoxTimerMessage message, int seconds) {
		BoxTimerTask task = new BoxTimerTask(message);

		logger.info("TimerMgr set timer : " + message.toString());
		increase();
		return newScheduledThreadPool.schedule(task, seconds * 1000, TimeUnit.MILLISECONDS);
	}

	public static ScheduledFuture<?> setTimer(Runnable command, int delaySeconds) {
		increase();
		return newScheduledThreadPool.schedule(command, delaySeconds * 1000, TimeUnit.MILLISECONDS);
	}
}
