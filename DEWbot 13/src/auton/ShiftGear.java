package auton;

import gearIntake.GearIntakeIO;

import java.util.ArrayList;

import constants.Constants;
import vision.VisionData;
import vision.VisionTarget;
import drive.DriveIO;
import drive.DriveIO.GearStatus;

public class ShiftGear implements AutonCommand {
	private double DEFAULT_TARGET = Constants.GEAR_PIC_WIDTH / 2;//49;
	private double SHIFT_BUFFER = 0;
	private String name = "";
	private boolean done;
	private boolean firstIteration = true;
	private int SHIFT = 7;
	private boolean left;

	public ShiftGear(boolean left, String name) {
		this.name = name;
		this.left = left;
	}
	
	@Override
	public void execute() {
//		GearIntakeIO.getInstance().adjustServo(getDirection()*SHIFT);
		double value = left ? 1 : -1;
		GearIntakeIO.getInstance().adjustServo(value * SHIFT);
	}

	private int getDirection() {
		double current = getBiggerX();
		
		if(current == -1){
//			DriveIO.getInstance().setGearStatus(GearStatus.FAILED);
			DriveIO.getInstance().setGearStatus(GearStatus.NO_SHIFT);
			System.out.println("Shifting failed");
			return 0;
		}
		else if ((Math.abs(DEFAULT_TARGET - current) < SHIFT_BUFFER)) {
			DriveIO.getInstance().setGearStatus(GearStatus.NO_SHIFT);
			System.out.println("Shift none. Bigger X: " + getBiggerX());
			return 0;
			
		}
		else if (DEFAULT_TARGET > current) { //shift left
			DriveIO.getInstance().setGearStatus(GearStatus.SHIFTED_LEFT);
			System.out.println("Shift left. Bigger X: " + getBiggerX());
			return -1;
		}
		else { //shift right
			DriveIO.getInstance().setGearStatus(GearStatus.SHIFTED_RIGHT);
			System.out.println("Shift right. Bigger X: " + getBiggerX());
			return 1;
		}	
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

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void reset() {
	
	}
}
