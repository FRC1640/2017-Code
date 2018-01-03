package auton;

import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import drive.DriveIO;
import edu.wpi.first.wpilibj.PIDController;

public class ManualTurnTimeOut implements AutonCommand{
	private String name;
	public enum TurnType {RELATIVE, ABSOLUTE};
	private TurnType type;
	private double angle, target;
	private boolean initialized = false, done = false;
	private DriveIO driveIO;// = DriveIO.getInstance();

	private PIDController PID;
	private PIDSourceDouble delta;
	private PIDOutputDouble speed;
	
	private double BUFFER = 1, SPEED = 0.3, DONE_ITERATIONS = 5;
	private double p = 0.01, i = 0.000001, d = 0;
	private double doneCount;
	private boolean firstIteration = true;
	
	private long startTime;
	private double timeOut;
	
	public ManualTurnTimeOut(double angle, TurnType turnType, double timeOut, String name){
		this.angle = angle;
		this.type = turnType;
		this.name = name;
		this.timeOut = timeOut;
		if(turnType == TurnType.ABSOLUTE)
			target = angle;
		else 
			target = Utilities.relativeToAbsolute(DriveIO.getInstance().getYaw(), angle);

		delta = new PIDSourceDouble();
		speed = new PIDOutputDouble();
		PID = new PIDController(p, i, d, 0, delta, speed, 0.02); 
		PID.setOutputRange(-0.4, 0.4);
		PID.reset();
		PID.enable();
	}
	
	@Override
	public void execute() {
		if (firstIteration) {
			startTime = System.nanoTime()/1000000;
			firstIteration = false;
		}
		driveIO = DriveIO.getInstance();
		double turnSpeed = 0;
		double angle = Utilities.shortestAngleBetween(target, driveIO.getYaw());
		delta.setValue(angle);
		if (Math.abs(angle) < BUFFER) {
			doneCount++;
			System.out.println("Almost done");
			if(doneCount > DONE_ITERATIONS){
				turnSpeed = 0;
				PID.disable();
				System.out.println("Done. Gyro: " + driveIO.getYaw());
				done = true;
			}
		}
		else{
			if (System.nanoTime()/1000000 - startTime > timeOut) {
				turnSpeed = 0;
				PID.disable();
				System.out.println("Done timeout. Gyro: " + driveIO.getYaw());
				done = true;
			}
			turnSpeed = -speed.getValue();
			doneCount = 0;
		}
		ControlInputs.getDriver().setAxis(Axis.X2, turnSpeed);
	}

	@Override
	public boolean isRunning() {
		return !done;
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
		initialized = false;
		done = false;
	}

}
