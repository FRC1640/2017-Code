package auton;


import utilities.ControlInputs;
import utilities.GaussianCurve;
import utilities.Vector;
import constants.Constants;
import constants.ControllerConstants;
import drive.DriveIO;

public class DriveGaussian implements AutonCommand{
	private String name;
	private double distance, angle, maxSpeed;
	private boolean initialized = false, done;
	private Vector moveVector;
	private final long TIME_OUT = 50000;//3000;
	private long startTime;
	private GaussianCurve curve;
	
	public DriveGaussian(double distance, double angle, double maxSpeed, String name){
		this.distance = distance;
		this.angle = angle;
		this.name = name;
		this.maxSpeed = maxSpeed;
		moveVector = Vector.makePolar(angle, 1);
		curve = new GaussianCurve(3, 4, distance);
	}

	@Override
	public void execute() {
		if(!initialized){
			System.out.println("Initialized Drive Gaussian");
			DriveIO.getInstance().resetEncoders();
			initialized = true;
			startTime = (long) (System.nanoTime() / 1000000.0);
		}
		if(!done){
			double speed = maxSpeed * curve.getOutput(Math.abs(selectEncoder()));
			double direction = speed / Math.abs(speed);
			speed = (Math.abs(speed) < 0.23 ? 0.23 : speed) * direction;
			ControlInputs.getDriver().setAxis(ControllerConstants.SIDE_DRIVER, moveVector.getX() * speed);
			ControlInputs.getDriver().setAxis(ControllerConstants.FOWARD_DRIVER, -moveVector.getY() * speed);
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
		double count = 0;ds
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

		return DriveIO.getInstance().getPivots()[Constants.MASTER_PIVOT].getPosition();
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
