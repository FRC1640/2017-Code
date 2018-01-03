package drive;


import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.ControlInputs;
import utilities.Controller;
import utilities.Utilities;
import utilities.Controller.Axis;
import utilities.Controller.Button;
import constants.ControllerConstants;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DriveCore{ // manages the robot's drive
	
	private static DriveCore driveCore; // singleton instance
	
	private DriveIO driveIO = DriveIO.getInstance();	
	private final DriveControl DEFAULT_DRIVE = /*new FieldCentric(AdvancedCVT.getInstance());*/new GyroCorrection(new SlowOperatorCVT(AdvancedCVT.getInstance())); //new GyroCorrection(new TurnScaling(new SlowOperatorCVT(AdvancedCVT.getInstance()))); 
	private final DriveControl AUTON_DRIVE = new MoveVector(new RoughTurn(new Turn(new GyroCorrection(new CVTDrive(SwerveDriveBase.getInstance()))))); 
	private	DriveControl driveFunct = DEFAULT_DRIVE;
	private Controller driver = ControlInputs.getDriver();
	private int prevDriveState;
	private boolean prevX, prevAuton, gyro, prevGyro, reset, prevFC, fieldCentric = true;
	private NetworkTable table;
	private long startTime;
	private int gearNum = 0;
	private State prevState = State.DISABLED;
	private double prevYaw = 0;
	private long iterations = 0;
	
	private DriveCore(){
		driveIO.resetGyro();
		
		NetworkTable.setServerMode();
		NetworkTable.setIPAddress("roboRIO-1640-FRC.local");
		table = NetworkTable.getTable("Graphing");
		
		startTime = (long) (System.nanoTime() / 1000000.0);
		
	}
	
	public static DriveCore getInstance() {
		if (driveCore == null) {
			driveCore = new DriveCore();
		}
		return driveCore;
	}
	
	public void update(){
		iterations++;
//		if (iterations % 20 == 0) {
	//		System.out.println("Front Sonar Voltage: " + driveIO.getFrontSonarVoltage());
//			System.out.println("Front Sonar Inches: " + driveIO.getFrontSonarInches());
	//		System.out.println("Right Sonar Voltage: " + driveIO.getRightSonarVoltage());
//			System.out.println("Right Sonar Inches: " + driveIO.getRightSonarInches());
	//		System.out.println("Left Sonar Voltage: " + driveIO.getLeftSonarVoltage());
//			System.out.println("Left Sonar Inches: " + driveIO.getLeftSonarInches());
//			System.out.println(driveIO.getPivots()[2].getVelocity() + " : " + driveIO.getPivots()[2].getPosition());

//		}
			
//		table.putNumber("velocity0", Math.abs(driveIO.getPivots()[0].getPosition()));
//		table.putNumber("velocity1", Math.abs(driveIO.getPivots()[1].getPosition()));
//		table.putNumber("velocity2", Math.abs(driveIO.getPivots()[2].getPosition()));
//		table.putNumber("velocity3", Math.abs(driveIO.getPivots()[3].getPosition()));
		
		table.putNumber("velocity0", Math.abs(driveIO.getPivots()[0].getVelocity()));
		table.putNumber("velocity1", Math.abs(driveIO.getPivots()[1].getVelocity()));
		table.putNumber("velocity2", Math.abs(driveIO.getPivots()[2].getVelocity()));
		table.putNumber("velocity3", Math.abs(driveIO.getPivots()[3].getVelocity()));

		table.putNumber("setpoint", Math.abs(driveIO.getPivots()[0].getSetPoint()));
		table.putNumber("setpoint1", Math.abs(driveIO.getPivots()[1].getSetPoint()));
		table.putNumber("setpoint2", Math.abs(driveIO.getPivots()[2].getSetPoint()));
		table.putNumber("setpoint3", Math.abs(driveIO.getPivots()[3].getSetPoint()));
		
		table.putNumber("error0", Math.abs(driveIO.getPivots()[0].getError()));
		table.putNumber("error1", Math.abs(driveIO.getPivots()[1].getError()));
		table.putNumber("error2", Math.abs(driveIO.getPivots()[2].getError()));
		table.putNumber("error3", Math.abs(driveIO.getPivots()[3].getError()));
		
		table.putNumber("position0", Math.abs(driveIO.getPivots()[0].getPosition()));
		table.putNumber("position1", Math.abs(driveIO.getPivots()[1].getPosition()));
		table.putNumber("position2", Math.abs(driveIO.getPivots()[2].getPosition()));
		table.putNumber("position3", Math.abs(driveIO.getPivots()[3].getPosition()));

//
//		table.putNumber("position1", Math.abs(driveIO.getPivots()[0].getPosition()));
//		table.putNumber("position2", Math.abs(driveIO.getPivots()[1].getPosition()));
//		table.putNumber("position3", Math.abs(driveIO.getPivots()[2].getPosition()));
//		table.putNumber("position4", Math.abs(driveIO.getPivots()[3].getPosition()));
		table.putNumber("time", (System.nanoTime()-startTime));
//		SmartDashboard.putNumber("Time", System.nanoTime()-startTime);
//		SmartDashboard.putNumber("Graphing Variable", Math.abs(driveIO.getPivots()[1].getVelocity()));
		if(driveIO.getYaw() != 0 && !reset){
			reset = true;
			driveIO.resetGyro();
		}
		
		if(driver.getButton(ControllerConstants.RESET_GYRO_DRIVER)){
			driveIO.resetGyro();
			driveIO.resetFC();
			driveIO.resetEncoders();
			driveIO.resetDisplacement();
			
		}
		else {
			double jump = Math.abs(Utilities.shortestAngleBetween(driveIO.getFCYaw(), prevYaw));
			if (Math.abs(jump) > 50) {
//				driveIO.setFCsOffset(driveIO.getFCOffset()+jump);
				System.out.println("!!!Jump correction. New offset: " + driveIO.getFCOffset());
			}
		}
		prevYaw = driveIO.getFCYaw();

		double x1 = driver.getAxis(Axis.X1);
		double y1 = -driver.getAxis(Axis.Y1);
		double x2 = driver.getAxis(Axis.X2);
		double y2 = -driver.getAxis(Axis.Y2);
		
//		boolean fc = driver.getButton(ControllerConstants.FIELD_CENTRIC_DRIVER);
//		if(fc && !prevFC){
//			fieldCentric = !fieldCentric;
//			System.out.println("Changing FC: " + fieldCentric);
//
//		}
		fieldCentric=!driver.getButton(ControllerConstants.FIELD_CENTRIC_DRIVER);
		/*
		 * Priority : Boiler Aim, Gear Place Aim, Gear Load Aim, Drive Straight, Field Centric
		 */
		boolean turnWheel = driveIO.getTurnWheel();
		boolean shooterDrive = false;
		boolean boilerAim = driver.getButton(ControllerConstants.SHOOT_CLOSE_DRIVER) || driver.getButton(ControllerConstants.SHOOT_FAR_DRIVER);
		boolean gearPlace = driver.getPOV() == 0;
		boolean gearLoad = false;
		boolean driveStraight = Robot.getState() == State.AUTON;
//		boolean fieldCentric = driver.getButton(ControllerConstants.FIELD_CENTRIC_DRIVER) && !prevFC;
		int driveState = (turnWheel ? 64 : 0) | (shooterDrive ? 32: 0) |(boilerAim ? 16 : 0) | (gearPlace ? 8 : 0) | (gearLoad ? 4 : 0) | (driveStraight ? 2 : 0) | (fieldCentric ? 1 : 0);
		
		if(driveState != prevDriveState){
			driveFunct.deconstruct();
			if(driveState >= 64){
				driveFunct = TurnWheelDriveBase.getInstance();
			}
			else if (driveState >= 32) { //shooter drive
				driveFunct = new OperatorControl(new Slow(new ShooterForward(AdvancedCVT.getInstance())));
			}
			else if(driveState >= 16){ //boiler aim
				driveFunct = new BoilerAutonAlign(new Turn(new SlowOperatorCVT(AdvancedCVT.getInstance())));
			}else if(driveState >= 8){ //gear place
				System.out.println("In Straight");
				driveFunct = new WheelVelocity(new GyroCorrection(new FieldCentric(new SlowOperatorCVT(AdvancedCVT.getInstance()))));
			}else if(driveState >= 4){ //gear load
				driveFunct = new SimpleGearAlign(new MoveVector(new GyroCorrection(AdvancedCVT.getInstance())));
			}else if(driveState >=2){ //drive straight (auton)
				System.out.println("In Auton");
				driveFunct = new WheelVelocity(new MoveVector(new GyroCorrection(new RoughTurn(new Turn(new CVTDrive(SwerveDriveBase.getInstance())))))); //AUTON_DRIVE;//new GyroCorrection(new MoveVector(new RoughTurn(new Turn(new CVTDrive(SwerveDriveBase.getInstance()))))); //new WheelVelocity(new MoveVector(new RoughTurn(new Turn(new GyroCorrection(new CVTDrive(SwerveDriveBase.getInstance())))))); 
			}else if(driveState == 1){ //field centric
				driveFunct = new WheelVelocity(new TurnScaling(new GyroCorrection(new FieldCentric(new SlowOperatorCVT(AdvancedCVT.getInstance())))));
//				driveFunct = new WheelVelocity(new FieldCentric(new SlowOperatorCVT(AdvancedCVT.getInstance())));//new FieldCentric(new CVTDrive(SwerveDriveBase.getInstance()));
			}else{ //default
				driveFunct = DEFAULT_DRIVE;
			}
		}
		if(System.nanoTime() / 1000000.0 - startTime > 100){
			startTime = (long) (System.nanoTime() / 1000000.0);
		}
		prevGyro = gyro;
		
		driveFunct.execute(x1, y1, x2, y2);
		
		prevDriveState = driveState;
		
		if((Robot.getState() == State.AUTON || Robot.getState() == State.TELEOP) && prevState == State.DISABLED){
			for(Pivot p : driveIO.getPivots())
				p.enable();
		}
		if(Robot.getState() == State.DISABLED && (prevState == State.AUTON || prevState == State.TELEOP)){
			for(Pivot p : driveIO.getPivots())
				p.disable();
		}
		
		if(Robot.getState() == State.TELEOP && driver.getButton(Button.BUTTON_A))
			System.out.println(driveIO.getFCYaw() + " , " + driveIO.getFCOffset());
		prevState = Robot.getState();
//		prevFC = fc;
	}
	
}
