package auton;

import utilities.Utilities;
import drive.DriveIO;

public class Turn implements AutonCommand{
	private String name;
	public enum TurnType {RELATIVE, ABSOLUTE};
	private TurnType type;
	private double angle;
	private boolean initialized = false, done = false; 
	
	public Turn(double angle, TurnType turnType, String name){
		this.angle = angle;
		this.type = turnType;
		this.name = name;
	}
	@Override
	public void execute() {
		if(!initialized){
			System.out.println("Setting angle");
			if(type == TurnType.ABSOLUTE)
				DriveIO.getInstance().setTurnAngle(angle);
			else{
				DriveIO.getInstance().setTurnAngle(Utilities.relativeToAbsolute(DriveIO.getInstance().getYaw(), angle));
			}
			initialized = true;
		}
		done = DriveIO.getInstance().getTurnAngle() == 999;
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
		done = false;
	}

}
