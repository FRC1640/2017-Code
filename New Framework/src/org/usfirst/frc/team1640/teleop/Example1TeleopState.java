package org.usfirst.frc.team1640.teleop;

import org.usfirst.frc.team1640.controller.ControllerContext;

public class Example1TeleopState extends TeleopState {
	private TeleopContext context;
	
	private ControllerContext driverControllerContext;
	
	private TeleopState nextState = this;
	
	// all external context must be fed through constructor
	public Example1TeleopState(TeleopContext context) {
		this.context = context;
		
		driverControllerContext = context.getDriverControllerContext();
	}
	
	@Override
	public TeleopState getNextState() {
		return nextState;
	}

	@Override
	public void execute() {
		
		// code
		
		if (driverControllerContext.getAButton()) {
			nextState = new Example2TeleopState(context);
		}
	}

	@Override
	public void leave() {
		
	}
	
}
