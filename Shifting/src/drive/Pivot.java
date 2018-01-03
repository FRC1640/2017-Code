package drive;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDController;

public class Pivot {
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
				return shortestAngleBetween();
			}
		};
		
		this.minVoltage = minVoltage;
		this.dVoltage = maxVoltage - minVoltage;
		this.offset = offset;
		
		anglePID = new PIDController(1.63, 0, 0.01, 0, resolver, steer, 0.02);// 1 0 0.01
		anglePID.setOutputRange(-1, 1);
		anglePID.setInputRange(-1, 1);
		anglePID.enable();
		anglePID.setSetpoint(0.0);
		
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
		if (enabled){
			double temp = (flipDrive ? -speed : speed);			
			if(speed == 0){
				drive.configPeakOutputVoltage(+0.0f, -0.0f);
			}
			else{
				drive.configPeakOutputVoltage(+12.0f, -12.0f);
			}
			setpoint = 0;//!velocityControl ? temp : temp * maxVel;
			drive.setSetpoint(setpoint);
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
		return drive.getSpeed();
	}
	
	public double getPosition(){
		return drive.getEncPosition() - encOffset;
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
		encOffset = drive.getEncPosition();
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
			drive.configPeakOutputVoltage(+12.0f, -12.0f);
			drive.configNominalOutputVoltage(+0.0f, -0.0f);
			drive.setPID(0.15, 0.0000, 0.2); 
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

