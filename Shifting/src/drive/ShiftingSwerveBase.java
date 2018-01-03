package drive;


public class ShiftingSwerveBase extends DriveControl {
	private static ShiftingSwerveBase shifting; //singleton instance
	private double[] prevAngles = {0, 0, 0, 0};
	
	public static ShiftingSwerveBase getInstance(){ //get singleton instance
		if(shifting == null)
			shifting = new ShiftingSwerveBase();
		return shifting;
	}
	
	private ShiftingSwerveBase(){};

	// executes the swerve drive functionality
	@Override
	protected void execute(double x1, double y1, double x2, double y2){
		DriveIO driveIO = DriveIO.getInstance();
			
		CVTPivot[] pivots = driveIO.getPivots();

		double[] driveSettings = DriveMath.calcDriveSettings(x1, y1, x2, driveIO.getPivots());

		for(int i = 0; i < 4; i++){
			double drive = driveSettings[i];
			double vel = pivots[i].getVelocity();
			double angle = pivots[i].getAngle();
			double deltaAngle = angle - prevAngles[i];
			
			pivots[i].setDrive(DriveMath.getMotor(drive, vel, deltaAngle));
			pivots[i].setServoAngle(DriveMath.getServo(drive, vel, deltaAngle));
			pivots[i].setTargetAngle(driveSettings[4 + i]);
		
			prevAngles[i] = angle;
		}
		
	}	
}
