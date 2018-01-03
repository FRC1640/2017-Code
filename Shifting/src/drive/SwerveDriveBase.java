package drive;

import utilities.Utilities;
import constants.Constants;

public class SwerveDriveBase extends DriveControl {
	private static SwerveDriveBase swerveDriveBase; //singleton instance
	
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
				
		Pivot fl = driveIO.getPivots()[0];
		Pivot fr = driveIO.getPivots()[1];
		Pivot bl = driveIO.getPivots()[2];
		Pivot br = driveIO.getPivots()[3];

		double[] driveSettings = DriveMath.calcDriveSettings(x1, y1, x2, driveIO.getPivots());

		fl.setDrive(driveSettings[0]);
		fr.setDrive(driveSettings[1]);	
		bl.setDrive(driveSettings[2]);
		br.setDrive(driveSettings[3]);
		
		fl.setTargetAngle(driveSettings[4]);
		fr.setTargetAngle(driveSettings[5]);
		bl.setTargetAngle(driveSettings[6]);
		br.setTargetAngle(driveSettings[7]);
	}	
}
