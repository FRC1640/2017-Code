package utilities;

import java.util.Timer;
import java.util.TimerTask;


public abstract class Subsystem {
	//this abstract class holds everything necessary to used the Timer Task for threading
	//to create a thread, simply extend this class, and implement the abstract methods
	//and make sure to start the thread in Robot
	
	//A timer object which will be used to schedule the TimerTask
	private Timer timer;
	
	//will be called when the thread executes
	public abstract void update();
	
	//will be called the first time the thread executes
	public abstract void init();
	
	private class Tasks extends TimerTask{
		private boolean initialized = false;
		
		//this will be called every time the timer task executes
		public void run(){
			if(!initialized){
				init();
				initialized = true;
			}
			update();
		}
		
	}
	
	//start the thread
	public void start(int period){
		if(timer == null){
			timer = new Timer();
			timer.schedule(new Tasks(), 0, period);
		}	
	}
	
	//stop the thread
	public void stop(){
		if(timer != null){
			timer.cancel();
			timer = null;
		}
	}
}
