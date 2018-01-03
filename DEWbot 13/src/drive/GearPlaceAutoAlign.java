package drive;

import java.util.ArrayList;

import utilities.Movement;
import utilities.Utilities;
import utilities.Vector;
import vision.VisionData;
import vision.VisionTarget;

public class GearPlaceAutoAlign extends DriveControlDecorator {
	private final double GEAR_PLACE_MIN_X_BUFFER = 0, GEAR_PLACE_MIN_Y = 0, GEAR_PLACE_MIN_AREA = 50;
	private final double camVerticalFOV = 34.5, camHorizontalFOV = 45.3, targetHeight = 1.245,
			camHeight = 1.4, camRightDistance = -1.3/*-0.4792*/, camForwardDistance = 0.083,
			camResolutionWidth = 640, camResolutionHeight = 480, camVerticalAngle = -3.5, placementOffset = 0;
	private DriveIO driveIO = DriveIO.getInstance();
	private Vector vectorMove;
	private boolean moving;
	
	private double prevTarget;

	public GearPlaceAutoAlign(DriveControl driveControl) {
		super(driveControl);
		
		driveIO.setAligning(true);
		
		ArrayList<VisionTarget> targets = VisionData.getInstance().getGearTargets();
		double angle = 0;
		Vector vector = Vector.makeCartesian(0, 0);
		double centerX1 = -999;
		double centerY1 = -999;
		double distance1 = -999;
		double angle1 = -999;
		double vx1 = -999;
		double vy1 = -999;
		double distance2 = -999;
		double angle2 = -999;
		double vx2 = -999;
		double vy2 = -999;
		int t1Index = 0;
		int t2Index = 0;
		
		for (int i = 0; i < targets.size(); i++) {
			if (targets.get(i).getArea() < GEAR_PLACE_MIN_AREA) continue;
			for (int j = 0; j < targets.size(); j++) {
				if (targets.get(j).getArea() < GEAR_PLACE_MIN_AREA) continue;
				VisionTarget t1 = targets.get(i);
				VisionTarget t2 = targets.get(j);
				if (Math.abs(t1.getCenterX() - t2.getCenterX()) > GEAR_PLACE_MIN_X_BUFFER) {
					if (Math.min(t1.getCenterY(), t2.getCenterY()) > GEAR_PLACE_MIN_Y) {
						
						t1Index = i;
						t2Index = j;
						
						double x1, y1, x2, y2;
						
						if (t1.getCenterX() < t2.getCenterX()) {
							x1 = t1.getCenterX();
							y1 = t1.getCenterY();
							x2 = t2.getCenterX();
							y2 = t2.getCenterY();
						}
						else {
							x1 = t2.getCenterX();
							y1 = t2.getCenterY();
							x2 = t1.getCenterX();
							y2 = t1.getCenterY();
						}
						centerX1 = x1;
						centerY1 = y1;
						Movement m = calcMovement(x1, y1, x2, y2);
						Vector d3 = m.getVector();
						
						distance1 = calcDistance(y1);
						angle1 = calcAngle(x1-camResolutionWidth/2, camResolutionWidth, camHorizontalFOV) + 90;
						vx1 = Vector.makePolar(angle1, distance1).getX();
						vy1 = Vector.makePolar(angle1, distance1).getY();
						distance2 = calcDistance(y2);
						angle2 = calcAngle(x2-camResolutionWidth/2, camResolutionWidth, camHorizontalFOV) + 90;
						vx2 = Vector.makePolar(angle2, distance2).getX();
						vy2 = Vector.makePolar(angle2, distance2).getY();
						
						angle = m.getAngle();
						
						vector = d3.add(Vector.makeCartesian(camRightDistance, camForwardDistance - placementOffset));
						vector = Vector.makeCartesian(-vector.getX(), vector.getY());
						vectorMove = vector.multiply(12);
					}
				}
			}
		}
		
//		angle = 180;
//		distance = Vector.makeCartesian(-32, 64);
//		
//		System.out.println("Turn: " + Utilities.relativeToAbsolute(DriveIO.getInstance().getYaw(), angle));
//		driveIO.setTurnAngle(Utilities.relativeToAbsolute(DriveIO.getInstance().getYaw(), angle));
		driveIO.setTranslationVector(vectorMove);
		moving = true;
//		driveIO.setTranslationVector(vector.multiply(12));
//		driveIO.setAligning(true);
		
		System.out.println("Center X1: " + centerX1);
		System.out.println("Center Y1: " + centerY1);
		System.out.println("Distance 1: " + distance1);
		System.out.println("Angle 1: " + angle1);
		System.out.println("V1: (" + vx1 + ", " + vy1 + ")");
		System.out.println("Distance 2: " + distance2);
		System.out.println("Angle 2: " + angle2);
		System.out.println("V2: (" + vx2 + ", " + vy2 + ")");
		System.out.println("Vector: Angle: " + vector.getAngle() + " Mag: " + vector.getMagnitude());
		System.out.println("V3: (" + vector.getX() + ", " + vector.getY() + ")");
		System.out.println("Angle to Turn: " + angle);
		System.out.println("T1: " + t1Index);
		System.out.println("T2: " + t2Index);
	}
	
	@Override
	public void execute(double x1, double x2, double y1, double y2) {
		
//		double target = VisionFileIO.getTarget(TargetType.BOILER);
//		if(target != prevTarget){
//			
//		}
//		prevTarget = target;
		
//		if(driveIO.getTurnAngle() == 999){
//			driveIO.setTranslationVector(vectorMove);
//			moving = true;
//		}
		if(moving && driveIO.getTranslationVector().getMagnitude() == 999){
			driveIO.setAligning(false);
		}
		
		super.execute(x1, x2, y1, y2);
	}
	
