package drive;

import utilities.Movement;
import utilities.Vector;
import constants.Constants;

public class Move extends DriveControlDecorator{
	
	/*
	 * Variables To Be Evaluated
	 * 
	 * prevLinearTotalDistanceVector[] is instantiated and reset properly, but never used
	 * rotationalTotalDistance[] is instantiated and accessed, but isn't ever changed
	 * prevRotationalTotalDistance[] is instantiated and reset to rotationalTotalDistance, but never used
	 */
	
	private DriveIO driveIO = DriveIO.getInstance();
	private Movement m;
	
	private Pivot[] pivots;
	
	private Vector linearGoalDistanceVector;
	private double rotationalGoalDistance;
	
	private double [] startPositions = new double[4];
	private double [] prevNetPositions = new double[4];
	
	private Vector [] radiusVectors = new Vector[4];
	
	private double [] rotationalNetDistances = new double[4];
	private Vector [] linearNetDistanceVectors = new Vector[4];
	
	private Vector [] linearTotalDistanceVector = new Vector[4];
	private Vector [] prevLinearTotalDistanceVector = new Vector[4];
	private double [] rotationalTotalDistance = new double[4];
	private double [] prevRotationalTotalDistance = new double[4];
	
	private Vector linearDistanceVector;
	private Vector prevLinearDistanceVector;
	private double rotationalDistance;
	private double prevRotationalDistance;
	
	private long prevTime;
	
	private final double maxOutput = 0.4;

	public Move(DriveControl driveControl) {
		super(driveControl);
		
		m = driveIO.getMovement();
		pivots = driveIO.getPivots();
		
		linearGoalDistanceVector = m.getVector()
				.divide(Constants.DRIVE_WHEEL_CIRCUMFERENCE)
				.multiply(Constants.DRIVE_ENCODER_COUNTS_PER_ROTATION);
		rotationalGoalDistance = 
				Math.toRadians(m.getAngle())
				*Constants.ROBOT_RADIUS
				/Constants.DRIVE_WHEEL_CIRCUMFERENCE
				*Constants.DRIVE_ENCODER_COUNTS_PER_ROTATION;
		
		double l = Constants.ROBOT_LENGTH, w = Constants.ROBOT_WIDTH, r = Constants.ROBOT_RADIUS;
		
		radiusVectors[0] = Vector.makePolar(Math.toDegrees(Math.atan2(l, -w)), r);
		radiusVectors[1] = Vector.makePolar(Math.toDegrees(Math.atan2(l, w)), r);
		radiusVectors[2] = Vector.makePolar(Math.toDegrees(Math.atan2(-l, w)), r);
		radiusVectors[3] = Vector.makePolar(Math.toDegrees(Math.atan2(-l, -w)), r);
		
		for (int i = 0; i < startPositions.length; i++) {
			startPositions[i] = pivots[i].getPosition();
			prevNetPositions[i] = 0;
			
			rotationalNetDistances[i] = 0;
			linearNetDistanceVectors[i] = new Vector();
			
			linearTotalDistanceVector[i] = new Vector();
			prevLinearTotalDistanceVector[i] = new Vector();
			rotationalTotalDistance[i] = 0;
			prevRotationalTotalDistance[i] = 0;
		}
		
		linearDistanceVector = new Vector();
		prevLinearDistanceVector = new Vector();
		rotationalDistance = 0;
		prevRotationalDistance = 0;
		
		prevTime = System.nanoTime();
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2) {
		for (int i = 0; i < 4; i++) {
			
			double netPosition = pivots[i].getPosition()-startPositions[i];
			double deltaPosition = netPosition-prevNetPositions[i];
			
			linearNetDistanceVectors[i] = linearNetDistanceVectors[i].add(Vector.makePolar(pivots[i].getAngle(), deltaPosition));
			rotationalNetDistances[i] = netPosition - linearNetDistanceVectors[i].getMagnitude();
			
			linearTotalDistanceVector[i] = radiusVectors[i]
					.add(linearNetDistanceVectors[i]
							.subtract(radiusVectors[i]
									.rotate(Math.toDegrees(
											rotationalNetDistances[i]/Constants.ROBOT_RADIUS))));
			
			prevNetPositions[i] = netPosition; //
		}
		
		linearDistanceVector = linearTotalDistanceVector[0];
		rotationalDistance = rotationalTotalDistance[0];
		
		Vector deltaV = linearDistanceVector.subtract(prevLinearDistanceVector);
		double deltaR = rotationalDistance - prevRotationalDistance;
		long deltaT = System.nanoTime()-prevTime;
		
		Vector a = ((deltaV.divide(deltaT)).divide(pivots[0].getVelocity())).multiply(maxOutput);
		double b = ((deltaR/deltaT)/pivots[0].getVelocity())*maxOutput;
		
		double nx1, ny1, nx2;
		
		if (linearDistanceVector.getMagnitude() < linearGoalDistanceVector.getMagnitude()) {
			nx1 = a.getX();
			ny1 = a.getY();
		}
		else {
			nx1 = 0;
			ny1 = 0;
		}
		if (rotationalDistance < rotationalGoalDistance) {
			nx2 = b;
		}
		else {
			nx2 = 0.0;
		}
		
		super.execute(nx1, ny1, nx2, 0.0);
		
		prevLinearDistanceVector = linearDistanceVector.clone();//
		prevRotationalDistance = rotationalDistance;//
		prevLinearTotalDistanceVector = linearTotalDistanceVector.clone();
		prevRotationalTotalDistance = rotationalTotalDistance;
		prevTime = System.nanoTime();
	}
	
//	private Vector calcLinearTotalVector() {
//		Vector v = new Vector();
//		for (int i = 0; i < 4; i ++) {
//			v = v.add(linearNetDistanceVectors[i].divide(4.0));
//		}
//		
//		return v;
//	}
//	
//	private double calcRotationalTotalDistance() {
//		return rotationalNetDistances[0];
//	}
}
