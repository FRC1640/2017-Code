package auton;

public class WaitCommand implements AutonCommand { //auton command to pause script until another command has been completed
	//this class allows for multitasking in auton scripts
	private AutonCommand command; //command to wait for
	private String name;
	
	public WaitCommand(AutonCommand command, String name){
		//record inputs
		this.command = command;
		this.name = name;
	}

	@Override
	public void execute() { }

	@Override
	public boolean isRunning() {
		return command.isRunning(); //this command only finishes when the command it holds is finished
	}

	@Override
	public boolean isInitialized() {
		return !command.isRunning(); //this command only initializes when the command it holds is finished 
		//(this prevents the script from moving to a new command)
	}
	
	public String getName(){
		return name;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof String){
			return o == name;
		}
		return false;
	}

	@Override 
	public void reset() {}
}
