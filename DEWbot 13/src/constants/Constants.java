package constants;

public class Constants {
	//DRIVE
	public static final double DRIVE_ENCODER_COUNTS_PER_ROTATION = 64;
	public static final double DRIVE_WHEEL_RADIUS = 2.0; //Inches
	public static final double DRIVE_WHEEL_CIRCUMFERENCE = 2*Math.PI*DRIVE_WHEEL_RADIUS; //Inches
	//DIMENSIONS
	public static final double ROBOT_LENGTH = 29; //Inches
	public static final double ROBOT_WIDTH = 33; //Inches
	public static final double ROBOT_RADIUS = Math.sqrt(Math.pow(ROBOT_WIDTH, 2) + Math.pow(ROBOT_LENGTH, 2));
	public static final double ROBOT_WIDTH_TO_RADIUS_RATIO = ROBOT_WIDTH / ROBOT_RADIUS;
	public static final double ROBOT_LENGTH_TO_RADIUS_RATIO = ROBOT_LENGTH / ROBOT_RADIUS;
	//BUTTONS
	public static final int JS_BUTTON_A = 1;
	public static final int JS_BUTTON_B = 2;
	public static final int JS_BUTTON_X = 3;
	public static final int JS_BUTTON_Y = 4;
	public static final int JS_BUTTON_L = 5;
	public static final int JS_BUTTON_R = 6;
	public static final int JS_BUTTON_BACK = 7;
	public static final int JS_BUTTON_START = 8;
	public static final int JS_BUTTON_LEFT_STICK = 9;
	public static final int JS_BUTTON_RIGHT_STICK = 10;
	//AXES
	public static final int JS_AXIS_LEFT_STICK_X = 0; //-1 to 1
	public static final int JS_AXIS_LEFT_STICK_Y = 1; //-1 to 1
	public static final int JS_AXIS_LEFT_TRIGGER = 2; //0 to 1
	public static final int JS_AXIS_RIGHT_TRIGGER = 3; //0 to 1
	public static final int JS_AXIS_RIGHT_STICK_X = 4; //-1 to 1
	public static final int JS_AXIS_RIGHT_STICK_Y = 5; //-1 to 1
	//POV
	public static final int JS_POV_N = 0; //North
	public static final int JS_POV_NE = 45; //North East
	public static final int JS_POV_E = 90; //East
	public static final int JS_POV_SE = 135; //South East
	public static final int JS_POV_S = 180; //South
	public static final int JS_POV_SW = 225; //South West
	public static final int JS_POV_W = 270; //West
	public static final int JS_POV_NW = 315; //North West
	
	public static final double SHOOTING_HORIZONTAL_FOV = 47;
	public static final double SHOOTING_VERTICAL_FOV = 480;
	public static final double SHOOTING_PIC_WIDTH = 640;
	public static final double SHOOTING_PIC_HEIGHT = 480;
	
	public static final double GEAR_HORIZONTAL_FOV = 57.8;
	public static final double GEAR_VERTICAL_FOV = 37.8;
	public static final double GEAR_PIC_WIDTH = 300;//320;
	public static final double GEAR_PIC_HEIGHT = 200;//240;
	
	public static final int MASTER_PIVOT = 1;
}
