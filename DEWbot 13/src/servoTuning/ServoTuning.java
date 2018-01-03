package servoTuning;

import utilities.Subsystem;

public class ServoTuning extends Subsystem{
	private ServoTuningIO servoTuningIO;

	@Override
	public void update() {
		servoTuningIO.update();
	}

	@Override
	public void init() {
		servoTuningIO = ServoTuningIO.getInstance();
	}

}
