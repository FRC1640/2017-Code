package drive;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import constants.ControllerConstants;
import utilities.ControlInputs;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;

public class GyroCorrection extends DriveControlDecorator{ //allows robot to drive straight using gyro (mostly for auton)
	private PIDSourceDouble shortestDistance;
	private PIDOutputDouble correctedX2;
	private PIDController gyroPID;
	
	private DriveIO driveIO = DriveIO.getInstance();
	private double gyroSetpoint, x2Prev;
	private long startTime;
	private double TURN_DELAY = 400;
	private State prevState;
	private double pAuton = 0.01, iAuton = 0.0001, dAuton = 0;
	private double pTeleop = 0.005, iTeleop = 0, dTeleop = 0.001;

	public GyroCorrection(DriveControl driveControl) {
		super(driveControl);
		shortestDistance = new PIDSourceDouble(); 
		correctedX2 = new PIDOutputDouble(); 
		gyroPID = new PIDController(pAuton, iAuton, dAuton, shortestDistance, correctedX2, 0.02); 
		gyroPID.setOutputRange(-0.5, 0.5);
		gyroPID.enable();
		gyroSetpoint = driveIO.getYaw();
	}
	
	public void execute(double x1, double y1, double x2, double y2){
		double speed = x2;
		if(ControlInputs.getDriver().getButton(ControllerConstants.RESET_GYRO_DRIVER)){ 
			gyroSetpoint = 0;
			System.out.println("reset");
		}
		
		State state = Robot.getState();
		if(state != prevState){
			gyroPID.disable();
			if(state == State.AUTON){
				gyroPID.setPID(pAuton, iAuton, dAuton);
			}
			else{
				gyroPID.setPID(pTeleop, iTeleop, dTeleop);
			}
			gyroPID.reset();
			gyroPID.enable();
		}
		
		if(x2 == 0){ 
			if(Robot.isAuton() && x2Prev != 0){
				gyroSetpoint = driveIO.getYaw();
			}
			if(Robot.isTeleop() && System.nanoTime() / 1000000.0 - startTime < TURN_DELAY){//if(x2Prev != 0){ 
				gyroSetpoint = driveIO.getYaw();
				speed = 0;
			}
			else{
				speed = getSpeed(x1, y1, x2);
			}
		}
		else 
			startTime = (long) (System.nanoTime() / 1000000.0);
		
		if(y1 == 0 && x1 == 0){
			if(Robot.isAuton())
				speed = x2;
			else if(x2 == 0){
				gyroSetpoint = driveIO.getYaw();
			}
		}
		x2Prev = x2;
		prevState = state;
		
		super.execute(x1, y1, speed, y2);
	}
	
	private double getSpeed(double x1, double y1, double x2){
		shortestDistance.setValue(Utilities.shortestAngleBetween(driveIO.getYaw(), gyroSetpoint));
		
		double buffer = 0.1;
		
		if (x1 < 0.2 && y1 < 0.2) {
			buffer = 0.01;
		}
		else {
			buffer = 0.005;
		}
		
		return (Math.abs(correctedX2.getValue()) < buffer ? x2 : correctedX2.getValue());
	}

}
