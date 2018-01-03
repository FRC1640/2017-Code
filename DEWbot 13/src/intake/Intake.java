package intake;

import utilities.Subsystem;

public class Intake extends Subsystem{
	private IntakeIO intakeIO;

	@Override
	public void update() {
		intakeIO.update();
	}

	@Override
	public void init() {
		intakeIO = IntakeIO.getInstance();
	}

}
