package org.usfirst.frc.team1640.robot.states;

import org.usfirst.frc.team1640.controller.ControllerContext;
import org.usfirst.frc.team1640.teleop.Example1TeleopState;
import org.usfirst.frc.team1640.teleop.TeleopContext;
import org.usfirst.frc.team1640.teleop.TeleopState;


public class TeleopRobotState extends RobotState {
	ControllerContext driverControllerSetting, operatorControllerSetting;
	
	private TeleopState myState;
	
	private boolean isInit = false;
	
	public TeleopRobotState(ControllerContext driverControllerContext, ControllerContext operatorControllerContext) {
		this.driverControllerSetting = driverControllerContext;
		this.operatorControllerSetting = operatorControllerContext;
		
		// construct initial state with initial context
		myState = new Example1TeleopState(new TeleopContext(driverControllerContext, operatorControllerContext));
	}
	
	private void init() {
		
		isInit = true;
	}
	
	@Override
	public void execute() {
		if (!isInit) {
			init();
		}
		
		myState = myState.getNextState();
		myState.execute();
	}

	@Override
	public void leave() {
		myState.leave();
	}
	
}
