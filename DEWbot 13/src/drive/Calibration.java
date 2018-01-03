package drive;

public class Calibration extends DriveControlDecorator{
	private double[] max, min = {4,4,4,4};
	private boolean changed;
	private Pivot[] pivots;
	private long startTime;

	public Calibration(DriveControl driveControl) {
		super(driveControl);
		pivots = DriveIO.getInstance().getPivots();
		max = new double[4];
		startTime = (long) (System.nanoTime() / 1000000.0);
	}
	
	@Override 
	public void execute(double x1, double y1, double x2, double y2){
		for(int i = 0; i < pivots.length; i++){
			if(pivots[i].getVoltage() > max[i]){
				max[i] = pivots[i].getVoltage();
				changed = true;
			}
			if(pivots[i].getVoltage() < min[i]){
				min[i] = pivots[i].getVoltage();
				changed = true;
			}
		}
		
		if(/*System.nanoTime() / 1000000.0 - startTime > 1000 &&*/ changed){
			changed = false;
			startTime = (long) (System.nanoTime() / 1000000.0);
			
			System.out.println("----------------------");
			System.out.println("Max FL: " + max[0]);
			System.out.println("Max FR: " + max[1]);
			System.out.println("Max FL: " + max[2]);
			System.out.println("Max FR: " + max[3] + "\n");

			System.out.println("Min FL: " + min[0]);
			System.out.println("Min FR: " + min[1]);
			System.out.println("Min FL: " + min[2]);
			System.out.println("Min FR: " + min[3]);
		}

		super.execute(x1, y1, x2, y2);
	}

}
