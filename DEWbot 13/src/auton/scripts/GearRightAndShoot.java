package auton.scripts;

import auton.Time;
import auton.DriveTime;
import auton.RoughTurn;
import auton.ShootCustom;
import auton.WaitCommand;

public class GearRightAndShoot extends AutonScript{
	private static GearRightAndShoot gearRightRed, gearRightBlue; 
	
	public static GearRightAndShoot getInstance(boolean red){
		if(red){
			if(gearRightRed == null){
				gearRightRed = new GearRightAndShoot(red);
			}
			return gearRightRed;
		}
		else{
			if(gearRightBlue == null){
				gearRightBlue = new GearRightAndShoot(red);
			}
			return gearRightBlue;
		}
	}
	
	private GearRightAndShoot(boolean red){
		script = new ScriptArrayList();
		script.addAll(GearRight.getInstance().getScript());
		
		if(!red){
			script.add(new RoughTurn(180, RoughTurn.TurnType.ABSOLUTE, "Turn to Wall"));
			script.add(new WaitCommand(script.get(script.indexOfName("Turn to Wall")), "waitDrive"));

			script.add(new DriveTime(2500, 180, 0.75, "driveTime"));
			script.add(new WaitCommand(script.get(script.indexOfName("driveTime")), "waitDrive"));
		}else{
			script.add(new RoughTurn(-30.0, RoughTurn.TurnType.ABSOLUTE, "turnToShoot"));
			script.add(new WaitCommand(script.get(script.indexOfName("turnToShoot")), "waitTurn"));
			
			script.add(new Time(1000, "time"));
			script.add(new WaitCommand(script.get(script.indexOfName("time")), "waitTime"));
			
			script.add(new ShootCustom(100, 2700, 5, "shooting"));
		}

	}
}
