package org.usfirst.frc.team1640.teleop;

public class Example2TeleopState extends TeleopState {
	private TeleopContext context;
	
	
	
	private TeleopState nextState = this;
	
	private boolean timeHasPassed = false;
	
	// all external context must be fed through constructor
	public Example2TeleopState(TeleopContext context) {
		this.context = context;
	}
	
	@Override
	public TeleopState getNextState() {
		return nextState;
	}

	@Override
	public void execute() {
		
		// code
		
		if (timeHasPassed) {
			nextState = new Example1TeleopState(context);
		}
	}

	@Override
	public void leave() {
		
	}
	
}