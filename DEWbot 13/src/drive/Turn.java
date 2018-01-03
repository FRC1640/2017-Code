package drive;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;


public class Turn extends DriveControlDecorator {
	private DriveIO driveIO = DriveIO.getInstance();
	
	private PIDController PID;
	private PIDSourceDouble delta;
	private PIDOutputDouble speed;
	
	private double target = 998, prevTarget = 998, doneCount;
	private boolean initialized, isDone, close;
	
	private final double DONE_ITERATIONS = 3;
	
	//primes turning
//	private static double pClose = 0.01, iClose = 0.00125, dClose = 0.005, fClose = 0.0, bufferClose = 1/*2*/; //pClose = 0.035, iClose = 0.001, dClose = 0, fClose = 0.0, bufferClose = 1/*2*/;
//	private static double pFar = 0.02, iFar= 0, dFar = 0.03, fFar = 0.0, bufferFar = 3/*2*/;
	private static double pClose = 0.03, iClose = 0.0005, dClose = 0.0, fClose = 0.0, bufferClose = 1/*2*/; //pClose = 0.035, iClose = 0.001, dClose = 0, fClose = 0.0, bufferClose = 1/*2*/;
	private static double pFar = 0.015, iFar= 0.0004, dFar = 0.3, fFar = 0.0, bufferFar = 3/*2*/;
	private final double MIN_OUTPUT = 0.1;
	
	private final double CLOSE_THRESHOLD = 6;
	private double buffer;


	
	
	public Turn(DriveControl driveControl) {
		super(driveControl);
		
		prevTarget = 0;
		System.out.println("In constructor. Target: " + target);
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		
		target = driveIO.getTurnAngle();
		
		double turnSpeed = 0;
		if (target != prevTarget && target != 999) {
			
			delta = new PIDSourceDouble();
			speed = new PIDOutputDouble();
			double p, i, d;
			target = driveIO.getTurnAngle();
			double angle = Utilities.shortestAngleBetween(target, driveIO.getYaw());
			if(Math.abs(angle) < CLOSE_THRESHOLD){
				p = pClose;
				i = iClose;
				d = dClose;
				buffer = bufferClose;
				System.out.println("Chose close");
			}
			else{
				p = pFar;
				i = iFar;
				d = dFar;
				buffer = bufferFar;
				System.out.println("Chose far");
			}
			PID = new PIDController(p, i, d, 0, delta, speed, 0.02); 
			PID.setOutputRange(-0.4, 0.4);
			
			System.out.println("initializing again: " + target + " " + prevTarget);
//			PID.reset();
			initialized = true;
			System.out.println("Not initialzed. Target: " + target);
//			changeClose();
			PID.reset();
			PID.enable();
		}
		
		double angle = Utilities.shortestAngleBetween(target, driveIO.getYaw());
		if (initialized) {
			delta.setValue(angle);
//			changeClose();
			if (Math.abs(angle) < buffer) {
				doneCount++;
				if(doneCount > DONE_ITERATIONS){
					turnSpeed = 0;
					PID.disable();
					driveIO.setTurnAngle(999);
					initialized = false;
					target = 999;
					System.out.println("Done. Gyro: " + driveIO.getYaw());
				}
			}
			else{
//				if(speed.getValue() != 0 && Math.abs(speed.getValue()) < MIN_OUTPUT){
//					turnSpeed = MIN_OUTPUT * Math.abs(speed.getValue()) / speed.getValue();
//				}
//				else
					turnSpeed = -speed.getValue();
				doneCount = 0;
			}
//			System.out.println("Turn Speed: " + turnSpeed + " : " + PID.isEnabled());
		}
		else{
			turnSpeed = x2;
		}
		prevTarget = target;
		super.execute(x1, y1, turnSpeed, y2);
	}

}
