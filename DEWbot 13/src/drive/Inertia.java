package drive;

import drive.DriveControl;
import drive.DriveControlDecorator;

public class Inertia extends DriveControlDecorator { //drive decorator that accounts for the inertia of the robot, not currently in use
//	private double prevX1 = 0, prevY1 = 0, prevX2 = 0, prevY2 = 0;
	private double[] prevX1, prevY1, prevX2, prevY2;
	private final int AVG_SIZE = 5;
	private int i;
	
	public Inertia(DriveControl driveControl) {
		super(driveControl);
		prevX1 = new double[AVG_SIZE];
		prevY1 = new double[AVG_SIZE];
		prevX2 = new double[AVG_SIZE];
		prevY2 = new double[AVG_SIZE];
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
//		double newX1 = inertia(x1, prevX1);
//		double newY1 = inertia(y1, prevY1);
//		double newX2 = inertia(x2, prevX2);
//		double newY2 = inertia(y2, prevY2);
//		if(newY1 != 0)
//			System.out.println("newY1: " + newY1);
//		
//		prevX1 = x1;
//		prevY1 = y1;
//		prevX2 = x2;
//		prevY2 = y2;

		double newX1 = inertia(x1, getAverage(prevX1));
		double newY1 = inertia(y1, getAverage(prevY1));
		double newX2 = inertia(x2, getAverage(prevX2));
		double newY2 = inertia(y2, getAverage(prevY2));
		if(newY1 != 0)
			System.out.println("newY1: " + newY1);
		
		prevX1[i] = x1;
		prevY1[i] = y1;
		prevX2[i] = x2;
		prevY2[i] = y2;
		i++;
		if(i == AVG_SIZE)
			i = 0;
		
		super.execute(newX1, newY1, newX2, newY2);
	}
	
	private double inertia(double joystick, double prevJoy){
		//making the constants bigger will make the reaction to a change in joystick more drastic
		return (/*joystick == 0 ? 0 : */joystick + ((joystick - prevJoy) * (10 + (0.5 * Math.pow(Math.abs(joystick), 2)))));
		
	}
	
	private double getAverage(double[] array){
		double avg = 0;
		for(int i = 0; i < array.length; i++){
			avg += array[i];
		}
		return avg / array.length;
	}
}
