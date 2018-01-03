package shooter;

import utilities.Subsystem;

public class Shooter extends Subsystem{
	
	private ShooterHoodIO shooterHoodIO;// = ShooterHoodIO.getInstance();
	private ShooterIO shooterIO = ShooterIO.getInstance();
	
	@Override
	public void update() {
		shooterIO.update();
		shooterHoodIO.update();
	}

	@Override
	public void init() {
		shooterHoodIO = ShooterHoodIO.getInstance();
	}
}
