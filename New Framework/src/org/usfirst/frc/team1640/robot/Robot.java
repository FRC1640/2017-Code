package org.usfirst.frc.team1640.robot;

import org.usfirst.frc.team1640.controller.ControllerSubsystem;
import org.usfirst.frc.team1640.drive.DriveSubsystem;
import org.usfirst.frc.team1640.robot.states.AutonRobotState;
import org.usfirst.frc.team1640.robot.states.DisabledRobotState;
import org.usfirst.frc.team1640.robot.states.RobotState;
import org.usfirst.frc.team1640.robot.states.TeleopRobotState;

import edu.wpi.first.wpilibj.SampleRobot;

final class Robot extends SampleRobot {

	@Override 
	public void robotMain(){
		
		RobotState myRobotState;
		RobotState prevRobotState;
		
		// initialize subsystems that will run in separate threads
		ControllerSubsystem controllerSubsystem = new ControllerSubsystem();
		DriveSubsystem driveSubsystem = new DriveSubsystem();
		
		// start subsystems
		final int PERIOD = 20;
		controllerSubsystem.start(PERIOD);
		driveSubsystem.start(PERIOD);
		
		// initialize robot states and inject any necessary context through constructor
		AutonRobotState auton = new AutonRobotState();
		
		TeleopRobotState teleop = new TeleopRobotState(
				controllerSubsystem.getDriverControllerContext(),
				controllerSubsystem.getOperatorControllerContext());
		
		DisabledRobotState disabled = new DisabledRobotState();
		
		// default RobotStates
		prevRobotState = disabled;
		myRobotState = disabled;
		
		// the main control loop only manages high level robot states
		while (true){
			
			if (!isEnabled()) {
				myRobotState = disabled; // if the robot is not enabled, set the robot state to DISABLED
			}
			else {
				if (isAutonomous()) {
					myRobotState = auton; // if the robot is enabled and in auton mode, set the robot state to AUTON
				}
				else {
					myRobotState = teleop; // if the robot is enabled and is not in auton mode, set the robot state to TELEOP
				}
			}
			
			// if the state changes, give the old state a chance to end its processes
			if (prevRobotState != myRobotState) {
				prevRobotState.leave();
			}
			
			// execute the code for the robot's state
			myRobotState.execute();
			
			prevRobotState = myRobotState;
		}
		
	}
}