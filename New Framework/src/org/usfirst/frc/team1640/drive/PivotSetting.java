package org.usfirst.frc.team1640.drive;

public class PivotSetting {
	private double speed, angle;
	
	PivotSetting(double speed, double angle) {
		this.speed = constrainToValidSpeed(speed);
		this.angle = constrainToValidAngle(angle);
	}
	
	PivotSetting() {
		this.speed = 0;
		this.angle = 0;
	}
	
	// constrain drive speed on the range (-1.0, 1.0)
	private double constrainToValidSpeed(double d) {
		
		if (d > 1.0) d = 1.0;
		if (d < -1.0) d = -1.0;
		
		return d;
	}
	
	// constrain drive angle on the range [0, 360)
	private double constrainToValidAngle(double d) {
		return d % 360;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public void setSpeed(double speed) {
		this.speed = constrainToValidSpeed(speed);
	}
	
	public void setAngle(double angle) {
		this.angle = constrainToValidAngle(angle);
	}
}
