package drive;

import java.util.ArrayList;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import vision.VisionData;
import vision.VisionTarget;
import constants.Constants;
import edu.wpi.first.wpilibj.PIDController;

public class GearStrafeOmniWheel extends DriveControlDecorator{
	private double TARGET = 10, BUFFER = 0.05, SPEED = 0.075;
	private double p = 0.1, i = 0, d = 0;
	private PIDController pid;
	private PIDOutputDouble speed;
	private PIDSourceDouble distance;
	private DriveIO driveIO;
	private boolean initialized = false;

	public GearStrafeOmniWheel(DriveControl driveControl) {
		super(driveControl);
		driveIO = DriveIO.getInstance();
		speed = new PIDOutputDouble();
		distance = new PIDSourceDouble();
		pid = new PIDController(p, i, d, distance, speed, 0.02);
		pid.setOutputRange(-0.4, 0.4);
		pid.enable();
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		double wheel = driveIO.getOmniWheel();
		System.out.println("Omni Wheel: " + wheel);
		distance.setValue(-wheel);
		System.out.println("Target: " + pid.getSetpoint());
		System.out.println("Speed: " + speed.getValue());
		
		if(Math.abs(speed.getValue()) < 0.25 && Math.abs(pid.getError()) < 0.2) {
			driveIO.setGearStrafe(false);
			super.execute(x1, y1, x2, y2);
		}
		else if(driveIO.getGearStrafe()){
			if (!initialized) {
				driveIO.resetOmniWheel();
				pid.setSetpoint(getStrafeDistance());
				initialized = true;
			}
			
//			if(getStrafeDistance() > TARGET)
//				super.execute(-SPEED, y1, x2, y2);
//			else
//				super.execute(SPEED, y1, x2, y2);
			super.execute(speed.getValue(), y1, x2, y2);
		}
		else
			super.execute(x1, y1, x2, y2);
		
		
	}
	
	private double getStrafeDistance() {
		ArrayList<VisionTarget> targets = VisionData.getInstance().getGearTargets();
		
		double middleX = Constants.GEAR_PIC_WIDTH/2;
		if (targets != null && targets.size() > 0) {
			for (VisionTarget t1: targets) {
				for (VisionTarget t2: targets) {
					if (Math.abs(t1.getHeight() - t2.getHeight()) < 10) {
						middleX = (t1.getCenterX() + t2.getCenterX())/2;
					}
				}
			}
		}
		
		return calcDistance(middleX);
	}
	
	private double calcDistance(double x){
		double angleToTurn = Utilities.calculateAngle(x , Constants.GEAR_PIC_WIDTH/2, Constants.GEAR_PIC_WIDTH, Constants.GEAR_HORIZONTAL_FOV);
		return Math.tan(Math.toRadians(angleToTurn)) * DriveIO.getInstance().getFrontSonarInches();
	}
//	
//	private double getBiggerX(){
//		double maxX = -1;
//		ArrayList<VisionTarget> targets = VisionData.getInstance().getGearTargets();
//		
//		if(targets != null && targets.size() > 0){
//			maxX = targets.get(0).getCenterX();
//			for(VisionTarget t : targets){
//				maxX = Math.max(t.getCenterX(), maxX);
//			}
//		}
//		return maxX;
//	}

}
