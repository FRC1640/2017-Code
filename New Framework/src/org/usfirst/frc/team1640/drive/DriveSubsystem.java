package org.usfirst.frc.team1640.drive;

import org.usfirst.frc.team1640.utilities.Subsystem;

public class DriveSubsystem extends Subsystem {
	
	boolean isInit = false;

	@Override
	public void init() {
		
		isInit = true;
	}
	
	@Override
	public void update() {
		if (!isInit) {
			init();
		}
	}

}
