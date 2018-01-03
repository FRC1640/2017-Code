package drive;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.ControlInputs;
import utilities.Controller.Button;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;

public class GyroCorrection extends DriveControlDecorator{ //allows robot to drive straight using gyro (mostly for auton)
	private PIDSourceDouble shortestDistance;
	private PIDOutputDouble correctedX2;
	private DriveIO driveIO = DriveIO.getInstance();
	private double gyroSetpoint, x2Prev;
	private PIDController gyroPID;
	private long startTime;
	private double TURN_DELAY = 400;
	private State prevState;
//	private double pAuton = 0.01/*0.0225*/, iAuton = 0.0001, dAuton = 0; /old 10/25/17
	private double pAuton = 0.005/*0.0225*/, iAuton = 0.000, dAuton = 0.001;

	private double pTeleop = 0.005, iTeleop = 0, dTeleop = 0.001;

	public GyroCorrection(DriveControl driveControl) {
		super(driveControl);
		shortestDistance = new PIDSourceDouble(); 
		correctedX2 = new PIDOutputDouble(); 
		gyroPID = new PIDController(pAuton, iAuton, dAuton, shortestDistance, correctedX2, 0.02); 
		gyroPID.setOutputRange(-0.5, 0.5);
		gyroPID.enable();
//		if(Robot.getState() == State.AUTON)
//			gyroSetpoint = 0;
//		else
			gyroSetpoint = driveIO.getFCYaw();
		System.out.println("Initial setpoint: " + gyroSetpoint);
	}
	
	public void execute(double x1, double y1, double x2, double y2){
		double speed = x2;
		if(ControlInputs.getDriver().getButton(Button.START)){ 
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
			if(Robot.getState() == State.AUTON && x2Prev != 0){
				gyroSetpoint = driveIO.getFCYaw();
				System.out.println("Setpoint: " + gyroSetpoint);
			}
			if(Robot.getState() == State.TELEOP && System.nanoTime() / 1000000.0 - startTime < TURN_DELAY){//if(x2Prev != 0){ 
				gyroSetpoint = driveIO.getFCYaw();
				speed = 0;
			}
			else{
				speed = getSpeed(x1, y1, x2);
			}
		}
		else 
			startTime = (long) (System.nanoTime() / 1000000.0);
		
		if(y1 == 0 && x1 == 0){
			if(Robot.getState() == State.AUTON)
				speed = x2;
			else if(x2 == 0){
				gyroSetpoint = driveIO.getFCYaw();
			}
		}
		x2Prev = x2;
		prevState = state;
		
		super.execute(x1, y1, speed, y2);
	}
	
	private double getSpeed(double x1, double y1, double x2){
		shortestDistance.setValue(Utilities.shortestAngleBetween(driveIO.getFCYaw(), gyroSetpoint));
		
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
