package org.usfirst.frc.team1640.utilities;

public abstract class State {
	
	// handle for the state
	public abstract void execute();
	
	// if a State change is called externally,
	//    this method provides a way to signal
	//    to a State that the robot is leaving that State
	//    and it should end any operations that need to stop
	public abstract void leave();
}
