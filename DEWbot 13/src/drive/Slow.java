package drive;

public class Slow extends DriveControlDecorator {
	private final double FACTOR = 0.25;

	public Slow(DriveControl driveControl) {
		super(driveControl);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2) {
		super.execute(FACTOR*x1, FACTOR*y1, FACTOR*x2, FACTOR*y2);
	}
	
}
