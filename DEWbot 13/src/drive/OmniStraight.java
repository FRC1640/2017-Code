package drive;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;


public class OmniStraight extends DriveControlDecorator {
	private DriveIO driveIO = DriveIO.getInstance();
	
	private PIDController PID;
	private PIDSourceDouble delta;
	private PIDOutputDouble speed;

	private static double p = 0.1, i = 0.000025, d = 0.0, f = 0.0;
	
	public OmniStraight(DriveControl driveControl) {
		super(driveControl);
		
		delta = new PIDSourceDouble();
		speed = new PIDOutputDouble();
		PID = new PIDController(p, i, d, f, delta, speed, 0.02);
		PID.setOutputRange(-1.0, 1.0);
		PID.setSetpoint(0.0);
		PID.enable();
		
		driveIO.resetOmniWheel();
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		
		double strafeSpeed = 0.0;
		delta.setValue(-driveIO.getOmniWheel());
		if (y1 > 0.1) {
			strafeSpeed = speed.getValue();
		}
//		System.out.println("Y1: " + y1);
//		System.out.println("OmniWheel: " + driveIO.getOmniWheel());
//		System.out.println("Strafe speed: " + strafeSpeed);
		super.execute(strafeSpeed, y1, x2, y2);
	}

}
