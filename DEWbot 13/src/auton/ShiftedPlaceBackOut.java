package auton;

import gearIntake.GearIntakeIO;

import java.util.ArrayList;

import utilities.Vector;
import vision.VisionData;
import vision.VisionTarget;
import drive.DriveIO;
import drive.DriveIO.GearStatus;

public class ShiftedPlaceBackOut implements AutonCommand {
	private String name = "";
	private boolean done;
	private boolean initialized;
	private double distance, prevMagnitude;
	private double angleLeft, angleRight, angleStraight;
	private int state = 0;

	public ShiftedPlaceBackOut(double angleLeft, double angleRight, double angleStraight, double distance, String name) {
		this.name = name;
		this.distance = distance;
		this.angleLeft = angleLeft;
		this.angleRight = angleRight;
		this.angleStraight = angleStraight;
	}
	
	@Override
	public void execute() {
		if(!initialized){
			if (state == 0) {
				DriveIO.getInstance().resetEncoders();
				double angle = angleStraight;
				if(DriveIO.getInstance().getGearStatus() == GearStatus.SHIFTED_LEFT){
					angle = angleLeft;
				}
				else if(DriveIO.getInstance().getGearStatus() == GearStatus.SHIFTED_RIGHT){
					angle = angleRight;
				}
				DriveIO.getInstance().setTranslationVector(Vector.makePolar(angle, distance));
				initialized = true;
				System.out.println("Set Shifted Translation vector");
			}
			else if (state == 1) {
				DriveIO.getInstance().resetEncoders();
				
			}
		}
		
		if(DriveIO.getInstance().getTranslationVector().getMagnitude() == 999 && prevMagnitude != 999 && initialized){
			state++;
			initialized = false;
		}
		prevMagnitude = DriveIO.getInstance().getTranslationVector().getMagnitude();
		
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
