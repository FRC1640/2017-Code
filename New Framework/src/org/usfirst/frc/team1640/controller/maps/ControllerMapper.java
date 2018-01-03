package org.usfirst.frc.team1640.controller.maps;

import org.usfirst.frc.team1640.controller.ControllerContext;
import org.usfirst.frc.team1640.controller.XboxControllerSetting;

public abstract class ControllerMapper implements XboxControllerSetting {

	public abstract ControllerContext getControllerContext();
	
	public final void mapAll() {
		mapAButton();
		mapBButton();
		mapXButton();
		mapYButton();
		mapBackButton();
		mapStartButton();
		mapLeftStickButton();
		mapRightStickButton();
		
		mapLeftXAxis();
		mapRightXAxis();
		mapRightXAxis();
		mapRightYAxis();
		mapLeftTriggerAxis();
		mapRightTriggerAxis();
		
		mapPOV();
	}
	
	// Buttons
	abstract void mapAButton();
	abstract void mapBButton();
	abstract void mapXButton();
	abstract void mapYButton();
	abstract void mapLeftBumper();
	abstract void mapRightBumper();
	abstract void mapBackButton();
	abstract void mapStartButton();
	abstract void mapLeftStickButton();
	abstract void mapRightStickButton();
	
	// Axes
	abstract void mapLeftXAxis();
	abstract void mapLeftYAxis();
	abstract void mapRightXAxis();
	abstract void mapRightYAxis();
	abstract void mapLeftTriggerAxis();
	abstract void mapRightTriggerAxis();
	
	// POV
	abstract void mapPOV();
	
}
