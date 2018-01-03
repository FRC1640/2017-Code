package auton.scripts;

import auton.DriveTime;
import auton.RoughTurn;
import auton.ShootCustom;
import auton.Time;
import auton.WaitCommand;

public class GearLeftAndShoot extends AutonScript{
	private static GearLeftAndShoot gearLeftRed, gearLeftBlue; 
	
	public static GearLeftAndShoot getInstance(boolean red){
		if(red){
			if(gearLeftRed == null){
				gearLeftRed = new GearLeftAndShoot(red);
			}
			return gearLeftRed;
		}
		else{
			if(gearLeftBlue == null){
				gearLeftBlue = new GearLeftAndShoot(red);
			}
			return gearLeftBlue;
		}
	}
	
	private GearLeftAndShoot(boolean red){
//		script = new ScriptArrayList();
		script.addAll(GearLeft.getInstance().getScript());
//		
//		double angle = red ? -60 : 30;
//		script.add(new Turn(angle, Turn.TurnType.ABSOLUTE, "turnToShoot"));
//		script.add(new WaitCommand(script.get(script.indexOfName("turnToShoot")), "waitTurn"));
//		
//		int hoodAngle = red ? 15 : 0;
//		int setpoint = red ? 3500 : 0;
//		double offset = red ? 0 : 0;
//		script.add(new ShootCustom(hoodAngle, setpoint, offset, "shooting"));
		
		if(red){
			script.add(new RoughTurn(180, RoughTurn.TurnType.ABSOLUTE, "Turn to Wall"));
			script.add(new WaitCommand(script.get(script.indexOfName("Turn to Wall")), "waitDrive"));

			script.add(new DriveTime(2500, 0, 0.75, "driveTime"));
			script.add(new WaitCommand(script.get(script.indexOfName("driveTime")), "waitDrive"));
		}else{
			script.add(new RoughTurn(230, RoughTurn.TurnType.ABSOLUTE, "turnToShoot"));
			script.add(new WaitCommand(script.get(script.indexOfName("turnToShoot")), "waitTurn"));
			
			script.add(new Time(500, "timeShoot"));
			script.add(new WaitCommand(script.get(script.indexOfName("timeShoot")), "waitShoot"));
			
			script.add(new ShootCustom(100, 2700, 2, "shooting"));
		}
	}
}
