package org.usfirst.frc.team1640.utilities;

public class Utilities {
	private Utilities() {}
	
	public static double magnitude(double x, double y) {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public static double angle(double x, double y) {
		return Math.toDegrees(Math.atan2(-x, -y)) + 180;
	}
}
