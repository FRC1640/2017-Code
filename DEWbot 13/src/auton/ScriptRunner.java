package auton;

import java.util.ArrayList;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import drive.DriveIO;
import auton.scripts.ScriptArrayList;

public class ScriptRunner implements AutonCommand{ //class in charge of running the auton scripts
	private ScriptArrayList prevScript = new ScriptArrayList();
	private ScriptArrayList script = new ScriptArrayList();
	private int position;
	private ArrayList<AutonCommand> executing = new ArrayList<AutonCommand>();
	private String name;
	private static ScriptRunner autonRunner;
	private boolean prevAuton;
	
	public static ScriptRunner getInstance(){ //get singleton instance
		if(autonRunner == null){
			autonRunner = new ScriptRunner("name");
			return autonRunner;
		}
		return autonRunner;
	}
	
	private ScriptRunner(String name){
		this.name = name;
	}

	@Override
	public void execute() {
		if(Robot.getState() == State.AUTON){
			if(!prevAuton){
				DriveIO.getInstance().resetGyro();
			}
			else {
				if(position < script.size() - 1 && script.get(position).isInitialized() ){ //if the previous command is initialized, move on to the next one
					AutonCommand a = script.get(position + 1);
					executing.add(a); //add new command to be executed
					System.out.println("Position: " + position + " Adding " + a);
					position++;
				}
				for(AutonCommand command: executing){ //execute all commands
					command.execute();
				}
				cleanUp(); //remove any finished commands
			}
		}
		prevAuton = Robot.getState() == State.AUTON;
	}

	@Override
	public boolean isRunning() { 
		return !(position == script.size() - 1 && executing.isEmpty()); //script is finished when all commands have been executed
	}

	@Override
	public boolean isInitialized() {
		return isRunning();
	}
	
	private void cleanUp(){ //remove any commands that are done running
		for(int i = 0; i < executing.size(); ){
			if(!executing.get(i).isRunning()){ 
				System.out.println("Removing: " + executing.get(i));
				executing.remove(i);
			}
			else
				i++;
		}
	}
	
	public String getName(){
		return name;
	}
	
	public void setScript(ScriptArrayList script){ //method for other classes to change the script
		if(script != null){
			this.script = script; //save new script
			executing.clear(); //clear previous executing commands
			position = 0; //reset position
			
			if(script.size() >= 1) { 
				executing.add(script.get(0)); //add first command
			}
			
			//if the script is new from the previous script, print it out for debugging purposes
//			boolean printScript = false;
//			if(prevScript.size() == script.size()) {
//				for(int i = 0; i < prevScript.size(); i++) {
//					if(prevScript.get(i) != script.get(i)) printScript = true;
//				}
//			} else printScript = true;
//			System.out.print(printScript ? "The script set is : " + script + "\n" : "");
			
			prevScript = script;
		}
		else{System.out.println("Null script");}
	}
	
	public void reset(){
		executing.clear(); //clear previous executing script
		position = 0; //reset position
		for(AutonCommand ac : script) { //reset each command in the script 
			ac.reset();
		}
		executing.add(this.script.get(0)); //add first command
	}
}
