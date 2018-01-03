package auton;

import utilities.ControlInputs;
import utilities.Controller;
import drive.DriveIO;

public class StraightDrive implements AutonCommand{
	private String name;
	private boolean done, on;
	
	public StraightDrive(boolean on, String name){
		this.name = name;
		this.on = on;
	}

	@Override
	public void execute() {
		int value = on ? 0 : -1;
		ControlInputs.getDriver().setPOV(value);
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
		done = false;
	}

}
