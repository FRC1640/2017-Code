package drive;

import java.util.ArrayList;

import constants.Constants;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import utilities.Vector;
import vision.VisionData;
import vision.VisionFileIO;
import vision.VisionFileIO.TargetType;
import vision.VisionTarget;
import edu.wpi.first.wpilibj.PIDController;


public class SimpleGearAlign extends DriveControlDecorator{
//	private static final double GEAR_PLACE_MIN_X_BUFFER = 1000, CENTER_X_TARGET = 47, STRAFE_SPEED = 0.2, BUFFER = 75;
//	private static final double SMALL_BUFFER = 15, BIG_BUFFER = 75;
//	private double buffer;
//	private PIDController pid;
//	private PIDSourceDouble strafeDistance;
//	private PIDOutputDouble strafeSpeed;
//	private double p = 0.0025, i = 0, d = 0.01, f = 0;
//	private boolean done;
	private final double offset = 10; //camera offset - spring offset
	private DriveIO driveIO;
	private double prevTarget;

	public SimpleGearAlign(DriveControl driveControl){
		super(driveControl);
		driveIO = DriveIO.getInstance();
		
//		strafeDistance = new PIDSourceDouble();
//		strafeSpeed = new PIDOutputDouble();
//		pid = new PIDController(p, i, d, f, strafeDistance, strafeSpeed, 0.02);
//		pid.setOutputRange(-0.5, 0.5);
//		pid.setSetpoint(CENTER_X_TARGET);
//		pid.enable();		
		driveIO.setAligning(true);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){		
		double target = 999;//VisionFileIO.getTarget(TargetType.GEAR_PLACE);
		if(target != prevTarget){
			double  distance = -getStrafeDistance() + offset;
			double angle = distance < 0 ? 180 : 0;
			driveIO.setTranslationVector(Vector.makeCartesian(distance , 0));

//			driveIO.setTranslationVector(Vector.makePolar(angle, Math.abs(distance)));
			System.out.println("BiggerX: " + getBiggerX() + " Angle: " + angle + " Distance (inches): " + distance + " getStrafeDistance(): " + getStrafeDistance() + " Sonar: " + driveIO.getFrontSonarInches());
			driveIO.setAligning(true);
		}
		prevTarget = target;
		
		if(driveIO.getTranslationVector().getMagnitude() == 999){
			driveIO.setAligning(false);
		}
		
		super.execute(x1, y1, x2, y2);
	}
	
	
	private double getStrafeDistance(){
		double angleToTurn = Utilities.calculateAngle(getBiggerX(), Constants.GEAR_PIC_WIDTH/2, Constants.GEAR_PIC_WIDTH, Constants.GEAR_HORIZONTAL_FOV);
		return Math.tan(Math.toRadians(angleToTurn)) * driveIO.getFrontSonarInches();
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
	
//	double speed = 0;
//	ArrayList<VisionTarget> targets = VisionData.getInstance().getAllTargets();
//	if(targets != null && !targets.isEmpty()){
//		if(!done){
//			double angleToTurn = Utilities.calculateAngle(getBiggerX(), VisionFileIO.getTarget(TargetType.GEAR_PLACE), Constants.GEAR_PIC_WIDTH, Constants.GEAR_HORIZONTAL_FOV);
//			double distanceToStrafe = Math.tan(Math.toRadians(angleToTurn)) * driveIO.getSonarInches();
//			strafeDistance.setValue(distanceToStrafe);
//			if(Math.abs(distanceToStrafe) < BUFFER){
//				done = true;
//				driveIO.setAligning(false);
//			}
//			else{
//				speed = strafeSpeed.getValue();
//			}
//		}
//	}
//	else {
//		System.out.println("Targets Empty");
//	}
//	super.execute(speed, y1, x1, y2);
	
}
