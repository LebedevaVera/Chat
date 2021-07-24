package rest;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import websocket.Chat;

public class ChatRobot {
	
	public static final String ROBOT_NAME = "Robot";
	private Timer timer = new Timer();
	private final int minWaitTime = 5;
	protected Random random;
	private boolean enabled = false;
	
	private class Task extends TimerTask {
		@Override
		public void run() {
			if(enabled) {
				Chat.send(random.nextInt());
				timer.schedule(new Task(), getInterval());
			}
		}
	}
	
	public ChatRobot() {
		random = new Random();
	}
	
	protected int getInterval() {
		return (random.nextInt(5) + minWaitTime) * 1000;
	}
	
	public synchronized void enable() {
		if(!enabled) {
			enabled = true;
			timer.schedule(new Task(), getInterval());
			System.out.println("Robot enabled");
		}
	}
	
	public synchronized void disable() {
		if(enabled) {
			enabled = false;
			System.out.println("Robot disabled");
		}
	}
}