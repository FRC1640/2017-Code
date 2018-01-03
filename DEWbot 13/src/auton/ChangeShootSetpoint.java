package auton;

import shooter.ShooterHoodIO;
import shooter.ShooterIO;

public class ChangeShootSetpoint implements AutonCommand {
	private String name;
	private boolean on;
	private ShooterIO shooterIO;
	private int setPoint;
	
	public ChangeShootSetpoint(int setPoint, String name) {
		shooterIO = ShooterIO.getInstance();
		this.setPoint = setPoint;
		this.name = name;
		System.out.println("Changing Shoot Setpoint");
	}

	@Override
	public void execute() {
		shooterIO.setShootingSpeed(setPoint);
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
