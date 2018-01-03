package powerManagement;

import utilities.Subsystem;

public class PowerManagement extends Subsystem {
	public  static MotorsManager motors;

	@Override
	public void update() {
		motors.update();
	}

	@Override
	public void init() {
		motors = MotorsManager.getInstance();
	}

}
