package com.bbd.exchange.platform;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bbd.exchange.message.ExchangeMessage;

public class ServiceMessageHandling extends Thread {
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

	@Override
	public void run() {
		ExchangeMessage message;
		Timer timer = new Timer();  
		timer.schedule(new TimerTask() {  
		        public void run() {  
		            //System.gc();  
		            //System.out.println("hehe");
		        }  
		}, 2000, 2000); 
		
		while (true) {
			while ((message = ServiceMessageQueue.getInstance().pull()) != null) {
				// System.gc();
				
//				 ServiceMessageHandlingTask task = new ServiceMessageHandlingTask(message);
//				 WeakReference<ServiceMessageHandlingTask> wref = new WeakReference<ServiceMessageHandlingTask>(task);
//				 fixedThreadPool.execute(task);
				fixedThreadPool.execute(new ServiceMessageHandlingTask(message));
			}
		}
	}
}
