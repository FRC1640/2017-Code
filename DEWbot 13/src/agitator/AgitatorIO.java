
package agitator;

import pdpManagement.PDPManagerIO;
import shooter.ShooterIO;
import utilities.ControlInputs;
import constants.ControllerConstants;
import constants.PortConstants;
import drive.DriveIO;

public class AgitatorIO {
	private static AgitatorIO agitatorIO;
	
	public static AgitatorIO getInstance() {
		if (agitatorIO == null)
			agitatorIO = new AgitatorIO();
		return agitatorIO;
	}
	
//	private final double RUNNING_POWER = 0.4;
	private final double SLOW_POWER = 0, FAST_POWER = 0.6;
	private final double UNJAM_POWER = -0.5;
	private final double MAX_CURRENT = 7.0; //4 amps standing
	private final double UNJAM_SLOW = 300000000, UNJAM_FAST = 200000000;
	
	private com.ctre.CANTalon motor;
	private ShooterIO shooterIO;
	
	private PDPManagerIO pdpManagerIO;
	
	boolean prevAgitate = false, prevAligning, doneAligning, started;
	boolean enabled = false;
	
	long startTime = 0;
	long startTimeUnjam = 0;
	
	private double current = 0;
	
	private AgitatorIO() {
		motor = new com.ctre.CANTalon(PortConstants.MotorAgitatorID);
		shooterIO = ShooterIO.getInstance();
		pdpManagerIO = PDPManagerIO.getInstance();
		current = 0;
	}
	
	public void update() {
		double current = pdpManagerIO.getCurrent(PortConstants.MotorAgitator);
		
		if (System.nanoTime() - startTime > 500000000//300000000
				&& System.nanoTime() - startTimeUnjam > 300000000
				&& current > MAX_CURRENT
				&& started) {
			startTimeUnjam = System.nanoTime();
			System.out.println("Unjamming");
		}
		
		boolean aligning = DriveIO.getInstance().getAligning(); 
		if(!aligning && prevAligning){
			doneAligning = true;
			System.out.println("Done aligning");
		}
		else if(aligning && !prevAligning){
			doneAligning = false;
			System.out.println("Not done aligning");
		}

		doneAligning = doneAligning && 
				(ControlInputs.getDriver().getButton(ControllerConstants.SHOOT_FAR_DRIVER) || 
				 ControlInputs.getDriver().getButton(
						 ControllerConstants.SHOOT_CLOSE_DRIVER));
		
		boolean agitateSlow = ControlInputs.getOp().getButton(ControllerConstants.SHOOT_FAR_SLOW_OP) || ControlInputs.getOp().getButton(ControllerConstants.SHOOT_CLOSE_SLOW_OP);
//		boolean agitateFast = ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_FAR_OP) > 0.75 || ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_CLOSE_OP) > 0.75;
		boolean agitateFast = ControlInputs.getOp().getPOV() == 1 || ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_CLOSE_OP) > 0.75;
		boolean agitate = agitateSlow || agitateFast || doneAligning;
		
		if (agitate && !started && shooterIO.getReady()) {
			startTime = System.nanoTime();
			started = true;
			System.out.println("Reset start time");
		}
		double unjamTime = agitateSlow ? UNJAM_SLOW : UNJAM_FAST;
		if (agitate && shooterIO.getReady()) {
			if (System.nanoTime() - startTimeUnjam < unjamTime) {
				motor.set(UNJAM_POWER);
			}
			else if (System.nanoTime() - startTimeUnjam < 350000000) {
				startTime = System.nanoTime();
			}
			else {
				double speed = agitateSlow ? SLOW_POWER : FAST_POWER;
				motor.set(speed);
			}
		}
		else {
			motor.set(0);
		}
		
		if(!agitate && prevAgitate){
			started = false;
		}
		prevAgitate = agitate;
		prevAligning = aligning;
	}
}
