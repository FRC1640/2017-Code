package drive;

import utilities.Utilities;
import constants.Constants;

public class SwerveDriveBase extends DriveControl {
	private static SwerveDriveBase swerveDriveBase; //singleton instance
//	private double prevFLS, prevFRS, prevBLS, prevBRS;
	
	public static SwerveDriveBase getInstance(){ //get singleton instance
		if(swerveDriveBase == null)
			swerveDriveBase = new SwerveDriveBase();
		return swerveDriveBase;
	}
	
	private SwerveDriveBase(){};

	// executes the swerve drive functionality
	@Override
	protected void execute(double x1, double y1, double x2, double y2){
		DriveIO driveIO = DriveIO.getInstance();
		double xPos = x1 + x2 * Constants.ROBOT_WIDTH_TO_RADIUS_RATIO;
		double xNeg = x1 - x2 * Constants.ROBOT_WIDTH_TO_RADIUS_RATIO;
		double yPos = y1 + x2 * Constants.ROBOT_LENGTH_TO_RADIUS_RATIO;
		double yNeg = y1 - x2 * Constants.ROBOT_LENGTH_TO_RADIUS_RATIO;
		
		//System.out.println("Xpos: " + xPos);
		//System.out.println("yNeg: " + yNeg);
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
		
		Pivot fl = driveIO.getPivots()[0];
		Pivot fr = driveIO.getPivots()[1];
		Pivot bl = driveIO.getPivots()[2];
		Pivot br = driveIO.getPivots()[3];
		
		
		if((x1 == 0 && y1 == 0 && x2 == 0)){
//			fls = fl.getAngle();
//			frs = fr.getAngle();
//			bls = bl.getAngle();
//			brs = br.getAngle();
			fls = fl.getAngleSetpoint();
			frs = fr.getAngleSetpoint();
			bls = bl.getAngleSetpoint();
			brs = br.getAngleSetpoint();
		}
		
		fl.setTargetAngle(fls);
		fr.setTargetAngle(frs);
		bl.setTargetAngle(bls);
		br.setTargetAngle(brs);

		fl.setDrive(fld);
		fr.setDrive(frd);	
		bl.setDrive(bld);
		br.setDrive(brd);
		
		
//		System.out.println(brd);
		
//		System.out.println("FLS: " + fls + "| FLA: " + fl.getAngle());
//		System.out.println("FRS: " + frs + "| FRA: " + fr.getAngle());
//		System.out.println("BLS: " + bls + "| BLA: " + bl.getAngle());
//		System.out.println("BLS: " + brs + "| BRA: " + br.getAngle());

//		prevFLS = fls;
//		prevFRS = frs;
//		prevBLS = bls;
//		prevBRS = brs;
	}
	
}
