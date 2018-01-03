package org.usfirst.frc.team1640.controller.maps;

import org.usfirst.frc.team1640.controller.ControllerContext;
import org.usfirst.frc.team1640.utilities.Utilities;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class DefaultControllerMapper extends ControllerMapper {
	XboxController controller;
	ControllerContext controllerContext;
	
	private final double DEADBAND = 0.23;
	
	private boolean aButton, bButton, xButton, yButton, leftBumper, rightBumper, backButton, startButton, leftStickButton, rightStickButton;
	
	private double leftXAxis, leftYAxis, rightXAxis, rightYAxis, leftTriggerAxis, rightTriggerAxis;
	
	private int pov;
	
	public DefaultControllerMapper(int port) {
		controller = new XboxController(port);
		controllerContext = new ControllerContext(this);
	}
	
	@Override
	public ControllerContext getControllerContext() {
		return controllerContext;
	}
	
	@Override
	protected void mapAButton() {
		aButton = controller.getAButton();
	}

	@Override
	protected void mapBButton() {
		bButton = controller.getBButton();
	}

	@Override
	protected void mapXButton() {
		xButton = controller.getXButton();
	}

	@Override
	protected void mapYButton() {
		yButton = controller.getYButton();
	}
	
	@Override
	protected void mapLeftBumper() {
		leftBumper = controller.getBumper(Hand.kLeft);
	}

	@Override
	protected void mapRightBumper() {
		rightBumper = controller.getBumper(Hand.kRight);
	}

	@Override
	protected void mapBackButton() {
		backButton = controller.getBackButton();
	}

	@Override
	protected void mapStartButton() {
		startButton = controller.getStartButton();
	}

	@Override
	protected void mapLeftStickButton() {
		leftStickButton = controller.getStickButton(Hand.kLeft);
	}

	@Override
	protected void mapRightStickButton() {
		rightStickButton = controller.getStickButton(Hand.kRight);
	}

	@Override
	protected void mapLeftXAxis() {
		final int X_AXIS = 0;
		leftXAxis = applyDeadband2D(controller.getX(Hand.kLeft), controller.getY(Hand.kLeft), DEADBAND)[X_AXIS];
	}

	@Override
	protected void mapLeftYAxis() {
		final int Y_AXIS = 1;
		leftYAxis = applyDeadband2D(controller.getX(Hand.kLeft), controller.getY(Hand.kLeft), DEADBAND)[Y_AXIS];
	}

	@Override
	protected void mapRightXAxis() {
		final int X_AXIS = 0;
		rightXAxis = applyDeadband2D(controller.getX(Hand.kRight), controller.getY(Hand.kRight), DEADBAND)[X_AXIS];
	}

	@Override
	protected void mapRightYAxis() {
		final int Y_AXIS = 0;
		rightYAxis = applyDeadband2D(controller.getX(Hand.kRight), controller.getY(Hand.kRight), DEADBAND)[Y_AXIS];
	}

	@Override
	protected void mapLeftTriggerAxis() {
		leftTriggerAxis = controller.getTriggerAxis(Hand.kLeft);

	}

	@Override
	protected void mapRightTriggerAxis() {
		rightTriggerAxis = controller.getTriggerAxis(Hand.kRight);
	}

	@Override
	protected void mapPOV() {
		pov = controller.getPOV(0);
	}
	
	protected double applyDeadband1D(double value, double limit) {
		double direction = Math.signum(value);
		value = Math.abs(value);
		if(value < limit) //if value is below limit, return 0
			return 0;
		else //adjust values to compensate
			return direction * (value - limit) / (1-limit);
	}
	
	// returns array {newXValue, newYValue}
	protected double[] applyDeadband2D(double xValue, double yValue, double limit) {
		
		double magnitude = Utilities.magnitude(xValue, yValue);
		
		if (magnitude < limit) {
			return new double[] {0.0, 0.0};
		} 
		else {
			double newMagnitude = applyDeadband1D(magnitude, limit);
			double theta = Math.atan2(yValue, xValue);
			double[] values = {Math.cos(theta) * newMagnitude, Math.sin(theta) * newMagnitude};
			return values;
		}
	}

	@Override
	public boolean getAButton() {
		return aButton;
	}

	@Override
	public boolean getBButton() {
		return bButton;
	}

	@Override
	public boolean getXButton() {
		return xButton;
	}

	@Override
	public boolean getYButton() {
		return yButton;
	}

	@Override
	public boolean getLeftBumper() {
		return leftBumper;
	}

	@Override
	public boolean getRightBumper() {
		return rightBumper;
	}

	@Override
	public boolean getBackButton() {
		return backButton;
	}

	@Override
	public boolean getStartButton() {
		return startButton;
	}

	@Override
	public boolean getLeftStickButton() {
		return leftStickButton;
	}

	@Override
	public boolean getRightStickButton() {
		return rightStickButton;
	}

	@Override
	public double getLeftXAxis() {
		return leftXAxis;
	}

	@Override
	public double getLeftYAxis() {
		return leftYAxis;
	}

	@Override
	public double getRightXAxis() {
		return rightXAxis;
	}

	@Override
	public double getRightYAxis() {
		return rightYAxis;
	}

	@Override
	public double getLeftTriggerAxis() {
		return leftTriggerAxis;
	}

	@Override
	public double getRightTriggerAxis() {
		return rightTriggerAxis;
	}

	@Override
	public int getPOV() {
		return pov;
	}
}
