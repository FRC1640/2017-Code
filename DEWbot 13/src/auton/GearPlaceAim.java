package auton;

import constants.ControllerConstants;
import utilities.ControlInputs;
import utilities.Controller;
import drive.DriveIO;

public class GearPlaceAim implements AutonCommand{
	private String name;
	private boolean done, on;
	
	public GearPlaceAim(boolean on, String name){
		this.name = name;
		this.on = on;
	}

	@Override
	public void execute() {
		ControlInputs.getDriver().setPOV(on ? 0 : -1);
		done = true;
	}

	@Override
	public boolean isRunning() {
		return done;
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
//		ControlInputs.getDriver().setButton(ControllerConstants.SHOOT_CLOSE_DRIVER, false);
	}

}
