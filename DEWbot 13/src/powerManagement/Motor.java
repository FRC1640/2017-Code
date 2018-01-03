package powerManagement;

import com.ctre.CANTalon;

public class Motor extends CANTalon{
	private static boolean stopped;

	
	public Motor(int deviceNumber) {
		super(deviceNumber);
	}
	
	@Override
	public void set(double speed){
		if(!stopped)
			super.set(speed);
		else
			super.set(0);
	}
	
	@Override
	public void disable(){
		stopped = true;
		super.disable();
	}
	
	@Override 
	public void enable(){
		stopped = false;
		super.enable();
	}
	
	public boolean isStopped(){
		return stopped;
	}

}
