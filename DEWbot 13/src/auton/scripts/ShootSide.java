package auton.scripts;


import auton.DriveGaussian;
import auton.ShootManual;
import auton.Time;
import auton.TurnWheels;
import auton.WaitCommand;

public class ShootSide extends AutonScript { //blank script
	private static ShootSide shootLeft, shootRight;

	public static ShootSide getInstance(boolean red){ //get singleton instance
		if(red){
			if(shootLeft == null)
				shootLeft = new ShootSide(true);
			return shootLeft;
		}
		if(shootRight == null)
			shootRight = new ShootSide(false);
		return shootRight;
	}
	
	
	private ShootSide(boolean red){
		script = new ScriptArrayList();
		
//		script.add(new Shoot(true, "shoot"));
//		script.add(new WaitCommand(script.get(script.indexOfName("shoot")), "waitShoot"));
		double angle = red ? 260 : 80;
		double wheelAngle = red ? -10 : 10;
		script.add(new TurnWheels(wheelAngle, "straight"));
		script.add(new ShootManual(true, true, "shooting"));
//		script.add(new SetFC(-angle + 90, "setFC"));

//		script.add(new Time(500, "time"));
//		script.add(new WaitCommand(script.get(script.indexOfName("time")), "waitTime"));
//		script.add(new Agitate(true, "Agitate"));
		script.add(new Time(10000, "time"));
		script.add(new WaitCommand(script.get(script.indexOfName("time")), "waitTime"));
//		script.add(new Shoot(false, "ShootingOff"));

		script.add(new DriveGaussian(250000, angle, 0.4, "Mobility"));
		script.add(new WaitCommand(script.get(script.indexOfName("Mobility")), "waitTime"));
	}
}
