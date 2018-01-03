package org.usfirst.frc.team1640.drive;

import org.usfirst.frc.team1640.controller.ControllerContext;

public class DriveContext {
	private ControllerContext controllerContext;
	private DriveSetting driveSetting;
	
	public DriveContext(ControllerContext controllerContext, DriveSetting driveSetting) {
		this.controllerContext = controllerContext;
		this.driveSetting = driveSetting;
	}
	
	public ControllerContext getControllerContext() {
		return controllerContext;
	}
	
	public DriveSetting getDriveSetting() {
		return driveSetting;
	}
}
