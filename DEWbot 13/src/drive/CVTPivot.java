package drive;

import edu.wpi.first.wpilibj.Servo;

public class CVTPivot extends Pivot{
	// Continuously Variable Transmission Pivot
	
	private Servo servo;

	public CVTPivot(int driveChannel, int steerChannel, int resolverChannel, int servoChannel, double minVoltage, double maxVoltage, double offset, String name) {
		super(driveChannel, steerChannel, resolverChannel, minVoltage, maxVoltage, offset, name);
		
		//System.out.println("Servo Channel: " + servoChannel);
		servo = new Servo(servoChannel);
	}
	
	public void setServoAngle(double angle){
		servo.setAngle(angle);
	}
	
	public double getServoAngle(){
		return servo.getAngle();
	}
	
	public double getServoRaw(){
		return servo.getRaw();
	}
	
	

}
