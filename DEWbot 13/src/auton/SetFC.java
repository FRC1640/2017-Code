package auton;


import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Controller.Button;
import constants.ControllerConstants;
import drive.DriveIO;

public class SetFC implements AutonCommand{
	private String name;
	private double fcOffset;
	
	public SetFC(double fcOffset, String name){
		this.fcOffset = fcOffset;
		this.name = name;
	}

	@Override
	public void execute() {
		System.out.println("Seting FC in auton" + fcOffset);
		DriveIO.getInstance().setFC(fcOffset);
	}

	@Override
	public boolean isRunning() {
		return true;
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
