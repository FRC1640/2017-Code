package auton;


import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Vector;
import constants.ControllerConstants;
import drive.DriveIO;

public class DriveTime implements AutonCommand{
	private String name;
	private long time;
	private double angle, speed;
	private boolean initialized = false, done = false;
	private Vector moveVector;
	private long startTime;
	
	public DriveTime(long time, double angle, double speed, String name){
		this.time = time;
		this.angle = angle;
		this.name = name;
		this.speed = speed;
		moveVector = Vector.makePolar(angle, speed);
	}

	@Override
	public void execute() {
		if(!initialized){
			System.out.println("Initialized Drive Time");
			DriveIO.getInstance().resetEncoders();
			initialized = true;
			startTime = (long) (System.nanoTime() / 1000000.0);
		}
		if(!done){
			ControlInputs.getDriver().setAxis(ControllerConstants.SIDE_DRIVER, moveVector.getX());
			ControlInputs.getDriver().setAxis(ControllerConstants.FOWARD_DRIVER, -moveVector.getY());
		}
		if(System.nanoTime() / 1000000.0 - startTime > time){
			done = true;
			System.out.println("Drive Time Done");
			ControlInputs.getDriver().setAxis(ControllerConstants.SIDE_DRIVER, 0);
			ControlInputs.getDriver().setAxis(ControllerConstants.FOWARD_DRIVER, 0);
		}
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
	}

}
