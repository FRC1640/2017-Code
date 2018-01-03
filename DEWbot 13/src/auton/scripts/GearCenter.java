package auton.scripts;

import auton.DriveGaussian;
import auton.DriveSpeed;
import auton.Gear;
import auton.ShiftGear;
import auton.Time;
import auton.TurnWheels;
import auton.WaitCommand;

public class GearCenter extends AutonScript{
	private static GearCenter gearCenter; 
	
	public static GearCenter getInstance(){
		if(gearCenter == null){
			gearCenter = new GearCenter();
		}
		return gearCenter;
	}
	
	private GearCenter(){
		script = new ScriptArrayList();
		
		script.add(new TurnWheels(0, "straight"));
		script.add(new ShiftGear(true, "shift"));
		script.add(new Time(1000, "wait1"));
		script.add(new WaitCommand(script.get(script.indexOfName("wait1")), "wait"));
		
		script.add(new DriveGaussian(180000, 90, 0.23, "one")); //13000 for drive speed
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	

		script.add(new Gear(true, "place"));
		script.add(new Time(500, "wait2"));
		script.add(new WaitCommand(script.get(script.indexOfName("wait2")), "wait"));

		script.add(new DriveSpeed(5000, 270, 0.25, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
		script.add(new Gear(false, "place"));
	}
}
