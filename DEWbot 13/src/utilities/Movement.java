package utilities;

public class Movement {
	private Vector vector;
	private double angle;
	
	public Movement(Vector vector, double angle) {
		this.vector = Vector.makeCartesian(vector.getX(), vector.getY());
		this.angle = angle;
	}
	
	public Vector getVector() {
		return vector;
	}
	
	public double getAngle() {
		return angle;
	}
}
