package drive;


public abstract class DriveControlDecorator extends DriveControl {
	// Defines the interface for a decorator of DriveControl
	// Anything that extends this class can add functionality on top of other DriveControls.
	// Follows the Decorator design pattern.
	// It is a modular way of adding further drive functionality on top
		// of existing functionalities.
	
	// this is the original object that the decorator will add functionality on top of
	private DriveControl driveControl;
	
	public DriveControlDecorator(DriveControl driveControl){
		this.driveControl = driveControl;
	}
	
	public void execute(double x1, double y1, double x2, double y2){
		// default implementation is to execute the original object with no extra functionality
		driveControl.execute(x1, y1, x2, y2);
	}
	
	public void deconstruct(){
		driveControl.deconstruct();
	}
}
