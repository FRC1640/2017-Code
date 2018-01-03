package gearIntake;

import utilities.Subsystem;

public class GearIntake extends Subsystem {
	private GearIntakeIO gearIntakeIO;

	@Override
	public void update() {
		gearIntakeIO.update();
	}

	@Override
	public void init() {
		gearIntakeIO = GearIntakeIO.getInstance();
	}
	
}
