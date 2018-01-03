package utilities;


import org.usfirst.frc.team1640.robot.Robot;
import edu.wpi.first.wpilibj.Joystick;


public class Controller {
	private Joystick joystick;
	public enum Button {BUTTON_A, BUTTON_B, BUTTON_X, BUTTON_Y, LEFT_BUMPER, RIGHT_BUMPER, BACK, START, LEFT_JOY, RIGHT_JOY};
	public enum Axis {X1, Y1, LEFT_TRIGGER, RIGHT_TRIGGER, X2, Y2};			
	
	public Controller(int joystickPort){
		joystick = new Joystick(joystickPort){
			@Override //deadband axis
			public double getRawAxis(int axis){
				if (axis == 0 || axis == 1) {
					return Utilities.deadband2(super.getRawAxis(0), -super.getRawAxis(1), 0.23)[axis];
				} else if (axis == 4 || axis == 5) {
					return Utilities.deadband2(super.getRawAxis(4), -super.getRawAxis(5), 0.23)[axis-4];
				} else {
					return Utilities.deadband(super.getRawAxis(axis), 0.23);
				}
			}
		};
	}
	private boolean A, B, X, Y, start, back, leftBumper, rightBumper, leftJoy, rightJoy;
	private double X1, Y1, X2, Y2, leftTrigger, rightTrigger;
	private int pov = -1;
	
	public boolean getButton(Button button){
		if(Robot.isAuton()){
			switch(button){
				case BUTTON_A: {return A;}
				case BUTTON_B: {return B;}
				case BUTTON_X: {return X;}
				case BUTTON_Y: {return Y;}
				case START: {return start;}
				case BACK: {return back;}
				case LEFT_BUMPER: {return leftBumper;}
				case RIGHT_BUMPER: {return rightBumper;}
				case LEFT_JOY: {return leftJoy;}
				case RIGHT_JOY: {return rightJoy;}
			}
		}
		return joystick.getRawButton(button.ordinal() + 1);
	}
	
	public double getAxis(Axis axis){
		if(Robot.isAuton()){
			switch(axis){
				case X1: {return X1;}
				case Y1: {return Y1;}
				case X2: {return X2;}
				case Y2: {return Y2;}
				case LEFT_TRIGGER: {return leftTrigger;}
				case RIGHT_TRIGGER: {return rightTrigger;}
			}
		}
		return joystick.getRawAxis(axis.ordinal());
	}
	
	public int getPOV(){
		if(Robot.isAuton()){
			return pov;
		}
		return joystick.getPOV();
	}
	
	public void setButton(Button button, boolean value){
		if(Robot.isAuton()){
			switch(button){
				case BUTTON_A: {A = value; break;}
				case BUTTON_B: {B = value; break;}
				case BUTTON_X: {X = value; break;}
				case BUTTON_Y: {Y = value; break;}
				case START: {start = value; break;}
				case BACK: {back = value; break;}
				case LEFT_BUMPER: {leftBumper = value; break;}
				case RIGHT_BUMPER: {rightBumper = value; break;}
			}
		}
	}
	
	public void setAxis(Axis axis, double value){
		if(Robot.isAuton()){
			switch(axis){
				case X1: {X1 = value; break;}
				case Y1: {Y1 = value; break;}
				case X2: {X2 = value; break;}
				case Y2: {Y2 = value; break;}
				case LEFT_TRIGGER: {leftTrigger = value; break;}
				case RIGHT_TRIGGER: {rightTrigger = value; break;}
			}
		}
	}
	
	public void setPOV(int value){
		if(Robot.isAuton()){
			pov = value;
		}
	}
}
