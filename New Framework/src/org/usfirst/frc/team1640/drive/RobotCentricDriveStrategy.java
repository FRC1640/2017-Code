package org.usfirst.frc.team1640.drive;

import org.usfirst.frc.team1640.constants.DimensionConstants;
import org.usfirst.frc.team1640.controller.ControllerContext;
import org.usfirst.frc.team1640.utilities.Utilities;

public class RobotCentricDriveStrategy extends OcelotDriveStrategy {
	DriveSetting driveSetting;
	ControllerContext controllerContext;
	
	double x1, y1, x2;
	
	double xPos, xNeg, yPos, yNeg;
	
	double flSpeed, frSpeed, blSpeed, brSpeed;
	double flAngle, frAngle, blAngle, brAngle;
	
	public RobotCentricDriveStrategy(DriveSetting driveSetting, ControllerContext controllerContext) {
		this.driveSetting = driveSetting;
		this.controllerContext = controllerContext;
	}

	@Override
	public void invoke() {
		
		x1 = controllerContext.getLeftXAxis();
		y1 = controllerContext.getLeftYAxis();
		x2 = controllerContext.getRightXAxis();
		
		calculateVectorComponents();
		
		calculateRelativeSpeeds();
		normalizeSpeeds();
		
		calculateAngles();
		
		// update the pivots
		PivotSetting flPivotSetting = driveSetting.getFrontLeftPivotSetting();
		flPivotSetting.setSpeed(flSpeed);
		flPivotSetting.setAngle(flAngle);
		
		PivotSetting frPivotSetting = driveSetting.getFrontRightPivotSetting();
		frPivotSetting.setSpeed(frSpeed);
		frPivotSetting.setAngle(frAngle);
		
		PivotSetting blPivotSetting = driveSetting.getBackLeftPivotSetting();
		blPivotSetting.setSpeed(blSpeed);
		blPivotSetting.setAngle(blAngle);
		
		PivotSetting brPivotSetting = driveSetting.getBackRightPivotSetting();
		brPivotSetting.setSpeed(brSpeed);
		brPivotSetting.setAngle(brAngle);
	}
	
	private void calculateVectorComponents() {
		xPos = x1 + x2 * DimensionConstants.WIDTH_TO_DIAGONAL_RATIO;
		xNeg = x1 - x2 * DimensionConstants.WIDTH_TO_DIAGONAL_RATIO;
		yPos = y1 + x2 * DimensionConstants.LENGTH_TO_DIAGONAL_RATIO;
		yNeg = y1 - x2 * DimensionConstants.LENGTH_TO_DIAGONAL_RATIO;
	}
	
	private void calculateRelativeSpeeds() {
		flSpeed = Utilities.magnitude(xPos, yPos);
		frSpeed = Utilities.magnitude(xPos, yNeg);
		blSpeed = Utilities.magnitude(xNeg, yPos);
		brSpeed = Utilities.magnitude(xNeg, yNeg);
	}
	
	private void normalizeSpeeds() {
		double sx1, sy1, sx2;
		double m = Math.max(Utilities.magnitude(x1, y1), Math.abs(x2));
		if (m != 0) {
			sx1 = x1/m;
			sy1 = y1/m;
			sx2 = x2/m;
		}
		else {
			sx1 = 0;
			sy1 = 0;
			sx2 = 0;
		}
		
		// normalize speeds
		double maxMag = Utilities.magnitude(Math.abs(sx1) + DimensionConstants.WIDTH_TO_DIAGONAL_RATIO*Math.abs(sx2),
				Math.abs(sy1) + DimensionConstants.LENGTH_TO_DIAGONAL_RATIO*Math.abs(sx2));
		
		if (maxMag != 0) {
			flSpeed /= maxMag;
			frSpeed /= maxMag;
			blSpeed /= maxMag;
			brSpeed /= maxMag;
		}
		else {
			flSpeed = 0;
			frSpeed = 0;
			blSpeed = 0;
			brSpeed = 0;
		}
	}
	
	private void calculateAngles() {
		flAngle = Utilities.angle(xPos, yPos);
		frAngle = Utilities.angle(xPos, yNeg);
		blAngle = Utilities.angle(xNeg, yPos);
		brAngle = Utilities.angle(xNeg, yNeg);
	}

}
