package auton.scripts;

import auton.DriveGaussian;
import auton.Gear;
import auton.WaitCommand;

public class ShootCenter extends AutonScript{
	private static ShootCenter instance; 
	
	public static ShootCenter getInstance(){
		if(instance == null){
			instance = new ShootCenter();
		}
		return instance;
	}
	
	private ShootCenter(){
		script = new ScriptArrayList();

//		script.add(new Shoot(false, "Shoot"));
		script.add(new DriveGaussian(1000, 0, 0.5, "one"));
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
		script.add(new Gear(true, "place"));
		
		script.add(new DriveGaussian(1500, 0, 0.5, "one"));
		script.add(new WaitCommand(script.get(script.indexOfName("one")), "waitApproach"));	
		
		script.add(new Gear(false, "place"));
	
	}
}
