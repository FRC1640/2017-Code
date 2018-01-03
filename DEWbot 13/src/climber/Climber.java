package climber;

import utilities.Subsystem;

public class Climber extends Subsystem{
	private ClimberIO climberIO;
	
	@Override
	public void update() {
		climberIO.update();
	}

	@Override
	public void init() {
		climberIO = ClimberIO.getInstance();
	}

}
