package shooter;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import utilities.ControlInputs;
import utilities.Controller.Button;
import utilities.FileIO;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class ShooterDataTest {
	private com.ctre.CANTalon wheel, wheel2;
	
	private double maxSpeed, maxError, prevSpeed, prevError, minError, prevMinError;
	private long startTime;
	private boolean firstIteration = true;
	
	private NetworkTable table;
	
	private double p, i, d, f;
	private double setPoint = 2800;
	
	private PowerDistributionPanel pdp;

	private State prevState;
	private boolean y = false;
	private boolean prevY = false;
	private FileIO log;
	
	public ShooterDataTest(){
		wheel = new com.ctre.CANTalon(3);
		wheel.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
		wheel.configEncoderCodesPerRev(512);
		wheel.setEncPosition(0);
		wheel.reverseOutput(false);
		wheel.enableControl();
		wheel.configPeakOutputVoltage(+12.0f, -12.0f);
		wheel.configNominalOutputVoltage(+0.0f, -0.0f);
		wheel.setCloseLoopRampRate(0);
		
		wheel2 = new com.ctre.CANTalon(4);
		wheel2.changeControlMode(CANTalon.TalonControlMode.Follower);
		wheel2.set(wheel.getDeviceID());
		
		
		pdp = new PowerDistributionPanel(0);
		
		NetworkTable.setServerMode();
		NetworkTable.setIPAddress("roboRIO-1640-FRC.local");
		table = NetworkTable.getTable("Robot");
//		table.addTableListener("setpoint", new ITableListener() {
//
//			@Override
//			public void valueChanged(ITable arg0, String arg1, Object arg2,
//					boolean arg3) {
//				setPoint = table.getNumber("setpoint", 0);
//				System.out.println("hello");
//			}
//			
//		}, true);
//		table.addTableListener("p", new ITableListener() {
//
//			@Override
//			public void valueChanged(ITable arg0, String arg1, Object arg2,
//					boolean arg3) {
//				p = table.getNumber("p", 0);
//				wheel.setP(p);
//			}
//			
//		}, true);
//		table.addTableListener("i", new ITableListener() {
//
//			@Override
//			public void valueChanged(ITable arg0, String arg1, Object arg2,
//					boolean arg3) {
//				i = table.getNumber("i", 0);
//				wheel.setI(i);
//			}
//			
//		}, true);
//			table.addTableListener("d", new ITableListener() {
//
//			@Override
//			public void valueChanged(ITable arg0, String arg1, Object arg2,
//					boolean arg3) {
//				d = table.getNumber("d", 0);
//				wheel.setD(d);
//			}
//			
//		}, true);
//		table.addTableListener("f", new ITableListener() {
//
//			@Override
//			public void valueChanged(ITable arg0, String arg1, Object arg2,
//					boolean arg3) {
//				f = table.getNumber("f", 0);
//				wheel.setF(f);
//			}
//			
//		}, true);
	}
	
	private int iterator = 0;
	
	public void update(){
		
		iterator++;
		
		table.putNumber("velocity", wheel.getSpeed());
		table.putNumber("percentage", wheel.get());
		table.putNumber("current", wheel.getOutputCurrent());
		table.putNumber("time", iterator*10);
//		System.out.println(wheel.getEncPosition());
		
		boolean yPressed = ControlInputs.getDriver().getButton(Button.BUTTON_Y);
		if (yPressed && !prevY) {
			y = !y;
		}
		//System.out.println(y);
		boolean x = ControlInputs.getDriver().getButton(Button.BUTTON_X);
		
		boolean a = ControlInputs.getDriver().getButton(Button.BUTTON_A);
		
		maxSpeed = Math.max(maxSpeed, wheel.getSpeed());
		
		if (Robot.getState() == State.TELEOP && prevState == State.DISABLED) {
			log = new FileIO("/home/lvuser/log.csv");
			System.out.println("New File");
			wheel.setEncPosition(0);
			log.write("***NEW RUN***\n");
			log.write("Setpoint: " + setPoint + ","
					+ "P: " + wheel.getP() + ","
					+ "I: " + wheel.getI() + ","
					+ "D: " + wheel.getD() + ","
					+ "F: " + wheel.getF() + ","
					+ "IZone: " + wheel.getIZone()
					+ "\n");
			log.write("Velocity (RPM), Time\n");
		}
		
		if(y) {
			if(firstIteration){
				startTime = System.nanoTime();
				firstIteration = false;
			}
			wheel.changeControlMode(CANTalon.TalonControlMode.Speed);
			wheel.set(setPoint);
			System.out.println("running");
			
//			long dt = (System.nanoTime() - startTime) / 1000000;
//			String str = wheel.getSpeed() + ", " + dt + "\n";
//			//startTime = System.nanoTime();
//			//System.out.println(str);
//			log.write(str);
//			log.flush();
			
			maxError = Math.max(maxError, wheel.getClosedLoopError());
			minError = Math.min(minError, wheel.getClosedLoopError());
		}
		else if (a) {
			firstIteration = true;
			wheel.changeControlMode(TalonControlMode.PercentVbus);
			wheel.set(0.2);
		}
		else if (x) {
			firstIteration = true;
			wheel.changeControlMode(TalonControlMode.PercentVbus);
			wheel.set(1.0);
		}
		else{
			firstIteration = true;
			wheel.changeControlMode(TalonControlMode.PercentVbus);
			wheel.set(0);
		}
		
		prevSpeed = maxSpeed;
		prevError = maxError;
		prevMinError = minError;
		prevState = Robot.getState();
		prevY = yPressed;
	}
}
