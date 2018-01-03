package org.usfirst.frc.team1640.controller;

public interface XboxControllerSetting {

	public boolean getAButton();
	public boolean getBButton();
	public boolean getXButton();
	public boolean getYButton();
	public boolean getLeftBumper();
	public boolean getRightBumper();
	public boolean getBackButton();
	public boolean getStartButton();
	public boolean getLeftStickButton();
	public boolean getRightStickButton();
	
	// Axes
	public double getLeftXAxis();
	public double getLeftYAxis();
	public double getRightXAxis();
	public double getRightYAxis();
	public double getLeftTriggerAxis();
	public double getRightTriggerAxis();
	
	// POV
	public int getPOV();
	
}
