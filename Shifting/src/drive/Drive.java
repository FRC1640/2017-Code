package drive;

import utilities.Subsystem;


public class Drive extends Subsystem{ // manages the drive thread
	private DriveCore driveCore = DriveCore.getInstance();
	
	public void init(){	}
	
	public void update() {
		driveCore.update(); // each iteration of the thread, update the drive code
	}
}
