package drive;

import utilities.Utilities;
import constants.Constants;

public class DriveMath {
	private static double MAX_VEL = 0, ANGLE_FACTOR = 0, VEL_FACTOR = 0;
	
	public static double[] calcDriveSettings(double x1, double y1, double x2, Pivot[] pivots){		
		double xPos = x1 + x2 * Constants.ROBOT_WIDTH_TO_RADIUS_RATIO;
		double xNeg = x1 - x2 * Constants.ROBOT_WIDTH_TO_RADIUS_RATIO;
		double yPos = y1 + x2 * Constants.ROBOT_LENGTH_TO_RADIUS_RATIO;
		double yNeg = y1 - x2 * Constants.ROBOT_LENGTH_TO_RADIUS_RATIO;
		
		double fld = Utilities.magnitude(xPos, yPos);
		double frd = Utilities.magnitude(xPos, yNeg);
		double bld = Utilities.magnitude(xNeg, yPos);
		double brd = Utilities.magnitude(xNeg, yNeg);
		
		double maxd = Math.max(fld, Math.max(frd, Math.max(bld, brd))); 
		
		if (maxd > 1){
			fld /= maxd;
			frd /= maxd;
			bld /= maxd;
			brd /= maxd;
		}
		
		double fls = Utilities.angle(xPos, yPos);
		double frs = Utilities.angle(xPos, yNeg);
		double bls = Utilities.angle(xNeg, yPos);
		double brs = Utilities.angle(xNeg, yNeg);		
		
		if((x1 == 0 && y1 == 0 && x2 == 0)){
			fls = pivots[0].getAngleSetpoint();
			frs = pivots[1].getAngleSetpoint();
			bls = pivots[2].getAngleSetpoint();
			brs = pivots[3].getAngleSetpoint();
		}
		
		double[] settings = {fld, frd, bld, brd, fls, frs, bls, brs};  
		return settings;
	}
	
	public static double getServo(double drive, double currentVel, double deltaAngle){
		return velToServo(calculateOutputVel(drive, currentVel, deltaAngle));
	}
	
	public static double getMotor(double drive, double currentVel, double deltaAngle){
		return RPMToOutput(velToRPM(calculateOutputVel(drive, currentVel, deltaAngle)));
	}
	
	private static double calculateOutputVel(double drive, double currentVel, double deltaAngle){
		double desiredVel = driveToVel(drive);
		double deltaShift = (desiredVel - currentVel) * (VEL_FACTOR - ANGLE_FACTOR * Math.abs(deltaAngle));
		double something = 0; //TODO: Is this a shift in param or mph?
		return something + deltaShift;
	}
	
	private static double STRAIGHT_VEL, HIGH_VEL, STRAIGHT_RPM, HIGH_SLOPE, HIGH_INTERCEPT;
	private static double velToRPM(double vel){
		double sign = vel / Math.abs(vel);
		vel = Math.abs(vel);
		if(vel < STRAIGHT_VEL)
			return (STRAIGHT_RPM - STRAIGHT_VEL) * vel * sign;
		else if(vel < HIGH_VEL)
			return STRAIGHT_RPM * sign;
		return (HIGH_SLOPE * vel + HIGH_INTERCEPT) * sign;
		
	}
	
	private static double driveToVel(double drive){
		return drive * MAX_VEL;
	}
	
	private static double RPMToOutput(double RPM){ //depends on how power vs speed graph is found
		return RPM;
	}
	
	private static double LOW_GEAR, MAX_ANGLE;
	private static double velToServo(double vel){
		if(vel < STRAIGHT_VEL)
			return LOW_GEAR;
		else if(vel < HIGH_VEL){
			double slope = (MAX_ANGLE - LOW_GEAR) / (HIGH_VEL - STRAIGHT_VEL);
			return LOW_GEAR - slope * vel;
		}
		return MAX_ANGLE;
	}
}
