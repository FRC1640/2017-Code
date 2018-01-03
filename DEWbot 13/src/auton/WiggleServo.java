package auton;


import gearIntake.GearIntakeIO;

public class WiggleServo implements AutonCommand{
	private String name;
	private boolean initialized = false, done;
	private int iteration;
	private long startTime;
	private double time, wiggle = 20;
	private boolean direction;
	
	public WiggleServo(double time, String name){
		this.time = time;
		this.name = name;
	}

	@Override
	public void execute() {
		if(!initialized){
			startTime = (long) System.nanoTime() / 1000000;
			initialized = true;
			GearIntakeIO.getInstance().adjustServo(direction ? wiggle/2 : -wiggle/2);
		}
		else{
			iteration++;
			if(System.nanoTime() / 1000000 - startTime > time){
				done = true;
				GearIntakeIO.getInstance().reset();
			}
			if(iteration % 15 == 0){
//				System.out.println("Adjusting");
				direction = !direction;
//				if (!GearIntakeIO.getInstance().isShifted()) {
					GearIntakeIO.getInstance().adjustServo(direction ? wiggle : -wiggle);
//				}
			}
		}
	}
	
	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void reset() {
		initialized = false;
	}

}
