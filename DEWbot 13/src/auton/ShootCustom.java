package auton;

import shooter.ShooterIO;
import utilities.ControlInputs;
import utilities.Controller.Button;
import constants.ControllerConstants;
import drive.DriveIO;

public class ShootCustom implements AutonCommand {
	private String name;
	private boolean done, prevDoneAligning, firstIteration = true;
	private double offset;
	private int angle, setpoint;
	
	public ShootCustom(int angle, int setpoint, double offset, String name) {
		this.name = name;
		this.angle = angle;
		this.setpoint = setpoint;
		this.offset = offset;
	}

	@Override
	public void execute() {
		if(firstIteration){
			DriveIO.getInstance().setAlignOffset(offset);
			ShooterIO.getInstance().setShootingSettings(setpoint, angle);
			ControlInputs.getDriver().setButton(ControllerConstants.SHOOT_CLOSE_DRIVER, true);
			firstIteration = false;
			System.out.println("Shooter custom set");
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
		
	}

}
