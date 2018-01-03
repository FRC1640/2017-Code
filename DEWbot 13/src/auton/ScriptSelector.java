
package auton;

import auton.scripts.Blank;
import auton.scripts.DriveStraight;
import auton.scripts.GearCenter;
import auton.scripts.GearCenterAndShoot;
import auton.scripts.GearLeft;
import auton.scripts.GearLeftAndShoot;
import auton.scripts.GearRight;
import auton.scripts.GearRightAndShoot;
import auton.scripts.HopperAndShoot;
import auton.scripts.ScriptArrayList;
import auton.scripts.ShootCenter;
import auton.scripts.ShootSide;
import auton.scripts.Straight;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ScriptSelector { //class that selects the correct script based on dashboard inputs
	private static ScriptSelector scriptSelector; //singleton instance
	private ScriptArrayList script = new ScriptArrayList();
	private ScriptRunner scriptRunner = ScriptRunner.getInstance();
	
	private String auton = "", alliance = "";
	
	private ScriptSelector(){
		
		//script = ExitRight.getInstance().getScript();
//		script.addAll(ExitRight.getInstance().getScript());
		//scriptRunner.setScript(script);
	}

	public static ScriptSelector getInstance() {
		if (scriptSelector == null)
			scriptSelector = new ScriptSelector();
		return scriptSelector;
	}

	public void update() {
		if(checkForChange()){
			script = null;

			boolean red = alliance.equals("Red");
			
			switch (auton) {
//				case "Blank": {script = Blank.getInstance().getScript(); break;}
				case "Mobility": {script = Straight.getInstance().getScript(); break;}
				case "Center Gear": {script = GearCenter.getInstance().getScript(); break;}
				case "Center Gear Shoot": {script = GearCenterAndShoot.getInstance(red).getScript(); break;}
				case "Left Gear": {script = GearLeft.getInstance().getScript(); break;}
				case "Left Gear Shoot": {script = GearLeftAndShoot.getInstance(red).getScript(); break;}
				case "Right Gear": {script = GearRight.getInstance().getScript(); break;}
				case "Right Gear Shoot": {script = GearRightAndShoot.getInstance(red).getScript(); break;}
				case "Shoot Side": {script = ShootSide.getInstance(red).getScript(); break;}
				case "Shoot Center": {script = ShootCenter.getInstance().getScript(); break;}
				case "Hopper Shoot": {script = HopperAndShoot.getInstance(red).getScript(); break;}
				case "Custom 1": {script = DriveStraight.getInstance().getScript(); break;}
				case "Custom 2": {break;}
				case "Custom 3": {break;}
				default: {break;}
			}
			if (script != null) {
				System.out.println("Script to be set: " + script);
				scriptRunner.setScript(script);
			}
			else {
				System.out.println("No script set");
			}
		}
	
	}
	
	private boolean checkForChange(){
		boolean changed = false;
		boolean error = false;
		
		String sdAlliance = SmartDashboard.getString("Alliance", "Error");
		if(!alliance.equals(sdAlliance)){
			if (sdAlliance.equals("Error")) error = true;
			else alliance = sdAlliance;
			changed = true;
		}
		
		String sdAuton = SmartDashboard.getString("Auton", "Error");

		if (!auton.equals(sdAuton)) {
			if (sdAuton.equals("Error")) error = true;
			else auton = sdAuton;
			changed = true;
		}
		
		if (error) {
			System.out.println("***Error Reading Auton Instructions***");
			return false;
		}
		
		return changed;
	}
}
