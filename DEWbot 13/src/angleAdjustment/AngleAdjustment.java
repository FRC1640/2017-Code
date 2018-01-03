package angleAdjustment;

import utilities.Subsystem;

public class AngleAdjustment extends Subsystem{
	private AngleAdjustmentIO angleAdjustmentIO;
	
	@Override
	public void update() {
		angleAdjustmentIO.update();
	}

	@Override
	public void init() {
		angleAdjustmentIO = AngleAdjustmentIO.getInstance();
	}

}
