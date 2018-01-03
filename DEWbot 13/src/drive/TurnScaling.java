package drive;

public class TurnScaling extends DriveControlDecorator{
	private DriveIO driveIO = DriveIO.getInstance();
	private Pivot[] pivots;
	private final double FAST_SPEED = 0.35, MAX_SPEED = 300;
	private final double SCALAR = 0.75;
	
	public TurnScaling(DriveControl driveControl){
		super(driveControl);
		pivots = driveIO.getPivots();
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		
//		double newX2 = x2 * (1 - Math.pow(Math.sqrt(1 - FAST_SPEED) * x2 / MAX_SPEED, 2));
		double newX2 = x2 * SCALAR;
//		System.out.println("New x2: " + newX2 + " Original X2: " + x2);
		super.execute(x1, y1, newX2, y2);
	}
}
