package auton;

import constants.ControllerConstants;
import drive.DriveIO;
import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Vector;

public class TurnWheels implements AutonCommand{ //command used to delay for certain time
	private long startTime;
	private boolean firstIteration = true;
	private String name;
	private boolean done;
	private double angle;
	private Vector angleVector;
	private final long DELAY = 200;
	
	public TurnWheels(double angle, String name){
		//record inputs
		this.angle = angle;
		this.name = name;
		
		angleVector = Vector.makePolar(angle + 90, 1);
	}

	@Override
	public void execute() {
		if(firstIteration){
			firstIteration = false;
			startTime = (long) (System.nanoTime() / 1000000.0);
			DriveIO.getInstance().setTurnWheel(true);
			ControlInputs.getDriver().setAxis(ControllerConstants.SIDE_DRIVER, angleVector.getX());
			ControlInputs.getDriver().setAxis(ControllerConstants.FOWARD_DRIVER, angleVector.getY());
		}
		if(System.nanoTime() / 1000000 - startTime > DELAY){
			DriveIO.getInstance().setTurnWheel(false);
			ControlInputs.getDriver().setAxis(ControllerConstants.SIDE_DRIVER, 0);
			ControlInputs.getDriver().setAxis(ControllerConstants.FOWARD_DRIVER, 0);
			done = true;
		}
	}

	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() { //is the command initialized
		return true;
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}
	
	@Override 
	public void reset() {
		firstIteration = true;
	}
}
