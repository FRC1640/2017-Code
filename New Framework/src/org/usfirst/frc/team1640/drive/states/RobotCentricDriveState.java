package org.usfirst.frc.team1640.drive.states;

import org.usfirst.frc.team1640.controller.ControllerContext;
import org.usfirst.frc.team1640.drive.DriveContext;
import org.usfirst.frc.team1640.drive.DriveSetting;
import org.usfirst.frc.team1640.drive.OcelotDriveStrategy;
import org.usfirst.frc.team1640.drive.RobotCentricDriveStrategy;


public class RobotCentricDriveState extends DriveState {
	ControllerContext controllerContext;
	DriveSetting driveSetting;
	OcelotDriveStrategy driveStrategy;
	
	public RobotCentricDriveState(DriveContext driveContext) {
		driveSetting = driveContext.getDriveSetting();
		controllerContext = driveContext.getControllerContext();
		
		driveStrategy = new RobotCentricDriveStrategy(driveSetting, controllerContext);
	}
	
	@Override
	public void execute() {
		driveStrategy.invoke();
	}
	
	@Override
	public void leave() {
		driveSetting.stopAllPivots();
	}

}
