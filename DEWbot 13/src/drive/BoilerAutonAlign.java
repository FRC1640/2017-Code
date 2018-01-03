package drive;

import java.util.ArrayList;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.ControlInputs;
import utilities.Utilities;
import vision.VisionData;
import vision.VisionTarget;
import constants.ControllerConstants;

public class BoilerAutonAlign extends DriveControlDecorator{
	private double BOILER_AREA_BUFFER = 100, BOILER_HEIGHT_BUFFER = 100, BOILER_X_BUFFER = 100, DEFAULT_TARGET = 320/2;// - 50;
	private double SHOOTER_OFFSET = 4;
	private double SHOOTER_OFFSET_CLOSE = 2/*-12*/, SHOOTER_OFFSET_FAR = 0; //positive offset turns more to camera
	private double fieldOfView = 47, pictureWidth = 320;
	private double prevTarget;
	private DriveIO driveIO = DriveIO.getInstance();
	private boolean prevDone;
	private long startTime;
	private boolean prevClose, prevFar, done;
	private int i;
	private double angleToTurn;
	
	public BoilerAutonAlign(DriveControl driveControl) {
		super(driveControl);
		
//		driveIO.setTurnAngle(getAngleToTurn());
		driveIO.setAligning(true);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		double target = DEFAULT_TARGET;//VisionFileIO.getTarget(TargetType.BOILER);	
		
		if(target != prevTarget && !done) {
			startTime = (long) (System.nanoTime() / 1000000);
			boolean close = ControlInputs.getDriver().getButton(ControllerConstants.SHOOT_CLOSE_DRIVER);
			driveIO.setTurnAngle(getAngleToTurn(close));
			driveIO.setAligning(true);
			System.out.println("Boiler Turn: " + getAngleToTurn(close) + " Close? " + close);
			angleToTurn = getAngleToTurn(close);
		}
		if(!done && i > 20 && angleToTurn == 0){
			driveIO.setAligning(false);
			done = true;
			System.out.println("No boiler targets");
		}
		prevTarget = target;
		
		if(driveIO.getTurnAngle() == 999 && angleToTurn != 0){
			if(!prevDone){
				System.out.println("Boiler Aim done");
				System.out.println("Boiler aim time: " + (System.nanoTime() / 1000000 - startTime));
			}
			driveIO.setAligning(false);
			done = true;
		}
		prevDone = driveIO.getTurnAngle() == 999; 
		i++;
		prevClose = ControlInputs.getDriver().getButton(ControllerConstants.SHOOT_CLOSE_DRIVER);
		prevFar = ControlInputs.getDriver().getButton(ControllerConstants.SHOOT_FAR_DRIVER);
		
		super.execute(x1, y1, x2, y2);
	}
	
	private double getAngleToTurn(boolean close){
		ArrayList<VisionTarget> targets = VisionData.getInstance().getBoilerTargets();
		double current = 999;
		
		System.out.println("Targets size: " + targets.size());
		for(VisionTarget t1 : targets){
			for(VisionTarget t2 : targets){
				if(t1.getCenterX() - (t2.getCenterX()) < BOILER_X_BUFFER){
					if(t1.getArea() - (t2.getArea() * 2) < BOILER_AREA_BUFFER){
						if(t1.getHeight() - t2.getHeight() * 2 < BOILER_HEIGHT_BUFFER){
							current = t1.getCenterX();
						}
					}
				}
			}
		}
		
		if(current != 999){
			double target = DEFAULT_TARGET; //VisionFileIO.getTarget(TargetType.BOILER);
			target = target != 999 ? target : DEFAULT_TARGET;
			double rel = -Utilities.calculateAngle(current, target, pictureWidth, fieldOfView);
			System.out.println("Rel: " + rel);
			double offset = close ? SHOOTER_OFFSET_CLOSE : SHOOTER_OFFSET_FAR;
			if(Robot.getState() == State.AUTON && DriveIO.getInstance().getAlignOffset() != 0){
				offset = DriveIO.getInstance().getAlignOffset();
				System.out.println("Offset: " + offset);
			}
			System.out.println("current: " + driveIO.getYaw() + "returned value " + Utilities.relativeToAbsolute(driveIO.getYaw(), rel+offset));
			return Utilities.relativeToAbsolute(driveIO.getYaw(), rel) + offset;
			
		}
		else if(Robot.getState() == State.AUTON ){
			DriveIO.getInstance().setTurnAngle(999);
		}
		return 0;
	}
}
