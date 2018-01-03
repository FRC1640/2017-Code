package auton.scripts;

import auton.DriveSpeed;
import auton.DriveTime;
import auton.TurnWheels;
import auton.WaitCommand;

public class Straight extends AutonScript{
	private static Straight mobility; 
	
	public static Straight getInstance(){
		if(mobility == null){
			mobility = new Straight();
		}
		return mobility;
	}
	
	private Straight(){
		script = new ScriptArrayList();
//		Straight
		script.add(new TurnWheels(0, "turnWheels"));

		script.add(new DriveSpeed(17000, 90, 0.25, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
	}
}
