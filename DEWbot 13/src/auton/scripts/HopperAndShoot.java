package auton.scripts;

import auton.DriveGaussian;
import auton.DriveTime;
import auton.RoughTurn;
import auton.ShootCustom;
import auton.Time;
import auton.WaitCommand;

public class HopperAndShoot extends AutonScript{
	private static HopperAndShoot hopperAndShootRed, hopperAndShootBlue; 
	
	public static HopperAndShoot getInstance(boolean red){
		if(red){
			if(hopperAndShootRed == null){
				hopperAndShootRed = new HopperAndShoot(true);
			}
			return hopperAndShootRed;
		}
		else{
			if(hopperAndShootBlue == null){
				hopperAndShootBlue = new HopperAndShoot(false);
			}
			return hopperAndShootBlue;
		}
	}
	
	private HopperAndShoot(boolean red){
		script = new ScriptArrayList();

		double angle = red ? 0 : 180;
		script.add(new DriveGaussian(200000, angle, 0.5, "approach"));
		script.add(new WaitCommand(script.get(script.indexOfName("approach")), "waitApproach"));
		
//		script.add(new DriveGaussian(10000, -90, 0.5, "bumpHopper"));
//		script.add(new WaitCommand(script.get(script.indexOfName("bumpHopper")), "waitBump"));
//		
//		script.add(new Time(1000, "timeToFill"));
//		script.add(new WaitCommand(script.get(script.indexOfName("timeToFill")), "waitFill"));
//		
//		script.add(new DriveGaussian(10000, 90, 0.75, "leaveHopper"));
//		script.add(new WaitCommand(script.get(script.indexOfName("leaveHopper")), "waitLeave"));
//		
//		if (!red) {
//			script.add(new RoughTurn(-160, RoughTurn.TurnType.ABSOLUTE, "turnIfBlue"));
//			script.add(new WaitCommand(script.get(script.indexOfName("turnIfBlue")), "waitTurnIfBlue"));
//		}
//		else {
//			System.out.println("Turning Red");
//			script.add(new RoughTurn(-10, RoughTurn.TurnType.ABSOLUTE, "turnIfRed"));
//			script.add(new WaitCommand(script.get(script.indexOfName("turnIfRed")), "waitTurnIfRed"));
//		}
//		
//		script.add(new Time(500, "wait"));
//		script.add(new WaitCommand(script.get(script.indexOfName("wait")), "waitWait"));
//		
//		int hoodAngle = red ? 115 : 115;
//		int setpoint = red ? 2650 : 2650;
//		double offset = red ? 2 : 2;
//		script.add(new ShootCustom(hoodAngle, setpoint, offset, "shooting"));

		
		//Old 10/25/2017
//		Sonar direction = red ? Sonar.LEFT : Sonar.RIGHT;
//		double angle = red ? 0 : 180;
//		script.add(new DriveTime(2000, angle, 0.5, "approach"));
//		script.add(new WaitCommand(script.get(script.indexOfName("approach")), "waitApproach"));
//		
//		script.add(new DriveTime(1750, -90, 0.5, "bumpHopper"));
//		script.add(new WaitCommand(script.get(script.indexOfName("bumpHopper")), "waitBump"));
//		
//		script.add(new Time(1000, "timeToFill"));
//		script.add(new WaitCommand(script.get(script.indexOfName("timeToFill")), "waitFill"));
//		
//		script.add(new DriveTime(500, 90, 0.75, "leaveHopper"));
//		script.add(new WaitCommand(script.get(script.indexOfName("leaveHopper")), "waitLeave"));
//		
//		if (!red) {
//			script.add(new RoughTurn(-160, RoughTurn.TurnType.ABSOLUTE, "turnIfBlue"));
//			script.add(new WaitCommand(script.get(script.indexOfName("turnIfBlue")), "waitTurnIfBlue"));
//		}
//		else {
//			System.out.println("Turning Red");
//			script.add(new RoughTurn(-10, RoughTurn.TurnType.ABSOLUTE, "turnIfRed"));
//			script.add(new WaitCommand(script.get(script.indexOfName("turnIfRed")), "waitTurnIfRed"));
//		}
//		
//		script.add(new Time(500, "wait"));
//		script.add(new WaitCommand(script.get(script.indexOfName("wait")), "waitWait"));
//		
//		int hoodAngle = red ? 115 : 115;
//		int setpoint = red ? 2650 : 2650;
//		double offset = red ? 2 : 2;
//		script.add(new ShootCustom(hoodAngle, setpoint, offset, "shooting"));

	}
}
