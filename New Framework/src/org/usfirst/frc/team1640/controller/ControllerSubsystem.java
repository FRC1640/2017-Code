package org.usfirst.frc.team1640.controller;

import org.usfirst.frc.team1640.controller.maps.DefaultControllerMapper;
import org.usfirst.frc.team1640.controller.maps.ControllerMapper;
import org.usfirst.frc.team1640.utilities.Subsystem;

public final class ControllerSubsystem extends Subsystem {
	ControllerContext driverControllerContext, operatorControllerContext;
	ControllerMapper driverControllerMapper, operatorControllerMapper;
	
	boolean isInit = false;
	
	public ControllerSubsystem() {
		
		final int DRIVER_PORT = 0;
		driverControllerMapper = new DefaultControllerMapper(DRIVER_PORT);
		driverControllerContext = driverControllerMapper.getControllerContext();
		
		final int OPERATOR_PORT = 1;
		operatorControllerMapper = new DefaultControllerMapper(OPERATOR_PORT);
		operatorControllerContext = operatorControllerMapper.getControllerContext();
	}
	
	@Override
	public void init() {
		
		// initialization code
		
		isInit = true;
	}
	
	@Override
	public void update() {
		if (!isInit) {
			init();
		}
		
		driverControllerMapper.mapAll();
		operatorControllerMapper.mapAll();
	}
	
	public ControllerContext getDriverControllerContext() {
		return driverControllerContext;
	}
	
	public ControllerContext getOperatorControllerContext() {
		return operatorControllerContext;
	}
	
}
