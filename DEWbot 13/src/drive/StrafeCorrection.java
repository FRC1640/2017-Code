package drive;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import edu.wpi.first.wpilibj.PIDController;

public class StrafeCorrection extends DriveControlDecorator{
	private PIDController pid;
	private PIDSourceDouble displacement;
	private PIDOutputDouble strafeSpeed;
	private double p = 1, i = 0, d = 0;

	public StrafeCorrection(DriveControl driveControl) {
		super(driveControl);
		
		displacement = new PIDSourceDouble();
		strafeSpeed = new PIDOutputDouble();
		pid = new PIDController(p, i, d, displacement, strafeSpeed, 0.02);
		pid.setOutputRange(-0.5, 0.5);
		pid.enable();
		
		pid.setSetpoint(DriveIO.getInstance().getDisplacementY());
		//System.out.println("Setpoint: " + DriveIO.getInstance().getAccel());
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		displacement.setValue(DriveIO.getInstance().getDisplacementY());
		double speed = strafeSpeed.getValue();
		double buffer = 1;
		if(x1 > 0.2 || y1 > 0.2){
			buffer = 0.05;
		}
		System.out.println("Displacement: " + DriveIO.getInstance().getDisplacementY() + " Speed: " + speed);

		speed = Math.abs(speed) < buffer ? 0 : speed;
		super.execute(speed, y1, x2, y2);
		
	}

}
