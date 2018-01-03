package drive;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Vector;
import edu.wpi.first.wpilibj.PIDController;

public class MoveVector extends DriveControlDecorator {
	
	private DriveIO driveIO = DriveIO.getInstance();
	
	private PIDController PID;
	private PIDSourceDouble delta;
	private PIDOutputDouble speed;
	
	private Vector target, prevTarget;
	private boolean initialized, isDone;
	private long startTime;
	private final long MAX_TIME = 60000;
	
//	private static double p = 0.025, i = 0, d = 0.0, f = 0.0, buffer = 5.0;
//	private static double p = 0.023, i = 0.0001, d = 0.0, f = 0.0, buffer = 1.0;
	private static double pClose = 0.04, iClose = 0.0005, dClose = 0.0, fClose = 0.0, bufferClose = 0.1;
	private static double pFar = 0.025, iFar = 0, dFar = 0.0, fFar = 0.0, bufferFar = 5.0;
	private static double BUFFER = bufferClose;
	private final static double CLOSE_THRESHOLD = 5;

	public MoveVector(DriveControl driveControl) {
		super(driveControl);
		
		delta = new PIDSourceDouble();
		speed = new PIDOutputDouble();
		PID = new PIDController(pClose, iClose, dClose, fClose, delta, speed, 0.02); 
		PID.setOutputRange(-0.4, 0.4);
		PID.setSetpoint(0.0);
		
		target = driveIO.getTranslationVector();
		prevTarget = Vector.makeCartesian(0, 0);
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		target = driveIO.getTranslationVector();
		Vector speedVector = Vector.makeCartesian(x1, y1);
		
		double moveSpeed = 0;
		if ((target.getMagnitude() != prevTarget.getMagnitude() || target.getAngle() != prevTarget.getAngle())
				&& target.getMagnitude() != 999) {
			init();
		}
		
		if (initialized) {
			double currentPos = Math.abs(driveIO.getInches("fr"));
//			System.out.println("Current Pos: " + currentPos + " Target: " + target.getMagnitude() + " Buffer: " + BUFFER);
			delta.setValue(target.getMagnitude() - currentPos);
			if (Math.abs(target.getMagnitude() - currentPos) < BUFFER || System.nanoTime() / 1000000.0 - startTime> MAX_TIME) {
				moveSpeed = 0;
				driveIO.setTranslationVector(Vector.makePolar(0,999));
				PID.disable();
				initialized = false;
				System.out.println("done");
				
			}
			else moveSpeed = -speed.getValue();
			speedVector = Vector.makePolar(target.getAngle(), moveSpeed);
		}
		
//		System.out.println(speedVector.getX());
		prevTarget = target;
		super.execute(speedVector.getX(), speedVector.getY(), x2, y2);
	}
	
	private void init(){
		System.out.println("Reset Move Vector");

		driveIO.resetEncoders();
		initialized = true;
		startTime = (long) (System.nanoTime() / 1000000.0);
		
		if(target.getMagnitude() < CLOSE_THRESHOLD){
			PID.setPID(pClose, iClose, dFar);
			BUFFER = bufferClose;
			System.out.println("Move Vector Close");
		}
		else{
			PID.setPID(pFar, iFar, dFar);
			BUFFER = bufferFar;
			System.out.println("Move Vector Far");
		}
		PID.reset();
		PID.enable();
	}
	
	private double getPivot(){
		for(Pivot p : driveIO.getPivots()){
			if(p.getPosition() != 0)
				return p.getPosition();
		}
		return 0;
	}
}
