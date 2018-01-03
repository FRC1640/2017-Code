package utilities;

public class Vector {
	protected double angle, magnitude, x, y;
	
	public Vector() {
		x = 0;
		y = 0;
		angle = 0;
		magnitude = 0;
	}
		
	public static Vector makeCartesian(double x, double y) {
		Vector v = new Vector();
		v.x = x;
		v.y = y;
		v.updateFromCartesian();
		return v;
	}
	
	public static Vector makePolar(double angle, double magnitude) {
		Vector v = new Vector();
		v.angle = constrainAngle(angle);
		v.magnitude = magnitude;
		v.updateFromPolar();
		return v;
	}
	
	public static double constrainAngle(double angle) {
		return angle;//(angle - 180.0)%360.0 + 180.0;
	}
	
	public static Vector average(Vector[] vectors) {
		Vector sum = new Vector();
		
		for (int i = 0; i < vectors.length; i++) {
			sum.add(vectors[i]);
		}
		
		return sum.divide(vectors.length);
	}
	
	public Vector add(Vector v) {
		return makeCartesian(x+v.x, y+v.y);
	}
	
	public Vector subtract(Vector v) {
		return makeCartesian(x-v.x, y-v.y);
	}
	
	public double dotProduct(Vector v) {
		return x*v.x + y*v.y;
	}
	
	public Vector multiply(double a) {
		return makeCartesian(a*x, a*y);
	}
	
	public Vector divide(double a) {
		return makeCartesian(x/a, y/a);
	}
	
	public Vector rotate(double theta) {
		return makePolar(angle+theta, magnitude);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public double getMagnitude() {
		return magnitude;
	}
	
	public Vector clone() {
		return makeCartesian(x, y);
	}
	
	private void updateFromCartesian() {
		angle = Math.toDegrees(Math.atan2(y, x));
		magnitude = Math.sqrt(Math.pow(x, 2.0)+ Math.pow(y, 2.0));
	}
	
	private void updateFromPolar() {
		x = magnitude*Math.cos(Math.toRadians(angle));
		y = magnitude*Math.sin(Math.toRadians(angle));
	}
}
