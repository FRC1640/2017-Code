package shooter;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.ControlInputs;
import utilities.Controller;
import constants.PortConstants;
import edu.wpi.first.wpilibj.Servo;

public class ShooterHoodIO {
	private static ShooterHoodIO shooterHoodIO;

	private Servo servo;
	private Controller op;
	
	//super far: 15
	private final int SHOOTING_ANGLE = 102, RETRACTED_ANGLE = 153, CLOSE_ANGLE = 102, FAR_ANGLE = 102; //Close: 115 Far: 35
	private final double HORIZONTAL_SHOOTER_RANGE_MINIMUM = 50, HORIZONTAL_SHOOTER_RANGE_MAXIMUM = 80; //The Minimum and Maximum 'Real-World' Ranges
	private int target = 999;
	private int angle = target;
	private int prevPOV = 1;
	private boolean climbing;
	private boolean retracted = false, close;
	private int prevTarget = 0;
	
	public static ShooterHoodIO getInstance() {
		if(shooterHoodIO == null)
			shooterHoodIO = new ShooterHoodIO();
		return shooterHoodIO;
	}
	
	private ShooterHoodIO() {
		System.out.println("Initialized");
		servo = new Servo(PortConstants.ServoShooterHood);
		op = ControlInputs.getOp();
		servo.setAngle(SHOOTING_ANGLE);
	}
	
	public void update() {
		if(climbing){
			angle = RETRACTED_ANGLE;
		}
		else if(Robot.getState() == State.AUTON && target != 999){
			angle = target;
		}
		else if(close){
			angle = CLOSE_ANGLE;
		}
		else{
			angle = FAR_ANGLE;
		}
		
		servo.setAngle(angle);
		
		prevTarget = target;
	}
	
		//retracted means you can climb
	public void setState(boolean isRetracted) {
		climbing = isRetracted;
		System.out.println("Climbing? : " + climbing);
	}
	
	
	public int horizontalToNativeDegrees(int degrees){
		double domainDegrees;
		
		if(degrees >= HORIZONTAL_SHOOTER_RANGE_MINIMUM && degrees <= HORIZONTAL_SHOOTER_RANGE_MAXIMUM)
		{
			domainDegrees = degrees;
		}
		else if(degrees < HORIZONTAL_SHOOTER_RANGE_MINIMUM)
		{
			domainDegrees = HORIZONTAL_SHOOTER_RANGE_MINIMUM;
		}
		else
		{
			domainDegrees = HORIZONTAL_SHOOTER_RANGE_MAXIMUM;
		}
		
		return (int) Math.round((domainDegrees - HORIZONTAL_SHOOTER_RANGE_MINIMUM) / (HORIZONTAL_SHOOTER_RANGE_MAXIMUM - HORIZONTAL_SHOOTER_RANGE_MINIMUM) * 180);
 	}
	
	public void setDistance(boolean close){
		this.close = close;
	}
	
	public void setAngle(int angle) {
		target = angle;
	}
	
	public void resetAngle() {
		target = 999;
	}
}
