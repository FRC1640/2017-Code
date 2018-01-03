package drive;

import utilities.ControlInputs;
import utilities.Controller;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;

public class PandaDrive extends DriveControlDecorator {
	private DriveIO driveIO;
	
	private double DEAD_BAND = 0.05;
	
	private double heading = 0;
	private double targetOrientation = 0;
	
	private double HEADING_TURN_SPEED = 1;
	private double SNAP_PERCENTAGE = 0.25;
	private double SNAP_BUFFER = 1.0;
	
	private double prevYaw = 0;
	private double prevLT = 0;
	private double prevRT = 0;
	
	private PIDSourceDouble shortestAngle;
	private PIDOutputDouble snapTurnSpeed;
	private PIDController orientationSnapPID;

	public PandaDrive(DriveControl driveControl) {
		super(driveControl);
		prevYaw = driveIO.getYaw();
		shortestAngle = new PIDSourceDouble(); 
		snapTurnSpeed = new PIDOutputDouble(); 
		orientationSnapPID = new PIDController(0.01, 0, 0, shortestAngle, snapTurnSpeed, 0.02); 
		orientationSnapPID.setOutputRange(-0.5, 0.5);
		orientationSnapPID.enable();
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2) {
		double X1 = 0, Y1 = 0, X2 = 0, Y2 = 0;
		
		double lt = ControlInputs.getDriver().getAxis(Controller.Axis.LEFT_TRIGGER);
		double rt = ControlInputs.getDriver().getAxis(Controller.Axis.RIGHT_TRIGGER);
		
		double yaw = driveIO.getYaw();
		
		//falling edge of triggers
		if (lt - rt <= DEAD_BAND && prevLT - prevRT > DEAD_BAND) {
			double orientation = yaw - heading;
			
			//determine nearest multiple of 90 to snap the orientation to
			if (Math.signum(lt - rt) > 0) {
				if (orientation % 90 > SNAP_PERCENTAGE*90.0) {
					targetOrientation = Math.ceil(orientation/90.0)*90.0;
				}
				else {
					targetOrientation = Math.floor(orientation/90.0)*90.0;
				}
			}
			else if (Math.signum(lt - rt) < 0) {
				if (orientation % 90 > (1-SNAP_PERCENTAGE)*90) {
					targetOrientation = Math.ceil(orientation/90.0)*90.0;
				}
				else {
					targetOrientation = Math.floor(orientation/90.0)*90.0;
				}
			}
			else {
				targetOrientation = 0;
			}
			targetOrientation %= 360;
			//clear error in PID to begin snapping
			orientationSnapPID.reset();
		}
		// if triggers are pressed
		else if (lt - rt > DEAD_BAND) {
			// turn the robot
			X2 = lt-rt;
			// change heading with right joystick
			if (Math.abs(x2) > DEAD_BAND) {
				heading += x2 * HEADING_TURN_SPEED;
			}
		}
		else {
			double angle = Utilities.shortestAngleBetween(driveIO.getYaw(), heading + targetOrientation);
			// PID to the snap angle
			if (Math.abs(angle) > SNAP_BUFFER) {
				shortestAngle.setValue(angle);
				X2 = snapTurnSpeed.getValue();
				// change heading with right joystick
				if (Math.abs(x2) > DEAD_BAND) {
					heading += x2 * HEADING_TURN_SPEED;
				}
			}
			// Finished Snapping
			else if (Math.abs(x2) > DEAD_BAND) {
				// turn the robot, setting heading based on gyro
				X2 = x2;
				heading += yaw-prevYaw;
			}
		}
		
		heading %= 360;
		
		// Math to move forward along heading
		X1 = -y1*Math.sin(Math.toRadians(driveIO.getYaw() - heading) ) + x1*Math.cos(Math.toRadians(driveIO.getYaw() - heading) );
		Y1 = y1*Math.cos(Math.toRadians(driveIO.getYaw() - heading) ) + x1*Math.sin(Math.toRadians(driveIO.getYaw() - heading) );
		
		super.execute(X1, Y1, X2, Y2);
		
		prevYaw = yaw;
		prevLT = lt;
		prevRT = rt;
	}

}
