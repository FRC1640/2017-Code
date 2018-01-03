package org.usfirst.frc.team1640.robot;

import intake.Intake;
import pdpManagement.PDPManager;
import servoTuning.ServoTuning;
import shooter.Shooter;
import vision.Vision;
import agitator.Agitator;
import auton.Auton;
import climber.Climber;
import drive.DriveMaster;
import edu.wpi.first.wpilibj.SampleRobot;
import gearIntake.GearIntake;

public class Robot extends SampleRobot {
	public enum State {AUTON, TELEOP, DISABLED};
	private static State state;
	
	private Auton auton;// = new Auton();
	private DriveMaster drive;// = new DriveMaster();
	private Vision vision;
	private GearIntake gearIntake;
	private Climber climber;
	private Intake intake;
	private Shooter shooter;
	private Agitator agitator;
//	private Auton auton;
	private PDPManager pdpManager;
	private ServoTuning servo;
		
	public Robot() {
		// initialize and start all subsystems
		//    each subsystem manages a section of the robot code in a separate thread
		//    this prevents exceptions and other issues from interfering with other systems
		auton = new Auton();
		auton.start(20); // 20 milliseconds between iterations
		drive = new DriveMaster();
		drive.start(20);
		vision = new Vision();
		vision.start(20);
		intake = new Intake();
		intake.start(20);
		gearIntake = new GearIntake();
		gearIntake.start(20);
		climber = new Climber();
		climber.start(20);
		shooter = new Shooter();
		shooter.start(20);
		agitator = new Agitator();
		agitator.start(20);
		pdpManager = new PDPManager();
		pdpManager.start(90);
		
//		servo = new ServoTuning();
//		servo.start(20);
	}

	@Override 
	public void robotMain(){
		try{
			// the main control loop only manages robot states
			while (true){
				if(isAutonomous() && isEnabled()){
					state = State.AUTON;
				}
				else if(isOperatorControl() && isEnabled()){
					state = State.TELEOP;
				}
				else{
					state = State.DISABLED;
				}
			}
		}catch(Exception e){e.printStackTrace();}
	}
	
	public static State getState(){
		return state;
	}
}
