package auton;

import shooter.ShooterHoodIO;
import utilities.ControlInputs;
import utilities.Controller.Button;

public class ChangeShootAngle implements AutonCommand {
	private String name;
	private boolean on;
	private ShooterHoodIO shooterHoodIO;
	private int angle;
	
	public ChangeShootAngle(int angle, String name) {
		shooterHoodIO = ShooterHoodIO.getInstance();
		System.out.println("Changed Shoot Angle");
		this.angle = angle;
		this.name = name;
	}

	@Override
	public void execute() {
		shooterHoodIO.setAngle(angle);
	}

	@Override
	public boolean isRunning() {
		return true;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void reset() {
		
	}

}