	public double calcDistance(double y) {
		double phi = Math.toRadians(calcAngle(camResolutionHeight/2-y, camResolutionHeight, camVerticalFOV));
		return (targetHeight - camHeight)/Math.tan(phi + Math.toRadians(camVerticalAngle));
	}
	
	public double calcAngle(double pixels, double resolution, double fov) {
		double a = Math.toRadians(fov);
		double focalLength = resolution/(2*Math.tan(a/2));
		return Math.toDegrees(Math.atan2(pixels, focalLength));
	}
	
	public Movement calcMovement(double x1, double y1, double x2, double y2) {
		Vector d3;
		double angle;
		
		double ang1 = calcAngle(x1-camResolutionWidth/2, camResolutionWidth, camHorizontalFOV) + 90;
		double mag1 = calcDistance(y1);
		Vector d1 = Vector.makePolar(ang1, mag1);
		
		double ang2 = calcAngle(x2-camResolutionWidth/2, camResolutionWidth, camHorizontalFOV) + 90;
		double mag2 = calcDistance(y2);
		Vector d2 = Vector.makePolar(ang2, mag2);
		
		Vector d12 = d1.subtract(d2);
		angle = Vector.constrainAngle(d12.getAngle());
		System.out.println("Vector Between: " + d12.getMagnitude());
		
		d3 = d2.add(d12.divide(2));
		
		return new Movement(d3, angle);
	}
	
//	private Vector calcDistance(double x, double y) {
//		//TODO: Compress into a single statement once tested
//		
//		double alpha = Math.toDegrees(Math.atan2(x - pictureWidth/2, pictureWidth/2*Math.tan(Math.toRadians(fieldOfViewH)/2)));
//		double phi = Math.toDegrees(Math.atan2(y - pictureHeight/2, pictureWidth/2*Math.tan(Math.toRadians(fieldOfViewV)/2)));
//		double magnitude = Math.abs((targetHeight - cameraHeight) / (Math.tan(Math.toRadians((phi + cameraAngleV)))));
//		
//		Vector d = Vector.makePolar(alpha, magnitude);
//		d.add(Vector.makeCartesian(cameraSideDistance, cameraForwardDistance));
//		return d;
//	}
//	
	/**
	 * Gives the "Real" Encoder Tick Distance that the Robot has Gone (Net) Given the delta-tick and angle of each
	 * Depends on CurrentMotionVector
	 * @param angle_ticks - double[4][2] Array Containing [][0] angles and [][1] ticks
	 * @param ms - Time (milliseconds) over which the delta-ticks have been counted
	 * @return "Real" Ticks From Summmative Velocity
	 * EXPERIMENTAL!
	 */
//	private static double compensatedEncoderTicks(double[][] angle_ticks, double ms)
//	{
//		double actualTicks = angle_ticks[0][1];
//		
//		//This code is pretty wasteful, but, it IS just a test
//		Vector encoderDistanceVector = currentMotionVector(angle_ticks);
//		Vector encoderVelocityVector = encoderDistanceVector.multiply(1 / (ms / 1000));
//		
//		actualTicks = encoderDistanceVector.getMagnitude();
//		
//		return actualTicks;
//	}
	
	/**
	 * Takes an double [4][2] Array of Angle Values in [][0] and Velocity in [][1] and Creates a Vector Average
	 * @param angle_encoder - Double ([4][2]) Array of Current Angles and Velocities (Ticks/Second)
	 * @return Average Vector of These, Current Motion of Robot Swerve (Ticks/Second)
	 * EXPERIMENTAL!
	 */
//	private static Vector currentMotionVector(double[][] angle_encoder)
//	{
//		//Does Angle Need to Be Adjusted to fit a Certain Range, or is a full 360 range functional
//		//This assumes full 360 range for polar vectors (/4?)
//		Vector returnVector = Vector.makePolar(0, 0);
//		for(int i = 0; i < angle_encoder.length; i ++)
//		{
//			returnVector = returnVector.add(Vector.makePolar(angle_encoder[i][0], angle_encoder[i][1]));
//			System.out.println(returnVector.getMagnitude());
//		}
//		returnVector = returnVector.multiply(1 / ((double) angle_encoder.length));
//		return returnVector;
//	}
//	
//	/* Here is the code that has been removed from VisioData. It can be used in the 2 gear decorators when they are created
//	 * 
//	 * 	public VisionTarget[] getGearPlaceTargets(){
//		for(VisionTarget t1 : targets){
//			for(VisionTarget t2 : targets){
//				if(t1.getCenterX() - (t2.getCenterX() * 2) < GEAR_X_BUFFER){
//					if(t1.getArea() - (t2.getArea() * 2) < GEAR_AREA_BUFFER){
//						if(t1.getHeight() - t2.getHeight() * 2 < GEAR_HEIGHT_BUFFER){
//							VisionTarget[] gearTargets = {t1, t2};
//							return gearTargets;
//						}
//					}
//				}
//			}
//		}
//		return null;		
//	}
//	
//	public VisionTarget getGearLoadTarget(){
//		double maxArea = 0;
//		VisionTarget target = null;
//		for(VisionTarget t : targets){
//			if(t.getArea() > maxArea){
//				target = t;
//				maxArea = t.getArea();
//			}
//		}
//		return target;
//	}*/

}
