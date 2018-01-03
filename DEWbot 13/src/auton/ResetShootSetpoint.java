package auton;

import shooter.ShooterHoodIO;
import shooter.ShooterIO;

public class ResetShootSetpoint implements AutonCommand {
	private String name;
	private boolean on;
	private ShooterIO shooterIO;
	
	public ResetShootSetpoint(String name) {
		shooterIO = ShooterIO.getInstance();
		this.name = name;
	}

	@Override
	public void execute() {
		shooterIO.resetShootingSpeed();
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
