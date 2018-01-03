package auton.scripts;

import auton.ManualTurn;
import auton.ManualTurnTimeOut;
import auton.Shoot;
import auton.Time;
import auton.WaitCommand;

public class GearCenterAndShoot extends AutonScript{
	private static GearCenterAndShoot gearCenterRed, gearCenterBlue; 
	
	public static GearCenterAndShoot getInstance(boolean red){
		if(red){
			if(gearCenterRed == null){
				gearCenterRed = new GearCenterAndShoot(red);
			}
			return gearCenterRed;
		}
		else{
			if(gearCenterBlue == null){
				gearCenterBlue = new GearCenterAndShoot(red);
			}
			return gearCenterBlue;
		}
	}
	
	private GearCenterAndShoot(boolean red){
		script = new ScriptArrayList();
		script.addAll(GearCenter.getInstance().getScript());
		
		double angle = red ? 215 : -25;
		script.add(new ManualTurnTimeOut(angle, ManualTurnTimeOut.TurnType.ABSOLUTE, 3000, "turnToShoot"));
		script.add(new WaitCommand(script.get(script.indexOfName("turnToShoot")), "waitTurn"));
		
		script.add(new Time(500, "timeShoot"));
		script.add(new WaitCommand(script.get(script.indexOfName("timeShoot")), "waitShoot"));
		
		script.add(new Shoot(false, "shooting"));
	}
}
