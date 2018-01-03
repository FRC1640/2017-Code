package auton;

public class Time implements AutonCommand{ //command used to delay for certain time
	private long delay, startTime;
	private boolean firstIteration = true;
	private String name;
	
	public Time(long msDelay, String name){
		//record inputs
		delay = msDelay;
		this.name = name;
	}

	@Override
	public void execute() {
	}

	@Override
	public boolean isRunning() {
		if (firstIteration){ //reset start time on first iteration
			startTime = (long) (System.nanoTime() / 1000000.0);
			firstIteration = false;
		}
		return !((System.nanoTime() / 1000000.0) - startTime >= delay); //command is done if the specified time has passed
	}

	@Override
	public boolean isInitialized() { //is the command initialized
		return true;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}
	
	@Override 
	public void reset() {
		firstIteration = true;
	}
}
