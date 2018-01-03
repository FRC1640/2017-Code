package gearIntake;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Controller.Button;
import constants.ControllerConstants;
import constants.PortConstants;
import edu.wpi.first.wpilibj.Servo;

public class GearIntakeIO {
	private static GearIntakeIO gearIntakeIO;
	private Servo right, left, rightFlap, leftFlap;
	private double leftIn, rightIn;
	private final double LEFT_FLAP_IN = 110 /*40*/, RIGHT_FLAP_IN = 80 /*131*/;
	private final double LEFT_FLAP_OUT = 40 /*171*/, RIGHT_FLAP_OUT = 20;

//	private final double RIGHT_FLAP_IN = 15, LEFT_FLAP_IN = 15;
//	private final double RIGHT_FLAP_OUT = 130, LEFT_FLAP_OUT = 100;
//
//	private final double LEFT_IN = 63, RIGHT_IN = 67;
//	private final double LEFT_OUT = 18, RIGHT_OUT = 111;

	private final double LEFT_IN = 70, RIGHT_IN = 45;
	private final double LEFT_OUT = 20, RIGHT_OUT = 127;
	private State prevState;
	
	private GearIntakeIO(){
		right = new Servo(PortConstants.ServoGearLeft);
		left = new Servo(PortConstants.ServoGearRight);
		rightFlap = new Servo(PortConstants.ServoGearFlapRight);
		leftFlap = new Servo(PortConstants.ServoGearFlapLeft);
		
		leftIn = RIGHT_IN;
		rightIn = LEFT_IN;
	}
	
	public static GearIntakeIO getInstance(){
		if(gearIntakeIO == null)
			gearIntakeIO = new GearIntakeIO();
		return gearIntakeIO;
	}
	
	public void update(){
		if(ControlInputs.getDriver().getAxis(ControllerConstants.GEAR_DRIVER) > 0.25){
			right.setAngle(LEFT_OUT);
			left.setAngle(RIGHT_OUT);
		}
		// left smaller angle = open, difference of 130
		// right bigger angle = open, difference of 50
		else{
			right.setAngle(rightIn);
			left.setAngle(leftIn);
		}
		
		State state = Robot.getState();
		if(state != prevState){
			reset();
		}
		prevState = state;
		
		if(ControlInputs.getDriver().getButton(ControllerConstants.GEAR_FLAP_DRIVER)){
			rightFlap.setAngle(RIGHT_FLAP_IN);
			leftFlap.setAngle(LEFT_FLAP_IN);
		}
		else{
			if(Robot.getState() == State.AUTON){
				rightFlap.setAngle(RIGHT_FLAP_IN);
				leftFlap.setAngle(LEFT_FLAP_IN);
			}else{
				rightFlap.setAngle(RIGHT_FLAP_OUT);
				leftFlap.setAngle(LEFT_FLAP_OUT);
			}
		}
	}
	
	public void adjustServo(double amount){
//		System.out.println(amount);
		leftIn += amount;
		rightIn += amount;
	}
	
	public void reset(){
		leftIn = RIGHT_IN;
		rightIn = LEFT_IN;
	}
	
	public boolean isShifted() {
		return leftIn != RIGHT_IN || rightIn != LEFT_IN;
	}
}
