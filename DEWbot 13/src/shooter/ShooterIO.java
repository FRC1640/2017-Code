package shooter;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.ControlInputs;
import utilities.Controller;

import com.ctre.CANTalon;

import constants.ControllerConstants;
import constants.PortConstants;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class ShooterIO {
	private static ShooterIO shooterIO;
	
	private CANTalon master, slave1, slave2;
	private final double P = 0.9, I = 0, D = 40.0, F = 0.05515;//I = Math.pow(10.0, -5.0), D = 12.0, F = 0.050055;
	private final double RPM_BUFFER = 500, RPM_SHOOT = 2500; //shooter setpoint 2500;
	private final double CLOSE_SETPOINT = 2800 /*2350*/, FAR_SETPOINT = 2800 /*3250*/;//Super far: 3500
	private double shootingSpeed = RPM_SHOOT;
	
	private Controller op;
	
	private boolean isReady = false;
	private boolean isEnabled = false;
	
	private long startTime = 0;
	private NetworkTable table;
	
	private boolean prevButtonB;
	
	private int autonSetpoint, autonAngle;
	
	public static ShooterIO getInstance(){
		if(shooterIO == null)
			shooterIO = new ShooterIO();
		return shooterIO;
	}
	
	private ShooterIO(){
		
		startTime = System.nanoTime();
		
		NetworkTable.setServerMode();
		NetworkTable.setIPAddress("roboRIO-1640-FRC.local");
		table = NetworkTable.getTable("Graphing");
		
		master = new CANTalon(PortConstants.MotorShooterMasterID);
		master.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		master.configEncoderCodesPerRev(512);
		master.setEncPosition(0);
		master.enableControl();
		master.changeControlMode(CANTalon.TalonControlMode.Speed);
		master.setCloseLoopRampRate(0.0);
		master.configPeakOutputVoltage(+12.0f, -12.0f);
		master.configNominalOutputVoltage(+0.0f, -0.0f);
		master.reverseSensor(false);
		
		slave1 = new com.ctre.CANTalon(PortConstants.MotorShooterSlave1ID);
		slave1.changeControlMode(CANTalon.TalonControlMode.Follower);
		slave1.set(master.getDeviceID());
		slave1.reverseOutput(false);
		
		slave2 = new com.ctre.CANTalon(PortConstants.MotorShooterSlave2ID);
		slave2.changeControlMode(CANTalon.TalonControlMode.Follower);
		slave2.set(master.getDeviceID());
		slave2.reverseOutput(false);

		master.setP(P);
		master.setI(I);
		master.setD(D);
		master.setF(F);
		
		op = ControlInputs.getOp();
	}
	
	public void update(){
		
		//Use Matt's Dashboard for graphing
//		table.putNumber("velocity", master.getSpeed());
//		table.putNumber("percentage", master.get());
//		table.putNumber("current", master.getOutputCurrent());
//		table.putNumber("time", System.nanoTime()-startTime);
		
		double setPoint = 0.0;
		
		boolean closeShoot = ControlInputs.getOp().getButton(ControllerConstants.SHOOT_CLOSE_SLOW_OP) || 
				ControlInputs.getDriver().getButton(ControllerConstants.SHOOT_CLOSE_DRIVER) ||
				ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_CLOSE_OP) > 0.75;
		boolean farShoot = ControlInputs.getOp().getButton(ControllerConstants.SHOOT_FAR_SLOW_OP) || 
				ControlInputs.getDriver().getButton(ControllerConstants.SHOOT_FAR_DRIVER) ||
//				ControlInputs.getOp().getAxis(ControllerConstants.SHOOT_FAR_OP) > 0.75;
				ControlInputs.getOp().getPOV() == 1;

		//shooter hood
		if(closeShoot){
			if(Robot.getState() == State.AUTON && autonSetpoint != 0){
				ShooterHoodIO.getInstance().setAngle(autonAngle);
				setPoint = 0; //autonSetpoint;
			}
			else{
				ShooterHoodIO.getInstance().setDistance(true);
				ShooterHoodIO.getInstance().resetAngle();
				setPoint = CLOSE_SETPOINT;
			}
		}
		else if(farShoot){
			ShooterHoodIO.getInstance().setDistance(false);
			ShooterHoodIO.getInstance().resetAngle();
			setPoint = FAR_SETPOINT;
		}
		else if(Robot.getState() == State.AUTON){
			setPoint = 0;// CLOSE_SETPOINT;
		}
		else{
			setPoint = 0;
			master.configPeakOutputVoltage(+0.0f, -0.0f);
		}
		
		if(closeShoot || farShoot || Robot.getState() == State.AUTON){
			master.configPeakOutputVoltage(+12.0f, -12.0f);
			if(Math.abs(master.getSpeed() - setPoint) < RPM_BUFFER){
				isReady = true;
			}
		}
		else{
			isReady = false;
		}
		master.set(setPoint);
	}
	
	public boolean getReady() {
		return isReady;
	}
	
	public void setShootingSpeed(double shootingSpeed) {
		this.shootingSpeed = shootingSpeed;
	}
	
	public void setShootingSettings(int shootingSpeed, int hoodAngle){
		this.autonSetpoint = shootingSpeed;
		this.autonAngle = hoodAngle;
	}
	
	public void resetShootingSpeed() {
		this.shootingSpeed = RPM_SHOOT;
	}
}
