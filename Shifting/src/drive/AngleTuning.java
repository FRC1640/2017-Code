package drive;

import utilities.ControlInputs;
import utilities.Utilities;

import constants.Constants;
import constants.ControllerConstants;
import constants.PortConstants;

public class AngleTuning extends DriveControl {
	private static AngleTuning angleTuning; //singleton instance
	private DriveIO driveIO;
	
	public static AngleTuning getInstance(){ //get singleton instance
		if(angleTuning == null)
			angleTuning = new AngleTuning();
		return angleTuning;
	}
	
	private AngleTuning(){
		driveIO = DriveIO.getInstance();
	}

	// executes the swerve drive functionality
	@Override
	protected void execute(double x1, double y1, double x2, double y2){
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
		
		if(ControlInputs.getDriver().getPOV() != -1){
			fls = ControlInputs.getDriver().getPOV();
			frs = ControlInputs.getDriver().getPOV();
			bls = ControlInputs.getDriver().getPOV();
			brs = ControlInputs.getDriver().getPOV();
		}
		
		fl.setTargetAngle(fls);
		fr.setTargetAngle(frs);
		bl.setTargetAngle(bls);
		br.setTargetAngle(brs);
		
		double error = Math.abs(fl.getAngle() - fls);
		System.out.println(error + " | " + Math.abs(error - 180));
	}
	
	@Override
	public void deconstruct(){
	}
	
}
