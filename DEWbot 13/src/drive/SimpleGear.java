package drive;

import java.util.ArrayList;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import vision.VisionData;
import vision.VisionTarget;
import edu.wpi.first.wpilibj.PIDController;


public class SimpleGear extends DriveControlDecorator{
	private static final double GEAR_PLACE_MIN_X_BUFFER = 1000, CENTER_X_TARGET = 47, STRAFE_SPEED = 0.2, BUFFER = 75;
	private static final double SMALL_BUFFER = 15, BIG_BUFFER = 75;
	private double buffer;
	private VisionTarget[] gearTargets;
	private VisionTarget t1, t2;
	private PIDController pid;
	private PIDSourceDouble centerX;
	private PIDOutputDouble speed;
	private double p = 0.0025, i = 0, d = 0.01, f = 0;
	private boolean done;

	public SimpleGear(DriveControl driveControl){
		super(driveControl);
		
		centerX = new PIDSourceDouble();
		speed = new PIDOutputDouble();
		pid = new PIDController(p, i, d, f, centerX, speed, 0.02);
		pid.setOutputRange(-0.5, 0.5);
		pid.setSetpoint(CENTER_X_TARGET);
		pid.enable();		
		DriveIO.getInstance().setAligning(true);
		
//		buffer = BUFFER;
		buffer = Math.abs(getBiggerX() - CENTER_X_TARGET) < BIG_BUFFER ? SMALL_BUFFER : BIG_BUFFER;
		System.out.println("Buffer: " + buffer + " Current: " + getBiggerX());
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		double strafeSpeed = x1;
		ArrayList<VisionTarget> targets = VisionData.getInstance().getGearTargets();
		if(targets != null && !targets.isEmpty()){
			for(VisionTarget t : targets){
				System.out.println("Target: " + t.getCenterX());
			}
			System.out.println("--------------------");
			if(!done){
				if(getBiggerX() < CENTER_X_TARGET)
					strafeSpeed = -STRAFE_SPEED;
				else
					strafeSpeed = STRAFE_SPEED;
				if(Math.abs(getBiggerX() - CENTER_X_TARGET) < buffer){
					strafeSpeed = 0;
					done = true;
					DriveIO.getInstance().setAligning(false);
				}
			}
//			centerX.setValue(gearTargets[0].getCenterX());
//			System.out.println(getBiggerX() + " : " + strafeSpeed + " : ");
		}
		else {
			System.out.println("Target Empty");
		}
//		System.out.println("Simple Gear Center X: " + getBiggerX());
		super.execute(strafeSpeed, y1, x1, y2);
	}
	
	private double getBiggerX(){
		double maxX = -1;
		ArrayList<VisionTarget> targets = VisionData.getInstance().getGearTargets();
		
		if(targets != null && targets.size() > 0){
			maxX = targets.get(0).getCenterX();
			for(VisionTarget t : targets){
				maxX = Math.max(t.getCenterX(), maxX);
			}
		}
		return maxX;
	}
}
