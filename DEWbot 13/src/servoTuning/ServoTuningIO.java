package servoTuning;

import utilities.ControlInputs;
import utilities.Controller;
import utilities.Controller.Button;
import constants.PortConstants;
import edu.wpi.first.wpilibj.Servo;

public class ServoTuningIO {
	private static ServoTuningIO servoTuningIO;
	private Servo servo;
	private Controller driver;
	private double prevAngle;
	private int prevPOV;
	private final int CHANNEL = PortConstants.ServoGearRight;
	private double MIN_VALUE = 0, MAX_VALUE = 180, SMALL_INCREMENT = 1, MED_INCREMENT = 10, LARGE_INCREMENT = 20;
//	private double MIN_VALUE = 0, MAX_VALUE = 1, SMALL_INCREMENT = 0.05, MED_INCREMENT = 0.1, LARGE_INCREMENT = 0.2;
	
	private ServoTuningIO(){
		servo = new Servo(CHANNEL);
		driver =  ControlInputs.getDriver();
	}
	
	public static ServoTuningIO getInstance(){
		if(servoTuningIO == null)
			servoTuningIO = new ServoTuningIO();
		return servoTuningIO;
	}
	
	public void update(){
		double angle = prevAngle;
		
		/*
		 * Button B: set to min
		 * Button X: set to max
		 * Button Y: set to middle
		 */
		if(driver.getButton(Button.BUTTON_B)){
			angle = MIN_VALUE;
		}
		else if(driver.getButton(Button.BUTTON_X)){
			angle = MAX_VALUE;
		}
		else if(driver.getButton(Button.BUTTON_Y)){
			angle = (MIN_VALUE + MAX_VALUE) / 2;
		}
		
		/*
		 * POV up / down: +- small increment
		 * POV & right bumper: +- med increment
		 * POV & right bumper & left bumper +- high bumper
		 */
		else if(driver.getPOV() != prevPOV){
			double direction = driver.getPOV() == 0 ? 1 : driver.getPOV() == 180 ? -1 : 0;
			if(driver.getButton(Button.RIGHT_BUMPER)){
				if(driver.getButton(Button.LEFT_BUMPER))
					direction *= LARGE_INCREMENT;
				else
					direction *= MED_INCREMENT;
			}
			else{
				direction *= SMALL_INCREMENT;
			}
			angle += direction;
		}
		
		angle = angle < MIN_VALUE ? MIN_VALUE : angle;
		angle = angle > MAX_VALUE ? MAX_VALUE : angle;
		
		if(angle != prevAngle){
			servo.setAngle(angle);
			System.out.println("New servo angle for channel " + CHANNEL + " is " + angle);
		}
		prevAngle = angle;
		prevPOV = driver.getPOV();
	}

}
