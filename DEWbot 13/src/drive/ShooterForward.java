package drive;

import utilities.ControlInputs;
import utilities.Controller;

public class ShooterForward extends DriveControlDecorator{

	public ShooterForward(DriveControl driveControl) {
		super(driveControl);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2) {
		super.execute(-y1, x1, x2, y2);
	}
	
}
