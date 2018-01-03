package auton.scripts;

import auton.DriveGaussian;
import auton.TurnWheels;
import auton.WaitCommand;

public class DriveStraight extends AutonScript {
	private static DriveStraight driveStraight; 
	
	public static DriveStraight getInstance(){
		if(driveStraight == null){
			driveStraight = new DriveStraight();
		}
		return driveStraight;
	}
	
	private DriveStraight(){
		script = new ScriptArrayList();
		
		script.add(new TurnWheels(90, "turnWheels"));
		script.add(new DriveGaussian(200, 90, 0.5, "driveStraight"));
		script.add(new WaitCommand(script.get(script.indexOfName("driveStraight")), "waitDriveStraight"));
		
	}
}
