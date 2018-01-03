package auton;


import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Vector;
import constants.ControllerConstants;
import drive.DriveIO;

public class FieldCentric implements AutonCommand{
	private String name;
	private boolean on;
	private boolean done = false;
	
	public FieldCentric(boolean on, String name){
		this.on = on;
		this.name = name;
	}

	@Override
	public void execute() {
		ControlInputs.getDriver().setButton(ControllerConstants.FIELD_CENTRIC_DRIVER, on);
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
