package angleAdjustment;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import powerManagement.Motor;
import powerManagement.MotorsManager;

import com.ctre.CANTalon.TalonControlMode;

import constants.PortConstants;

public class AngleAdjustmentIO {
	private static AngleAdjustmentIO angleAdjustmentIO;
	private Motor hood;
	private double angleSetpoint, prevSetpoint;
	private Robot.State prevState;
	
	private AngleAdjustmentIO(){
//		hood = MotorsManager.createMotor(PortConstants.MotorAngleAdjustmentID);
		hood.changeControlMode(TalonControlMode.Position);
		//TODO: set up PID
	}
	
	public static AngleAdjustmentIO getInstance(){
		if(angleAdjustmentIO == null)
			angleAdjustmentIO = new AngleAdjustmentIO();
		return angleAdjustmentIO;
	}
	
	public void update(){
		if(Robot.getState() == State.DISABLED){
			hood.reset();
		}
		else if(prevState == State.DISABLED){
			hood.enable();
		}
		
		if(angleSetpoint != prevSetpoint){
			hood.setSetpoint(convertToVolts(angleSetpoint));
		}
		
		prevState = Robot.getState();
	}
	
	public void setAngle(double angleSetpoint){
		this.angleSetpoint = angleSetpoint;
	}
	
	public double getAngle(){
		return convertToDegrees(hood.getAnalogInPosition());
	}
	
	public double getAngleSetpoint(){
		return angleSetpoint;
	}
	
	private double convertToDegrees(double volts){
		//TODO: convert analog to degrees
		return 0;
	}
	
	private double convertToVolts(double degrees){
		return 0;
	}
}
