package auton.scripts;

import auton.ChangeCVT;
import auton.ChangeCVT.Setting;
import auton.SonarDrive.Sonar;
import auton.Gear;
import auton.ShiftGear;
import auton.SonarDrive;
import auton.Time;
import auton.TurnWheels;
import auton.WaitCommand;

public class GearCenterShift extends AutonScript{
	private static GearCenterShift gearCenter; 
	
	public static GearCenterShift getInstance(){
		if(gearCenter == null){
			gearCenter = new GearCenterShift();
		}
		return gearCenter;
	}
	
	private GearCenterShift(){
		script = new ScriptArrayList();
		
		script.add(new TurnWheels(0, "turningWheels"));
		script.add(new ChangeCVT(Setting.MEDIUM, "cvt"));

		script.add(new Time(500, "time2"));
		script.add(new WaitCommand(script.get(script.indexOfName("time2")), "waitTime"));
		
		script.add(new SonarDrive(0.5, 30, 90, Sonar.FRONT, "initialApproach"));
		script.add(new WaitCommand(script.get(script.indexOfName("initialApproach")), "waitApproach"));
		
//		script.add(new ShiftGear("shiftGear"));
		script.add(new SonarDrive(0.25, 2, 90, Sonar.FRONT, "placingSpring"));
		script.add(new WaitCommand(script.get(script.indexOfName("placingSpring")), "waitPlacing"));
		
		script.add(new Gear(true, "test"));
		script.add(new WaitCommand(script.get(script.indexOfName("test")), "waitTime"));
		
		script.add(new Time(500, "time2"));
		script.add(new WaitCommand(script.get(script.indexOfName("time2")), "waitTime"));
		
		script.add(new SonarDrive(0.3, 40, 90, Sonar.FRONT, "approachSpring3"));
		script.add(new WaitCommand(script.get(script.indexOfName("approachSpring3")), "waitApproach"));

	}
}
