package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;

public abstract class AutonScript {//abstract class that holds a script (or a list of commands to complete in auton)
	protected ScriptArrayList script = new ScriptArrayList();
	
	public ScriptArrayList getScript() {
		ScriptArrayList mScript = new ScriptArrayList();
		
		for (int i = 0; i < script.size(); i++) {
			mScript.add(script.get(i));
		}
		
		return mScript;
	}

}
