package auton;


import utilities.Vector;
import drive.DriveIO;

public class Drive implements AutonCommand{
	private String name;
	private double distance, angle;
	private boolean initialized = false, done;
	private double prevMagnitude;
	
	public Drive(double distance, double angle, double speed, String name){
		this.distance = distance;
		this.angle = angle;
		this.name = name;
	}

	@Override
	public void execute() {
		if(!initialized){
			DriveIO.getInstance().resetEncoders();
			DriveIO.getInstance().setTranslationVector(Vector.makePolar(angle, distance));
			initialized = true;
			System.out.println("Set Translation vector");
		}
		if(DriveIO.getInstance().getTranslationVector().getMagnitude() == 999 && prevMagnitude != 999){
			done = true;
			System.out.println("done drive");
		}
//		System.out.println("prevMag: " + prevMagnitude);
		prevMagnitude = DriveIO.getInstance().getTranslationVector().getMagnitude();
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
