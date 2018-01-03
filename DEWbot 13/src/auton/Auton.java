package auton;

import utilities.ControlInputs;
import utilities.Controller.Axis;
import utilities.Controller.Button;
import utilities.Subsystem;


public class Auton extends Subsystem{ //class to run auton thread
	private ScriptSelector scriptSelector;
	private ScriptRunner scriptRunner;

	@Override
	public void update() { //update method of thread
		scriptSelector.update();
		scriptRunner.execute();
//		ControlInputs.getDriver().setAxis(Axis.Y1, 1);
//		ControlInputs.getDriver().setButton(Button.BUTTON_B, false);
	}

	@Override
	public void init() { //init method of thread
		scriptSelector = ScriptSelector.getInstance();
		scriptRunner = ScriptRunner.getInstance();
	}


}
