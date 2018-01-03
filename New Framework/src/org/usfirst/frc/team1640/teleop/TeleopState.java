package org.usfirst.frc.team1640.teleop;

import org.usfirst.frc.team1640.utilities.State;

public abstract class TeleopState extends State {
	
	// handle for context to receive state for next iteration
	// should be overridden in most cases
	// by default, a TeleopState should return itself
	public abstract TeleopState getNextState();

}