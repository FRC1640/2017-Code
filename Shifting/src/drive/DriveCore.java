package drive;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.ControlInputs;
import utilities.Controller;
import utilities.Controller.Axis;
import constants.ControllerConstants;

public class DriveCore {
	private static DriveCore driveCore;
	private boolean prevA;
	private Controller driver = ControlInputs.getDriver();
	private boolean shifting;
	private DriveControl DEFAULT_DRIVE = new FieldCentric(AngleTuning.getInstance());
	private DriveControl SHIFTING_DRIVE = new FieldCentric(ShiftingSwerveBase.getInstance());
	private DriveControl driveState = DEFAULT_DRIVE;
	private State prevState = Robot.getState();
	
	private DriveCore(){}
	
	public static DriveCore getInstance(){
		if(driveCore == null)
			driveCore = new DriveCore();
		return driveCore;
	}
	
	public void update(){
		boolean switchShifting = driver.getButton(ControllerConstants.SWITCH_SHIFTING_DRIVER);
		boolean resetGyro = driver.getButton(ControllerConstants.RESET_GYRO_DRIVER);
		double x1 = driver.getAxis(Axis.X1);
		double y1 = driver.getAxis(Axis.Y1);
		double x2 = driver.getAxis(Axis.X2);
		double y2 = driver.getAxis(Axis.Y2);
		
		if(resetGyro){
			DriveIO.getInstance().resetGyro();
		}

		if(switchShifting && !prevA){
			shifting = !shifting;
			driveState.deconstruct();
			if(shifting)
				driveState = DEFAULT_DRIVE;
			else
				driveState = SHIFTING_DRIVE;
		}
		
		if(Robot.getState() != prevState){
			if(Robot.isDisable())
				for(Pivot p : DriveIO.getInstance().getPivots())
					p.disable();
			else 
				for(Pivot p : DriveIO.getInstance().getPivots())
					p.enable();
		}
		prevState = Robot.getState();
		driveState.execute(x1, y1, x2, y2);
		prevA = switchShifting;
	}
}
