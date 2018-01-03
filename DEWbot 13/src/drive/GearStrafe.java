package drive;

import java.util.ArrayList;

import constants.Constants;
import edu.wpi.first.wpilibj.PIDController;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import vision.VisionData;
import vision.VisionTarget;

public class GearStrafe extends DriveControlDecorator{
	private double TARGET = Constants.GEAR_PIC_WIDTH / 2, BUFFER = 1, SPEED = 0.075;
	private double p = 0.0009, i = 0, d = 0;
	private PIDController pid;
	private PIDOutputDouble speed;
	private PIDSourceDouble distance;
	private int[] frameCounts;
	private int FRAME_COUNT_SIZE = 10, index, iterations;

	public GearStrafe(DriveControl driveControl) {
		super(driveControl);
		speed = new PIDOutputDouble();
		distance = new PIDSourceDouble();
		pid = new PIDController(p, i, d, distance, speed, 0.02);
		pid.setSetpoint(TARGET);
		pid.enable();
		frameCounts = new int[FRAME_COUNT_SIZE];
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		distance.setValue(getMidX());
		iterations++;
		if(y1 != 0){
			if(iterations % 10 == 0)
//				System.out.println("Mid X: " + getMidX() + " Speed: " + speed.getValue());
			super.execute(-speed.getValue(), y1, x2, y2);
		}
		else
			super.execute(x1, y1, x2, y2);
		
		
	}
	
	private double getMidX(){
		if(!frameCountChanged()){
			System.out.println("OpenCV has died");
			return TARGET;
		}
		ArrayList<VisionTarget> targets = VisionData.getInstance().getGearTargets();
		if(targets.size() == 0){
			System.out.println("Too many/little targets. Target size: " + targets.size());
			return TARGET;
		}
		else if(targets.size() == 1){
			System.out.println("Only one target");
			return TARGET;
//			double x = targets.get(0).getCenterX();
//			if (x > TARGET) {
//				System.out.println("Target on right side");
//				return x + 70;
//			}
//			else {
//				System.out.println("Target on left side");
//				return x - 70;
//			}
		}
		else if (targets.size() > 2){
			return filterByArea(targets);
		}
		else{
			double average = targets.get(0).getCenterX() + targets.get(1).getCenterX();
			average /= 2;
			return average;
		}
		
	}
	
	private boolean frameCountChanged(){
		frameCounts[index] = VisionData.getInstance().getGearFrameCount();
		index++;
		if(index == frameCounts.length)
			index = 0;
		
		boolean changed = false;
		for(int i = 1; i < frameCounts.length; i++){
			if(frameCounts[i] != frameCounts[i - 1])
				changed = true;
		}
		return changed;
	}
	
	private double filterByArea(ArrayList<VisionTarget> targets) {
		double area1 = 0, area2 = 0;
		double centerX1 = 0, centerX2 = 0;
		
		for (VisionTarget target : targets) {
			double a = target.getArea();
			double x = target.getCenterX();
			if (a > area1) {
				area2 = area1;
				centerX2 = centerX1;
				area1 = a;
				centerX1 = x;
			}
			else if (a > area2) {
				area2 = a;
				centerX2 = x;
			}
		}
		
		return (centerX1 + centerX2)/2;
	}

}
