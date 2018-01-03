package utilities;

import java.util.Arrays;

import constants.Constants;
import drive.Pivot;

public class Utilities {
	
	public static double deadband(double value, double limit){ //ignore values below a limit and adjust other values to compensate
		boolean neg = value < 0;
		value = Math.abs(value);
		if(value < limit) //if value is below limit, return 0
			return 0;
		else //adjust values to compensate
			return ((neg) ? -1.0 : 1.0) * Math.pow((value - limit) / (1-limit), 2);
	}
	
	public static double[] deadband2 (double value1, double value2, double limit){ //same as deadband but with 2 values returned in an array
		if (magnitude(value1, value2) < limit) { return new double[] {0.0, 0.0 }; } 
		else {
			double newMagnitude = deadband(magnitude(value1, value2), limit);
			double theta = Math.atan2(value2, value1);
			double[] values = {Math.cos(theta) * newMagnitude, Math.sin(theta) * newMagnitude};
			return values;
		}
	}
	
	public static double magnitude(double x, double y){ //pythagorean therom
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public static double angle(double x, double y){
		return Math.toDegrees(Math.atan2(-x, -y)) + 180;
	}
	
	public static double minAbs(double[] arr) {
		double minVelocity = Math.abs(arr[0]);
		for(double d : arr)
			if(d != 0)
				minVelocity = Math.min(minVelocity, Math.abs(d));
		return minVelocity;
	}
	
	public static double shortestAngleBetween(double current, double target){ //find shortest distance between two gyro angles
		double distance = current % 360 - target;
		double shortestDistance = distance < 0 ? (360 + distance) : (distance - 360); //ensure that the 359-0 gap is counted for
		return (Math.abs(distance) >= 180 ? shortestDistance : distance); //choose shortest distances
	}
	
	public static double relativeToAbsolute(double current, double angle){
		double absAngle = current + angle;
		absAngle = (absAngle + 360) % 360;
		return absAngle;
	}
	
	public static double mean(double[] arr) {
		double sum = 0;
		
		for(int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
		
		return sum/arr.length;
	}
	
	public static double median(double[] arr) {
		double[] arr2;
		
		arr2 = Arrays.copyOf(arr, arr.length);
		Arrays.sort(arr2);
		
		return (arr2.length % 2 == 0) ? arr2[arr2.length/2] : (arr2[arr2.length/2]+arr2[arr2.length/2+1])/2.0;
	}
}