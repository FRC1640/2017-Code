package drive;

import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;

public abstract class MoveDecorator extends DriveControlDecorator{
	private PIDController PID;
	private PIDSourceDouble delta;
	private PIDOutputDouble speed;
	private DriveIO driveIO = DriveIO.getInstance();
	private double target, p, i, d, f, buffer, prevTarget;
	private boolean initialized, isDone;
	
	public enum Axis{X1, Y1, X2};
	private Axis axis;
	
	public MoveDecorator(DriveControl driveControl, Axis axis, double p, double i, double d, double f, double buffer) {
		super(driveControl);
		
		delta = new PIDSourceDouble();
		speed = new PIDOutputDouble();
		PID = new PIDController(p, i, d, f, delta, speed, 0.02); 
		PID.setOutputRange(-0.4, 0.4);
		PID.setSetpoint(0.0);
		
		this.p = p;
		this.i = i;
		this.d = d;
		this.f = f;
		this.buffer = buffer;
		this.axis = axis;
	}

	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		double speedValue = 0;
		if(target != prevTarget){
			PID.reset();
			initialized = false;
		}
		
		if(!initialized && target != 0){ 
			PID.enable();
			initialized = true;
		}
		else{
			double currentAngle = driveIO.getYaw();
			delta.setValue(Utilities.shortestAngleBetween(currentAngle, target));
			isDone = Math.abs(Utilities.shortestAngleBetween(currentAngle, target)) < buffer; 
			speedValue = isDone ? 0 : speed.getValue();
			
			if(isDone){
				initialized = false;
				driveIO.setTurnAngle(0);
				PID.disable();
			}
		}
		prevTarget = target;
		
		switch(axis){
			case X1:
				super.execute(speedValue, y1, x2, y2);
				break;	
			case Y1:
				super.execute(x1, speedValue, x2, y2);
				break;
			case X2:
				super.execute(x1, y1, speedValue, y2);
				break;
		}
	}
	
	protected void setTarget(double newTarget){
		target = newTarget;
	}

}
