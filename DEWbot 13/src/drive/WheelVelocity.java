package drive;

public class WheelVelocity extends DriveControlDecorator{
	private Pivot[] pivots;

	public WheelVelocity(DriveControl driveControl) {
		super(driveControl);
		pivots = DriveIO.getInstance().getPivots();
		for(Pivot p:pivots){
			p.setVelocityControl(true);
		
		}
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
//		for(Pivot p:pivots){
//			p.setVelocity(y1*250);
//			//System.out.println(p.getSetPoint());
//		}
		super.execute(x1, y1, x2, y2);
	}
	
	@Override
	public void deconstruct(){
		for(Pivot p:pivots){
			p.setVelocityControl(false);
		}
		System.out.println("In deconstructor");
	}

}
