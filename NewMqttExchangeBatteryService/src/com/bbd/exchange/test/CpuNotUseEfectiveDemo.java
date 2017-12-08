package com.bbd.exchange.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CpuNotUseEfectiveDemo {
	private static int executeTimes = 10;  
	private static int taskCount = 200;  

	public static void main(String[] args) throws Exception {  
	    Task task = new Task();  
	    for (int i = 0; i < taskCount; i++) {  
	        task.addTask(Integer.toString(i));  
	    }  

	    long beginTime = System.currentTimeMillis();  
	    for (int i = 0; i < executeTimes; i++) {  
	        System.out.println("Round: " + (i + 1));  
	        Thread thread = new Thread(task);  
	        thread.start();  
	        thread.join();  
	    }  
	    long endTime = System.currentTimeMillis();  
	    System.out.println("Execute summary: Round( " + executeTimes + " ) TaskCount Per Round( " + taskCount   
	            + " ) Execute Time ( " + (endTime - beginTime) + " ) ms");  
	}  

	static class Task implements Runnable {  
	    List<String> tasks = new ArrayList<String>();  
	    Random random = new Random();  
	    boolean exitFlag = false;  

	    public void addTask(String task) {  
	        List<String> copyTasks = new ArrayList<String>(tasks);  
	        copyTasks.add(task);  

	        tasks = copyTasks;  
	    }  
	    @Override  
	    public void run() {  
	        List<String> runTasks = tasks;  
	        List<String> removeTasks = new ArrayList<String>();  
	        for (String task : runTasks) {  
	            try {  
	                Thread.sleep(random.nextInt(10));  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }  

	            removeTasks.add(task);  
	        }  

	        try {  
	            Thread.sleep(10);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	    }  

	}  
}
