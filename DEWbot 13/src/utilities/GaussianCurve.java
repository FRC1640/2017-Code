package utilities;

public class GaussianCurve {
	private double mu, sigma, max;

	public GaussianCurve(double mu, double sigma){
		this.mu = mu;
		this.sigma = sigma;
	}
	
	public GaussianCurve(double mu, double sigma, double max){
		this.mu = mu;
		this.sigma = sigma;
		this.max = max;
	}
	
	public void setMax(double max){
		this.max = max;
	}
	
	public double getOutput(double x){
		return Math.pow(Math.E, -(Math.pow(x - (max / mu), 2)  / Math.pow(2 * (max / sigma), 2)));
	}
}

