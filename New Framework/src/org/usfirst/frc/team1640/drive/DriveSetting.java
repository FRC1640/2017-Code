package org.usfirst.frc.team1640.drive;


public class DriveSetting {
	private PivotSetting frontLeft, frontRight, backLeft, backRight;
	
	public DriveSetting(PivotSetting frontLeft, PivotSetting frontRight, PivotSetting backLeft, PivotSetting backRight) {
		this.frontLeft = frontLeft;
		this.frontRight = frontRight;
		this.backLeft = backLeft;
		this.backRight = backRight;
	}
	
	public DriveSetting() {
		this.frontLeft = new PivotSetting();
		this.frontRight = new PivotSetting();
		this.backLeft = new PivotSetting();
		this.backRight = new PivotSetting();
	}
	
	public PivotSetting getFrontLeftPivotSetting() {
		return frontLeft;
	}
	
	public PivotSetting getFrontRightPivotSetting() {
		return frontRight;
	}
	
	public PivotSetting getBackLeftPivotSetting() {
		return backLeft;
	}
	
	public PivotSetting getBackRightPivotSetting() {
		return backRight;
	}
	
	public void stopAllPivots() {
		frontLeft.setSpeed(0.0);
		frontRight.setSpeed(0.0);
		backLeft.setSpeed(0.0);
		backRight.setSpeed(0.0);
	}
}
