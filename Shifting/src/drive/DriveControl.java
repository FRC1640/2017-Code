package drive;



public abstract class DriveControl {
	// abstract base class for DriveControls.
	// Follows the Decorator design pattern.
	// Serves as a base on which DriveControlDecorators can be added for combinations of functionality.
		
	// all DriveControls need an execute method with four arguments
	protected abstract void execute(double x1, double y1, double x2, double y2);
	
	protected void deconstruct(){}
}
