package agitator;

import utilities.Subsystem;

public class Agitator extends Subsystem {
	AgitatorIO agitatorIO = AgitatorIO.getInstance();

	@Override
	public void update() {
		agitatorIO.update();
	}

	@Override
	public void init() {
	}

}
