package drive;

import constants.ControllerConstants;
import utilities.ControlInputs;
import utilities.Controller;
import utilities.Controller.Axis;
import utilities.Controller.Button;

public class CVTDrive extends DriveControlDecorator {
	private DriveIO driveIO = DriveIO.getInstance();
	private Controller driver = ControlInputs.getDriver();
	private CVTPivot[] pivots;
	private boolean prevA, prevB, prevX, normal, fast, slow;
	private double SCALAR = 0.45;
	
	private final double MIN_VELOCITY = 45/*65*/, FAST_ANGLE = 30, NORMAL_ANGLE = 120, SLOW_ANGLE = 170;
	private double angle;
	
	public CVTDrive(DriveControl driveControl) {
		super(driveControl);
		pivots = driveIO.getPivots();
	}
	
	// executes the CVTDrive functionality
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		double minVelocity = Math.abs(pivots[0].getVelocity());
//		for(CVTPivot p : pivots)
//			if(p.getVelocity() != 0)
//				minVelocity = Math.min(minVelocity, Math.abs(p.getVelocity()));
		//System.out.println("Min Velocity: " + minVelocity);
		
		
		boolean b = ControlInputs.getDriver().getButton(ControllerConstants.SLOW_DRIVER);
		boolean a = ControlInputs.getDriver().getButton(ControllerConstants.FAST_DRIVER);
		boolean x = ControlInputs.getDriver().getButton(ControllerConstants.MED_DRIVER);
		if(b && !prevB){
			slow = true;
			fast = false;
			normal = false;
		}
		if(a && !prevA){
			slow = false;
			fast = true;
			normal = false;
		}
		if(x && !prevX){
			slow = false;
			fast = false;
			normal = true;
		}
		
		if(slow){
			angle = SLOW_ANGLE;
			x1 *= SCALAR;
			y1 *= SCALAR;
			x2 *= SCALAR;
		}
		else if(fast && minVelocity > MIN_VELOCITY){
			angle = FAST_ANGLE;
		}
		else{
			angle = NORMAL_ANGLE;
		}
		
		for(CVTPivot p : pivots){
			p.setServoAngle(angle);
		}
		prevA = a;
		prevB = b;
		prevX = x;
		super.execute(x1, y1, x2, y2);
	}

}
