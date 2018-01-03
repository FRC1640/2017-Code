package drive;

import utilities.Utilities;

import com.ctre.CANTalon;

import constants.Constants;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;

public class Pivot {
	//private com.ctre.CANTalon drive, steer;
	private com.ctre.CANTalon drive, steer;

	private AnalogInput resolver;
	public PIDController anglePID;
	private boolean enabled;
	private double minVoltage, dVoltage;
	private double offset;
	private boolean flipDrive;
	private double targetAngle;
	private String id;
	private boolean velocityControl = false;
	private int encOffset;
	private int maxVel = 80000;
	private double setpoint;
	private int iterations;
	
	
	public Pivot(int driveChannel, int steerChannel, int resolverChannel, double minVoltage, double maxVoltage, double offset, String id){
		// setup all motors and sensors for the pivots
		drive = new CANTalon(driveChannel);
		steer = new CANTalon(steerChannel);
		
		resolver = new AnalogInput(resolverChannel){
			@Override
			public double pidGet(){
//				if(id == "fl")
//					System.out.println(getAngle() - targetAngle);
//				return getAngle() - targetAngle;
				return shortestAngleBetween();
			}
		};
		
		this.minVoltage = minVoltage;
		this.dVoltage = maxVoltage - minVoltage;
		this.offset = offset;
		
		// setup angle PID
		anglePID = new PIDController(1, 0, 0.01, 0, resolver, steer, 0.02);//0.9, 0.0001 //0.95, 0.0075
		anglePID.setOutputRange(-1, 1);
//		 anglePID.setContinuous();
		anglePID.setInputRange(-1, 1);
		anglePID.enable();
		anglePID.setSetpoint(0.0);
		
//		enabled = true;
		flipDrive = false;
		targetAngle = 0.0;
		
		this.id = id;
		drive.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		drive.setVoltageRampRate(0);
		
	}
	
	public double shortestAngleBetween() {
		double dAngle = targetAngle - getAngle();
		flipDrive = inRange(90, 270, Math.abs(dAngle));
		return (flipDrive) ? Math.sin(Math.toRadians(dAngle)) : -Math.sin(Math.toRadians(dAngle));
	}
	
	public void setTargetAngle(double angle){
		targetAngle = angle;
	}

	public void setDrive(double speed){
//		System.out.println(drive.getClosedLoopError() + " speed: " + speed);
		if (enabled){
			double temp = (flipDrive ? -speed : speed);
//			System.out.println("Vel control: " + velocityControl);
//			if(this.id == "bl")
//				System.out.println("Vel: " + getVelocity() + " : " + temp * maxVel);
			if(velocityControl && drive.getSetpoint() != 0 && drive.getSpeed() == 0 && enabled){
				iterations++;
			}
			else
				iterations = 0;
			if(iterations == 10){
				this.setVelocityControl(false);
				System.out.println("Encoder Broken. ID: " + id + " setpoint: " + drive.getSetpoint());
			}
			
			if(speed == 0){
				drive.configPeakOutputVoltage(+0.0f, -0.0f);
			}
			else{
				drive.configPeakOutputVoltage(+12.0f, -12.0f);
			}
			setpoint = !velocityControl?temp:temp*maxVel;
			drive.setSetpoint(!velocityControl?temp:temp*maxVel);
		}
		else {
			iterations = 0;
			drive.set(0);
		}
	}
	
	public double getAngle(){
		return ((360.0 * (resolver.getVoltage() - minVoltage) / dVoltage) + 360.0 - offset) % 360;
	}
	
	public double getVoltage(){
		return resolver.getVoltage();
	}

	public double getVelocity(){
		return drive.getSpeed();//drive.getEncVelocity();
	}
	
	public double getError(){
		return Math.abs(this.getPosition()) - Math.abs(DriveIO.getInstance().getPivots()[Constants.MASTER_PIVOT].getPosition());
//		return velocityControl ? drive.getSpeed() - setpoint : 999;
	}
	
	public double getPosition(){
		return drive.getEncPosition() - encOffset;//drive.getEncPosition();
	}
	
	public double getInches(){
		return ((getPosition()*2*Math.PI*2*(12.0/32))/64.0);
	}
	
	public void enable(){
		if (!enabled){
			enabled = true;
			anglePID.enable();
			if(velocityControl){
				drive.enable();
			}
		}
	}
	
	public void disable(){
		if (enabled){
			enabled = false;
			anglePID.disable();
			drive.set(0);
			anglePID.reset();
			if(velocityControl){
				drive.disable();
			}
		}
	}
	
	public static boolean inRange (double min, double max, double val) {
		return (val >= min && val <= max);
	}	
	
	public void resetEncoder(){
		encOffset = drive.getEncPosition();//drive.setEncPosition(0);
//		System.out.println("Reset: " + getPosition());
	}
	
	public String getID() {
		return id;
	}
	
	public void setVelocityControl(boolean velocity){
		velocityControl = velocity;
		if (velocity){
			drive.changeControlMode(CANTalon.TalonControlMode.Speed);
			drive.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
			drive.configEncoderCodesPerRev(64);
			drive.setEncPosition(0);
			drive.enableControl();
//			if(offset == 180)
//				drive.reverseOutput(true);
			drive.configPeakOutputVoltage(+12.0f, -12.0f);
			drive.configNominalOutputVoltage(+0.0f, -0.0f);
			drive.setPID(0.15, 0.0000, 0.2); //0.1, 0, 0.1, 0.0365
			drive.setF(0.0365);
		} else{
			drive.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		}
	}
	
	public boolean getVelocityControl(){
		return velocityControl;
	}
	
	public double getSetPoint(){
		return velocityControl ? setpoint : 999;
	}
	
	public double getAngleSetpoint(){
		return targetAngle;
	}
	
	public double getDrive(){
		return setpoint;
	}
	
	public boolean getFlipDrive(){
		return flipDrive;
	}
}

