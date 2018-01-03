package powerManagement;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class MotorsManager {
	private static MotorsManager motors;
	
	private static ArrayList<Motor> talons;
	private PowerDistributionPanel pdp;
	
	public static MotorsManager getInstance(){
		if(motors == null)
			motors = new MotorsManager();
		return motors;
	}
	
	private MotorsManager(){
		talons = new ArrayList<Motor>();
		pdp = new PowerDistributionPanel(0);
	}
	
	public static Motor createMotor(int channelID){
		getInstance();
		Motor motor = new Motor(channelID);
		talons.add(motor);
		return motor;
	}
	
	public static Motor getMotor(int channelID){
		getInstance();
		for(Motor m : talons){
			if(m.getDeviceID() == channelID)
				return m;
		}
		return null;
	}
	
	public static boolean isStopped(int channelID){
		return getMotor(channelID).isStopped();
	}
	
	public void update(){
		/* Regulation example: 
		if(pdp.getVoltage() >= LIMIT)
			getMotor(0).disable();
		*/
	}
	
	

}
