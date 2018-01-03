package auton;

import constants.ControllerConstants;
import utilities.ControlInputs;

public class ShootManual implements AutonCommand {
	private String name;
	private boolean close, done, prevDoneAligning, firstIteration = true, on;
	
	public ShootManual(boolean close, boolean on, String name) {
		this.close = close;
		this.name = name;
		this.on = on;
	}

	@Override
	public void execute() {
		if(firstIteration){
			double value = on ? 1 : 0;
			if(close){
				ControlInputs.getOp().setAxis(ControllerConstants.SHOOT_CLOSE_OP, value);
			}
			else{
//				ControlInputs.getOp().setAxis(ControllerConstants.SHOOT_FAR_OP, value);
				ControlInputs.getOp().setPOV(1);
			}
			firstIteration = false;
			System.out.println("Shooter set");
		}
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
