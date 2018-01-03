package drive;

public class EncoderTestDriveBase extends DriveControl{
	private static EncoderTestDriveBase encoderTest;
	private final double TARGET = 160;
	
	public static EncoderTestDriveBase getInstance(){
		if(encoderTest == null){
			encoderTest = new EncoderTestDriveBase();
		}
		return encoderTest;
	}
	
	private EncoderTestDriveBase(){}
	
	@Override
	protected void execute(double x1, double y1, double x2, double y2) {
		for(Pivot p : DriveIO.getInstance().getPivots()){
			if(Math.abs(p.getInches()) < TARGET){
				p.setDrive(1);
				System.out.println("ID: " + p.getID() + " Inches: " + p.getInches());
			}
			else
				p.setDrive(0);
			
		}
	}

}
