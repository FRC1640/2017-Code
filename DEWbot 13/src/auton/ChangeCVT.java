package auton;


import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Controller.Button;
import constants.ControllerConstants;
import drive.DriveIO;

public class ChangeCVT implements AutonCommand{
	private String name;
	public enum Setting {SLOW, MEDIUM, HIGH};
	private Setting setting;
	
	public ChangeCVT(Setting setting, String name){
		this.setting = setting;
		this.name = name;
	}

	@Override
	public void execute() {
		switch(setting){
			case SLOW: {ControlInputs.getDriver().setButton(ControllerConstants.FAST_DRIVER, false); 
						ControlInputs.getDriver().setButton(ControllerConstants.SLOW_DRIVER, true); 
						ControlInputs.getDriver().setButton(ControllerConstants.MED_DRIVER, false); 
						break;}
			case MEDIUM: {ControlInputs.getDriver().setButton(ControllerConstants.FAST_DRIVER, false); 
						  ControlInputs.getDriver().setButton(ControllerConstants.SLOW_DRIVER, false); 
						  ControlInputs.getDriver().setButton(ControllerConstants.MED_DRIVER, true); 
							break;}
			case HIGH: {ControlInputs.getDriver().setButton(ControllerConstants.FAST_DRIVER, true); 
						ControlInputs.getDriver().setButton(ControllerConstants.SLOW_DRIVER, false); 
						ControlInputs.getDriver().setButton(ControllerConstants.MED_DRIVER, false); 
						break;}
		}
	}

	@Override
	public boolean isRunning() {
		return true;
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
