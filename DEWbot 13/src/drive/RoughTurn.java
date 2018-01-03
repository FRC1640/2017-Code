package drive;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;


public class RoughTurn extends DriveControlDecorator {
	private DriveIO driveIO = DriveIO.getInstance();
	
	private PIDController PID;
	private PIDSourceDouble delta;
	private PIDOutputDouble speed;
	
	private double target = 998;
	private final double SPEED = 0.4;
	private double BUFFER = 3;

	public RoughTurn(DriveControl driveControl) {
		super(driveControl);
		
		target = driveIO.getRoughTurnAngle();
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		
		target = driveIO.getRoughTurnAngle();
		double turnSpeed = 0;
		if (target != 999) {
			double angle = Utilities.shortestAngleBetween(target, driveIO.getYaw());
			System.out.println("Current: " + driveIO.getYaw() + " angle: " + angle);
				if (Math.abs(angle) < BUFFER) {
					turnSpeed = 0;
					driveIO.setRoughTurnAngle(999);
					target = 999;
					System.out.println("Done. Gyro: " + driveIO.getYaw());
				}
				else{
					turnSpeed = (angle >= 0) ? SPEED : -SPEED;
				}
		}
		else {
			turnSpeed = x2;
		}
		super.execute(x1, y1, turnSpeed, y2);
	}
	
	public void setTarget(double target) {
		this.target = target;
	}

}
