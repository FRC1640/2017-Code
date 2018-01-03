package drive;

import utilities.ControlInputs;
import utilities.Controller;

public class OperatorControl extends DriveControlDecorator {

	public OperatorControl(DriveControl driveControl) {
		super(driveControl);
		// TODO Auto-generated constructor stub
	}
	
	public void execute(double x1, double y1, double x2, double y2) {
		x1 = ControlInputs.getOp().getAxis(Controller.Axis.X1);
		y1 = ControlInputs.getOp().getAxis(Controller.Axis.Y1);
		x2 = ControlInputs.getOp().getAxis(Controller.Axis.X2);
		y2 = ControlInputs.getOp().getAxis(Controller.Axis.Y2);
		
		super.execute(x1, -y1, x2, y2);
	}

}
