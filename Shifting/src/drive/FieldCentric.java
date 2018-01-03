package drive;

public class FieldCentric extends DriveControlDecorator {

	public FieldCentric(DriveControl driveControl) {
		super(driveControl);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2) {
		double[] joysticks = fieldCentric(x1, y1, x2);
		super.execute(joysticks[0], joysticks[1], joysticks[2], y2);
	}
	
	private double[] fieldCentric(double x1, double y1, double x2) {
		double temp;
		DriveIO driveIO = DriveIO.getInstance();
		temp = y1*Math.cos(Math.toRadians(driveIO.getYaw() ) ) - x1*Math.sin(Math.toRadians(driveIO.getYaw() ) );
		x1 =  y1*Math.sin(Math.toRadians(driveIO.getYaw() ) ) + x1*Math.cos(Math.toRadians(driveIO.getYaw() ) );
		y1 = temp;
		double[] joysticks = {x1, y1, x2};
		return joysticks;
	}

}
