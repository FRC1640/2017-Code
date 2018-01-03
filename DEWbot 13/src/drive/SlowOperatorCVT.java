package drive;

import utilities.ControlInputs;
import utilities.Controller;
import utilities.Controller.Axis;

public class SlowOperatorCVT extends DriveControlDecorator{
	private Controller op = ControlInputs.getOp();
	private final double DRIVE_REDUCTION = 0.3;
	private final double STEER_REDUCTION = 0.2;

	public SlowOperatorCVT(DriveControl driveControl) {
		super(driveControl);
	}

	@Override
	public void execute(double x1, double y1, double x2, double y2){
		double opX1 = op.getAxis(Axis.X1);
		double opY1 = -op.getAxis(Axis.Y1);
		double opX2 = op.getAxis(Axis.X2);
		
		if(Math.abs(opX1) > Math.abs(y1) || Math.abs(opY1) > Math.abs(x1) || Math.abs(opX2) > Math.abs(x2)){
//			double direction = opX2 / Math.abs(opX2);
//			double newX2 = Math.sqrt(Math.abs(opX2)) * direction * STEER_REDUCTION;
			DriveIO.getInstance().setOpControl(true);
			super.execute(-opY1 * DRIVE_REDUCTION, opX1 * DRIVE_REDUCTION, opX2 * STEER_REDUCTION, y2);
//			super.execute(opY1 * REDUCTION, opX1 * REDUCTION, opX2 * REDUCTION, y2);
		}
		else{
			DriveIO.getInstance().setOpControl(false);
			super.execute(x1, y1, x2, y2);
		}
	}
	
}
