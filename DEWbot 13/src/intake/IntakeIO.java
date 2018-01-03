package intake;

import pdpManagement.PDPManagerIO;
import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Controller.Button;

import com.ctre.CANTalon;

import constants.ControllerConstants;
import constants.PortConstants;
import edu.wpi.first.wpilibj.Servo;

public class IntakeIO {
	private static IntakeIO intakeIO;
	private CANTalon intake;
//	private Servo lid;
	private final double MAX_CURRENT = 70;
	private PDPManagerIO pdpManagerIO;
	private boolean direction = true;
	private boolean prevX = false, prevB = false;
	private final double INTAKE_SPEED = 0.7, MIN_SPEED = 0.4;
	private double speed = 0;
	private boolean y = false;
	private double current = 0;
	private int iterations = 0;
	
	private IntakeIO(){
		pdpManagerIO = PDPManagerIO.getInstance();
		intake = new CANTalon(PortConstants.MotorIntakeID);
//		lid = new Servo(PortConstants.ServoIntakeLid);
		
//		PowerDistributionPanel pdp = new PowerDistributionPanel(0);

	}
	
	public static IntakeIO getInstance(){
		if(intakeIO == null)
			intakeIO = new IntakeIO();
		return intakeIO;
	}
	
	public void update(){
		current = pdpManagerIO.getCurrent(PortConstants.MotorIntake);
		
		//prime standing current = 8.25
		//prime current max around 30
		
		if (Math.abs(current) > MAX_CURRENT) {
			speed = 0.0;
		}
		else {
			if(ControlInputs.getDriver().getAxis(ControllerConstants.INTAKE_DRIVER) > MIN_SPEED){
				speed = INTAKE_SPEED * ControlInputs.getDriver().getAxis(ControllerConstants.INTAKE_DRIVER);
			}
			else{
				speed = 0;
			}
		}
		
		y = ControlInputs.getDriver().getButton(ControllerConstants.SWITCH_INTAKE_DRIVER);
		
		intake.set(y ? -speed : speed);
		
		//Lid
//		if(ControlInputs.getOp().getButton(Button.BUTTON_X) && !prevB){
//			lid.set(0.25);
//		}
//		else {
//			lid.set(1.0); //0.65
//		}
		
//		System.out.println("Intake Current: " + current);

	}
}
