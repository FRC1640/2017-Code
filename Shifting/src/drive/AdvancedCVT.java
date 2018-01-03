package drive;

import utilities.ControlInputs;
import utilities.Utilities;

import constants.Constants;
import constants.ControllerConstants;
import constants.PortConstants;

public class AdvancedCVT extends DriveControl {
	private static AdvancedCVT advancedCVT; //singleton instance
	private DriveIO driveIO;
	private final double SCALAR = 0.2;
	private final double MAX_MOTOR = (1.0 / 3), MAX_SERVO = (2.0 / 3);
	private final double NORMAL_ANGLE = 120, MAX_ANGLE = 20, SLOW_ANGLE = 170,IDEAL_MOTOR = 0.5, MIN_THRESHOLD = 45, MIN_CURRENT_THRESHOLD = 10, MIN_COUNTS_PER_REV_THRESHOLD = 10000;
	private boolean slow, prevSlow, fast, prevFast;
	private int count;
	
	public static AdvancedCVT getInstance(){ //get singleton instance
		if(advancedCVT == null)
			advancedCVT = new AdvancedCVT();
		return advancedCVT;
	}
	
	private AdvancedCVT(){
		driveIO = DriveIO.getInstance();
	}

	// executes the swerve drive functionality
	@Override
	protected void execute(double x1, double y1, double x2, double y2){
		if(slow){
			x1 *= SCALAR;
			x2 *= SCALAR;
			y1 *= SCALAR;
//			if(count % 10 == 0)
		}
		DriveIO driveIO = DriveIO.getInstance();
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
		
		CVTPivot fl = driveIO.getPivots()[0];
		CVTPivot fr = driveIO.getPivots()[1];
		CVTPivot bl = driveIO.getPivots()[2];
		CVTPivot br = driveIO.getPivots()[3];
		
		
		if((x1 == 0 && y1 == 0 && x2 == 0)){
			fls = fl.getAngle();
			frs = fr.getAngle();
			bls = bl.getAngle();
			brs = br.getAngle();
		}
		
		fl.setTargetAngle(fls);
		fr.setTargetAngle(frs);
		bl.setTargetAngle(bls);
		br.setTargetAngle(brs);
		
		if(slow){
			fl.setDrive(fld);
			fr.setDrive(frd);	
			bl.setDrive(bld);
			br.setDrive(brd);
			
			fl.setServoAngle(SLOW_ANGLE);
			fr.setServoAngle(SLOW_ANGLE);
			bl.setServoAngle(SLOW_ANGLE);
			br.setServoAngle(SLOW_ANGLE);
		}
		else if (fast) {
			fl.setDrive(fld);
			fr.setDrive(frd);
			bl.setDrive(bld);
			br.setDrive(brd);
			
			fl.setServoAngle(MAX_ANGLE);
			fr.setServoAngle(MAX_ANGLE);
			bl.setServoAngle(MAX_ANGLE);
			br.setServoAngle(MAX_ANGLE);
		}
		else{
			fl.setDrive(calculateMotorSpeed(fld));
			fr.setDrive(calculateMotorSpeed(frd));	
			bl.setDrive(calculateMotorSpeed(bld));
			br.setDrive(calculateMotorSpeed(brd));
			
			fl.setServoAngle(calculateServoAngle(fld));
			fr.setServoAngle(calculateServoAngle(frd));
			bl.setServoAngle(calculateServoAngle(bld));
			br.setServoAngle(calculateServoAngle(brd));
		}
		count++;
//		boolean slowButton = ControlInputs.getDriver().getButton(ControllerConstants.SLOW_DRIVER);
//		if(slowButton && !prevSlow){
//			slow = !slow;
//			count++;
//			System.out.println("Changing slow: " + slow + " Count: " + count);
//		}
//		prevSlow = slowButton;
		if(ControlInputs.getDriver().getButton(ControllerConstants.SLOW_DRIVER)){
			slow = true;
			fast = false;
			System.out.println("Slow enabled: " + count);
		}
		else if(ControlInputs.getDriver().getButton(ControllerConstants.MED_DRIVER)){
			slow = false;
			fast = false;
		}
		else if (ControlInputs.getDriver().getButton(ControllerConstants.FAST_DRIVER)) {
			slow = false;
			fast = true;
			System.out.println("Fast Override");
		}
		else{
			fast = false;
		}
	}
	
	@Override
	public void deconstruct(){
		Pivot[] pivots = DriveIO.getInstance().getPivots();
		for(Pivot p:pivots){
			p.setVelocityControl(false);
		}
		System.out.println("In ACVT deconstructor");
	}
	
	private double calculateServoAngle(double joy){
		joy = Math.abs(joy);
//		if(getCurrent() > MIN_CURRENT_THRESHOLD){
		if(getMinVelocity() < MIN_COUNTS_PER_REV_THRESHOLD){
//			System.out.println(getCurrent());
			return NORMAL_ANGLE;
		}
		if(joy <= MAX_MOTOR)
			return NORMAL_ANGLE;
		else if(joy >= MAX_SERVO)
			return MAX_ANGLE;
		else
			return (joy - MAX_MOTOR) * (MAX_ANGLE - NORMAL_ANGLE) / (MAX_SERVO - MAX_MOTOR) + NORMAL_ANGLE;
	}
	
	private double calculateMotorSpeed(double joy){
		double direction = joy == 0 ? 0 : joy / Math.abs(joy);
		joy = Math.abs(joy);
		if(joy <= MAX_MOTOR)
			return joy * (IDEAL_MOTOR / MAX_MOTOR) * direction;
		else if(joy <= MAX_SERVO)
			return IDEAL_MOTOR * direction;
		else 
			return (joy - MAX_SERVO) * IDEAL_MOTOR / (1 - MAX_SERVO) + IDEAL_MOTOR * direction;
	}
	
	private double getMinVelocity(){
		Pivot[] pivots = DriveIO.getInstance().getPivots();
		double minVelocity = Math.abs(pivots[0].getVelocity());
		for(Pivot p : pivots)
			if(p.getVelocity() != 0){
				minVelocity = minVelocity == 0 ? Math.abs(p.getVelocity()) : Math.min(minVelocity, Math.abs(p.getVelocity()));
			}
		return minVelocity;
		
//		return Math.abs(DriveIO.getInstance().getPivots()[2].getVelocity());
	}
	
	private double getRelativeVelocity(){
		//Trying a different algorithm using mean, median, and smallest
		//Honestly, It's a complete guess if the /2 will get anything near a useful min value....
		
		Pivot[] pivots = driveIO.getPivots();
		double[] pivotVelocity = new double[pivots.length];
		boolean shouldUseMean = true;
		
		for(int i = pivots.length - 1; i >= 0; i--){
			if(pivots[i].getVelocity() == 0){
				shouldUseMean = false;
			}
			pivotVelocity[i] = pivots[i].getVelocity();
		}
		
		double minVelocity = Utilities.minAbs(pivotVelocity);
		double meanVelocity = Utilities.mean(pivotVelocity);
		double medianVelocity = Utilities.median(pivotVelocity);
		
		if((minVelocity > meanVelocity / 2) || (minVelocity > medianVelocity / 2)){
			return minVelocity;
		}
		else if(shouldUseMean && (meanVelocity > medianVelocity)){
			return meanVelocity / 2;
		}
		else{
			return medianVelocity / 2;
		}
	}
	
}
