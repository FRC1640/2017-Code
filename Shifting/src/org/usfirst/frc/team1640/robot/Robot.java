package org.usfirst.frc.team1640.robot;

import drive.Drive;
import edu.wpi.first.wpilibj.SampleRobot;


/*
 * TODO:
 * tune wheel pid
 * tune gyro pid
 * tune vel pid
 * test shifting code
 * test with vel pids
 * code and test with gyro correction
 */
public class Robot extends SampleRobot {
	public enum State {AUTON, TELEOP, DISABLED};
	private static State state;
	
	private Drive drive;
	
	public Robot() {
		drive = new Drive();
		drive.start(20);
	}
	
	
	@Override
	public void robotMain(){
		try{
			while(true){
				if(isAutonomous())
					state = State.AUTON;
				else if(isOperatorControl())
					state = State.TELEOP;
				else 
					state = State.DISABLED;
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	public static boolean isAuton(){
		return state == State.AUTON;
	}
	
	public static boolean isTeleop(){
		return state == State.TELEOP;
	}
	
	public static boolean isDisable(){
		return state == State.DISABLED;
	}
	
	public static State getState(){
		return state;
	}

}
