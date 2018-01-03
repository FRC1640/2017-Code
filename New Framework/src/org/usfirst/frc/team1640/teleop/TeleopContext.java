package org.usfirst.frc.team1640.teleop;

import org.usfirst.frc.team1640.controller.ControllerContext;

public class TeleopContext {
	ControllerContext driverControllerContext, operatorControllerContext;
	
	public TeleopContext(ControllerContext driverControllerContext, ControllerContext operatorControllerContext) {
		this.driverControllerContext = driverControllerContext;
		this.operatorControllerContext = operatorControllerContext;
	}
	
	public ControllerContext getDriverControllerContext() {
		return driverControllerContext;
	}
	
	public ControllerContext getOperatorControllerContext() {
		return operatorControllerContext;
	}
}
