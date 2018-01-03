package org.usfirst.frc.team1640.controller;


public class ControllerContext implements XboxControllerSetting {
	XboxControllerSetting controllerSetting;
	
	public ControllerContext(XboxControllerSetting controllerSetting) {
		this.controllerSetting = controllerSetting;
	}

	@Override
	public boolean getAButton() {
		return controllerSetting.getAButton();
	}

	@Override
	public boolean getBButton() {
		return controllerSetting.getBButton();
	}

	@Override
	public boolean getXButton() {
		return controllerSetting.getXButton();
	}

	@Override
	public boolean getYButton() {
		return controllerSetting.getYButton();
	}

	@Override
	public boolean getLeftBumper() {
		return controllerSetting.getLeftBumper();
	}

	@Override
	public boolean getRightBumper() {
		return controllerSetting.getRightBumper();
	}

	@Override
	public boolean getBackButton() {
		return controllerSetting.getBackButton();
	}

	@Override
	public boolean getStartButton() {
		return controllerSetting.getStartButton();
	}

	@Override
	public boolean getLeftStickButton() {
		return controllerSetting.getLeftStickButton();
	}

	@Override
	public boolean getRightStickButton() {
		return controllerSetting.getRightStickButton();
	}

	@Override
	public double getLeftXAxis() {
		return controllerSetting.getLeftXAxis();
	}

	@Override
	public double getLeftYAxis() {
		return controllerSetting.getLeftYAxis();
	}

	@Override
	public double getRightXAxis() {
		return controllerSetting.getRightXAxis();
	}

	@Override
	public double getRightYAxis() {
		return controllerSetting.getRightYAxis();
	}

	@Override
	public double getLeftTriggerAxis() {
		return controllerSetting.getLeftTriggerAxis();
	}

	@Override
	public double getRightTriggerAxis() {
		return controllerSetting.getRightTriggerAxis();
	}

	@Override
	public int getPOV() {
		return controllerSetting.getPOV();
	}
	
	
	
}
