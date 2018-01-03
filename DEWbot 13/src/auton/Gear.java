package auton;

import utilities.ControlInputs;
import constants.ControllerConstants;
import drive.DriveIO;
import drive.DriveIO.GearStatus;

public class Gear implements AutonCommand{
	private String name;
	private boolean in, done;
	
	public Gear(boolean in, String name){
		this.in = in;
		this.name = name;
	}

	@Override
	public void execute() {
		if(DriveIO.getInstance().getGearStatus() != GearStatus.FAILED){
			double value = in ? 1 : 0;	
			ControlInputs.getDriver().setAxis(ControllerConstants.GEAR_DRIVER, value);
			System.out.println("In gear. Setting value: " + value);
		}
		else{
			System.out.println("Auton failed. Not placing gear");
		}
		done = true;

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
	}

}
