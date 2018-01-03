package climber;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import pdpManagement.PDPManagerIO;
import shooter.ShooterHoodIO;
import utilities.ControlInputs;

import com.ctre.CANTalon;

import constants.ControllerConstants;
import constants.PortConstants;

public class ClimberIO {
	private static ClimberIO climberIO;
	private CANTalon climber;
	private final double CLIMB_SPEED = 1.0;
	private final double SLOW_SPEED = 1.0, FAST_SPEED = 1, FAST_THRESHOLD = 3.0;
	private final double DELAY_FOR_SHOOTER_RETRACT = 500;
	private double speed = 0.0;
	private double prevDirection;// = 0.0;
	private long startTime = 0;
	private boolean retract = false;
	private boolean retracting = false;
	private boolean prevRetracted;
	private boolean retracted = false;
	private PDPManagerIO pdpManagerIO;
	private double current = 0;
	private int iterations = 0;

	private ShooterHoodIO shooterHoodIO;// = ShooterHoodIO.getInstance();

	private ClimberIO(){
		climber = new CANTalon(PortConstants.MotorClimberID);
		shooterHoodIO = ShooterHoodIO.getInstance();
		pdpManagerIO = PDPManagerIO.getInstance();
	}
	
	public static ClimberIO getInstance(){
		if(climberIO == null)
			climberIO = new ClimberIO();
		return climberIO;
	}
	
	public void update(){
		
		current = pdpManagerIO.getCurrent(PortConstants.MotorClimber);
		// Prime standing current: 2.65
//		System.out.println("Current: "  + current);
		
		if(current > FAST_THRESHOLD){
			speed = FAST_SPEED;
		}
		else{
			speed = SLOW_SPEED;
		}
		
		boolean shooting = ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_CLOSE_OP) > 0.75 ||
				//ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_FAR_OP) > 0.75 ||
				ControlInputs.getOp().getButton(ControllerConstants.SHOOT_CLOSE_SLOW_OP) ||
				ControlInputs.getOp().getButton(ControllerConstants.SHOOT_FAR_SLOW_OP) ||
				ControlInputs.getDriver().getButton(ControllerConstants.SHOOT_FAR_DRIVER) ||
				ControlInputs.getDriver().getButton(ControllerConstants.SHOOT_CLOSE_DRIVER);
		
		double direction = 0;
		if(ControlInputs.getOp().getButton(ControllerConstants.CLIMBER_OP)){
			direction = 1;
			retracted = true;
		}
		// New code
		else if (ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_FAR_OP) > 0.2) {
			direction = ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_FAR_OP);
			System.out.println("Using new climber axis");
		}
		// end
		else if(shooting){
			retracted = false;
		}
		climber.set(direction * speed);
		
		if(retracted != prevRetracted){
			ShooterHoodIO.getInstance().setState(retracted);
		}
		prevRetracted = retracted;

		
		//bolt code
//		double direction = 0;
//		if(ControlInputs.getOp().getButton(ControllerConstants.CLIMBER_OP)){
//			direction = 1;
//			retract = true;
//		}
////		else if(ControlInputs.getOp().getButton(Button.BUTTON_A)){
////			direction = -1;
////			retract = true;
//////			System.out.println("A is pressed");
////		}
//		else {
//			direction = 0;
//			retract = false;
////			System.out.println("Speed set to 0");
//		}
//		
//		if (direction != prevDirection && !retracted && Robot.getState() == State.TELEOP) {
//			startTime = (long) (System.nanoTime() / 1000000.0);
//			shooterHoodIO.setState(retract);
//			retracting = true;
//		}
//		if (System.nanoTime() / 1000000.0 - startTime > DELAY_FOR_SHOOTER_RETRACT || retracted) {
//			if (retracting) retracted = true;
//			climber.set(speed * direction);
//		}
//		else {
//			climber.set(0);
////			System.out.println("Climber set to 0");
//		}
		prevDirection = direction;
	}

}
