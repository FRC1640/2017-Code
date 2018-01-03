package auton;


import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Vector;
import constants.Constants;
import constants.ControllerConstants;
import drive.DriveIO;

public class DriveSpeed implements AutonCommand{
	private String name;
	private double distance, angle, speed;
	private boolean initialized = false, done;
	private double prevMagnitude;
	private Vector moveVector;
	private final long TIME_OUT = 10000;
	private long startTime;
	
	public DriveSpeed(double distance, double angle, double speed, String name){
		this.distance = distance;
		this.angle = angle;
		this.name = name;
		this.speed = speed;
		moveVector = Vector.makePolar(angle, speed);
	}

	@Override
	public void execute() {
		if(!initialized){
			System.out.println("Initialized Drive Speed");
			DriveIO.getInstance().resetEncoders();
			initialized = true;
			startTime = (long) (System.nanoTime() / 1000000.0);
		}
		if(!done){
			ControlInputs.getDriver().setAxis(ControllerConstants.SIDE_DRIVER, moveVector.getX());
			ControlInputs.getDriver().setAxis(ControllerConstants.FOWARD_DRIVER, -moveVector.getY());
		}
		if(Math.abs(selectEncoder()) > distance || System.nanoTime() / 1000000.0 - startTime > TIME_OUT){
			done = true;
			System.out.println("Done");
			ControlInputs.getDriver().setAxis(ControllerConstants.SIDE_DRIVER, 0);
			ControlInputs.getDriver().setAxis(ControllerConstants.FOWARD_DRIVER, 0);
		}
	}
	
	private double selectEncoder(){
		/*double sum = 0;
		double count = 0;
		for(Pivot p : DriveIO.getInstance().getPivots()){
			sum += p.getInches();
			if(p.getInches() != 0){
				count++;
			}
			System.out.println("Inches: " + p.getInches() + " id: " + p.getID());
		}
		System.out.println("Encoders: " + sum / count + " sum " + sum + " count "+ count);
		return sum / count;*/
//		System.out.println("Inches: " + DriveIO.getInstance().getPivots()[1].getInches());

		return DriveIO.getInstance().getPivots()[Constants.MASTER_PIVOT].getInches();
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
