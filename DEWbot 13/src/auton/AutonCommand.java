package auton;

public interface AutonCommand { //interface for all auton commands (ex. drive, intake)
	void execute();
	
	boolean isRunning();
	
	boolean isInitialized();
	
	String getName();
	
	void reset();
}
