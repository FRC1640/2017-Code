package auton;

import drive.DriveIO;

public class GearStrafe implements AutonCommand {
	DriveIO driveIO;
	private String name;
	private boolean done = false;
	private boolean initialized = false;
	
	public GearStrafe(String name) {
		this.name = name;
	}

	@Override
	public void execute() {
		if (!initialized) {
			driveIO = DriveIO.getInstance();
			driveIO.setGearStrafe(true);
			initialized = true;
		}
		if (!driveIO.getGearStrafe()) {
			done = true;
		}
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
