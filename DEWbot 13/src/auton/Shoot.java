package auton;

import utilities.ControlInputs;
import utilities.Controller.Button;
import constants.ControllerConstants;
import drive.DriveIO;

public class Shoot implements AutonCommand {
	private String name;
	private boolean close, done, prevDoneAligning, firstIteration = true;
	
	public Shoot(boolean close, String name) {
		this.close = close;
		this.name = name;
	}

	@Override
	public void execute() {
		if(firstIteration){
			if(close){
				ControlInputs.getOp().setAxis(ControllerConstants.SHOOT_CLOSE_OP, 1.0);
			}
			else{
//				ControlInputs.getOp().setAxis(ControllerConstants.SHOOT_FAR_OP, 1.0);
				ControlInputs.getOp().setPOV(1);
			}
			firstIteration = false;
			System.out.println("Shooter set");
		}
		
//		boolean doneAligning = DriveIO.getInstance().getAligning();
//		if(doneAligning && !prevDoneAligning){
//			if(close){
//				ControlInputs.getDriver().setButton(Button.LEFT_BUMPER, false);
//			}
//			else{
//				ControlInputs.getDriver().setButton(Button.RIGHT_BUMPER, false);
//			}
//			System.out.println("Auton done shooting");
//		}
//		
//		prevDoneAligning = doneAligning;
	}

	@Override
	public boolean isRunning() {
		return !done;
	}

	@Override
	public boolean isInitialized() {
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void reset() {
		
	}

}
