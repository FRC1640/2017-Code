package drive;


public class MoveForward extends MoveDecorator{
	private static double p = 0.1, i = 0.0, d = 0.0, f = 0.0, buffer = 3.0;
	private DriveIO driveIO = DriveIO.getInstance();
	
	public MoveForward(DriveControl driveControl) {
		super(driveControl, Axis.X1, p, i, d, f, buffer);
		driveIO.resetEncoders();
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		setTarget(driveIO.getTranslationVector().getX());
		super.execute(x1, y1, x2, y2);
		
	}

}
