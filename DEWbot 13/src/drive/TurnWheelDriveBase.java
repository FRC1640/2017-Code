package drive;

import utilities.Utilities;
import constants.Constants;

public class TurnWheelDriveBase extends DriveControl {
	private static TurnWheelDriveBase turnWheelDriveBase; //singleton instance
	
	public static TurnWheelDriveBase getInstance(){ //get singleton instance
		if(turnWheelDriveBase == null)
			turnWheelDriveBase = new TurnWheelDriveBase();
		return turnWheelDriveBase;
	}
	
	private TurnWheelDriveBase(){};

	// executes the swerve drive functionality
	@Override
	protected void execute(double x1, double y1, double x2, double y2){
		DriveIO driveIO = DriveIO.getInstance();
		double xPos = x1 + x2 * Constants.ROBOT_WIDTH_TO_RADIUS_RATIO;
		double xNeg = x1 - x2 * Constants.ROBOT_WIDTH_TO_RADIUS_RATIO;
		double yPos = y1 + x2 * Constants.ROBOT_LENGTH_TO_RADIUS_RATIO;
		double yNeg = y1 - x2 * Constants.ROBOT_LENGTH_TO_RADIUS_RATIO;
	
		double fls = Utilities.angle(xPos, yPos);
		double frs = Utilities.angle(xPos, yNeg);
		double bls = Utilities.angle(xNeg, yPos);
		double brs = Utilities.angle(xNeg, yNeg);
		
		Pivot fl = driveIO.getPivots()[0];
		Pivot fr = driveIO.getPivots()[1];
		Pivot bl = driveIO.getPivots()[2];
		Pivot br = driveIO.getPivots()[3];
		
		
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
		
	}
}
