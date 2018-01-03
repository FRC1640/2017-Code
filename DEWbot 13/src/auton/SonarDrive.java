package auton;


import constants.ControllerConstants;
import drive.DriveIO;
import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Vector;

public class SonarDrive implements AutonCommand{
	private String name;
	private double distance;
	private boolean initialized = false, done, direction;
	private final double BUFFER = 7;//3;
	private double[] average = new double[10];
	private int iteration;
	private double speed = 0;
	private double offset = 0;//2;
	private long startTime;
	public enum Sonar {FRONT, LEFT, RIGHT};
	private Sonar sonar;
	private double angle;
	private final double TIME_OUT = 4000;
	
	public SonarDrive(double speed, double distance, double angle, Sonar sonar, String name){
		this.speed = speed;
		this.distance = distance;
		for(int i = 0; i < average.length; i++){
			average[i] = 0;
		}
		this.name = name;
		this.angle = angle;
		this.sonar = sonar;
	}

	@Override
	public void execute() {
		if(!initialized){
			DriveIO.getInstance().resetEncoders();
			direction = getInches() > distance;
			initialized = iteration % (average.length - 1) == 0 && iteration != 0;
			startTime = System.nanoTime() / 1000000;
			System.out.println("Sonar drive: " + sonar + " direction: " + direction + " sonar: " + getInches());
		}
		else{
			Vector speedVector = Vector.makeCartesian(0, 0); 
			if (System.nanoTime() / 1000000 - startTime > TIME_OUT) {
				done = true;
			}
			if(!done){
				double mSpeed = speed;
				mSpeed = direction ? -mSpeed : mSpeed;
				speedVector = Vector.makePolar(angle, mSpeed);
			}
			if(Math.abs(distance - getInches()) < BUFFER){
				done = true;
				speedVector = Vector.makePolar(angle, 0);
			}
			ControlInputs.getDriver().setAxis(ControllerConstants.FOWARD_DRIVER, speedVector.getY());
			ControlInputs.getDriver().setAxis(ControllerConstants.SIDE_DRIVER, speedVector.getX());
		}
	}
	
	private int getInches(){
		double sonarInches = 0;
		switch(sonar){
			case FRONT: {sonarInches = DriveIO.getInstance().getFrontSonarInches(); break;}
			case LEFT: {sonarInches = DriveIO.getInstance().getLeftSonarInches(); break;}
			case RIGHT: {sonarInches = DriveIO.getInstance().getRightSonarInches(); break;}
		}
		average[iteration] = sonarInches;
		iteration++;
		if(iteration == average.length) iteration = 0;
		int avg = 0;
		for(int i = 0; i < average.length; i++){
			avg += average[i];
		}
		return avg / average.length;
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
