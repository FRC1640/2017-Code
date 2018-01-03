package auton.scripts;

import auton.DriveGaussian;
import auton.DriveSpeed;
import auton.Gear;
import auton.ManualTurnTimeOut;
import auton.SetFC;
import auton.Time;
import auton.TurnWheels;
import auton.WaitCommand;
import auton.WiggleServo;

public class GearRight extends AutonScript{
	private static GearRight gearRight; 
	
	public static GearRight getInstance(){
		if(gearRight == null){
			gearRight = new GearRight();
		}
		return gearRight;
	}
	
	private GearRight(){
		script = new ScriptArrayList();
		
		script.add(new TurnWheels(90, "turningWheels"));
		script.add(new SetFC(90, "setFC"));

		script.add(new Time(500, "time2"));
		script.add(new WaitCommand(script.get(script.indexOfName("time2")), "waitTime"));
		
		script.add(new DriveGaussian(193000, 0, 0.25, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
		script.add(new ManualTurnTimeOut(30.0, ManualTurnTimeOut.TurnType.ABSOLUTE, 3000, "testTurn"));
		script.add(new WaitCommand(script.get(script.indexOfName("testTurn")), "waitTurn"));

		script.add(new TurnWheels(0, "turningWheels2"));
		script.add(new WaitCommand(script.get(script.indexOfName("turningWheels2")), "waitTurnWheels2"));

		script.add(new Time(1000, "time3"));
		script.add(new WaitCommand(script.get(script.indexOfName("time3")), "waitTime3"));
		
		script.add(new DriveSpeed(100, 90, 0.15, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
//		script.add(new WiggleServo(5000, "wiggle"));
		
		script.add(new DriveSpeed(4500, 90, 0.25, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
		script.add(new Time(500, "time4"));
		script.add(new WaitCommand(script.get(script.indexOfName("time4")), "waitTime4"));
		
 		script.add(new Gear(true, "place"));
 		script.add(new Time(1000, "time3"));
		script.add(new WaitCommand(script.get(script.indexOfName("time3")), "waitTime3"));
		
		script.add(new DriveSpeed(6000, 270, 0.25, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
	}
}
