package auton.scripts;

import auton.DriveGaussian;
import auton.DriveSpeed;
import auton.Gear;
import auton.ManualTurn;
import auton.ManualTurn.TurnType;
import auton.SetFC;
import auton.Time;
import auton.TurnWheels;
import auton.WaitCommand;
import auton.WiggleServo;

public class GearLeft extends AutonScript{
	private static GearLeft gearLeft; 
	
	public static GearLeft getInstance(){
		if(gearLeft == null){
			gearLeft = new GearLeft();
		}
		return gearLeft;
	}
	
	private GearLeft(){
		script = new ScriptArrayList();
	
		script.add(new TurnWheels(90, "turningWheels"));
		script.add(new SetFC(270, "setFC"));

		script.add(new Time(500, "time2"));
		script.add(new WaitCommand(script.get(script.indexOfName("time2")), "waitTime"));
		
		script.add(new DriveGaussian(190500, 180, 0.25, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
		script.add(new ManualTurn(-33.0, TurnType.ABSOLUTE, "testTurn"));
		script.add(new WaitCommand(script.get(script.indexOfName("testTurn")), "waitTurn"));
		
		script.add(new TurnWheels(0, "turningWheels2"));
		script.add(new WaitCommand(script.get(script.indexOfName("turningWheels2")), "waitTurnWheels2"));
	
		script.add(new Time(1500, "time3"));
		script.add(new WaitCommand(script.get(script.indexOfName("time3")), "waitTime3"));
		
		script.add(new DriveSpeed(100, 90, 0.15, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
		script.add(new WiggleServo(5000, "wiggle"));
		
		script.add(new DriveSpeed(3750, 90, 0.25, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
		script.add(new Gear(true, "place"));
		script.add(new Time(1000, "time3"));
		script.add(new WaitCommand(script.get(script.indexOfName("time3")), "waitTime3"));
		
		script.add(new DriveSpeed(6000, 270, 0.25, "one")); //10000
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
	}
}
