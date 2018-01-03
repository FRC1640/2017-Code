package auton;

import shooter.ShooterHoodIO;
import utilities.ControlInputs;
import utilities.Controller.Button;

public class ResetShootAngle implements AutonCommand {
	private String name;
	private boolean on;
	private ShooterHoodIO shooterHoodIO;
	
	public ResetShootAngle(String name) {
		shooterHoodIO = ShooterHoodIO.getInstance();
		this.name = name;
	}

	@Override
	public void execute() {
		shooterHoodIO.resetAngle();
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
