package pdpManagement;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

public class PDPManagerIO {
	private static PDPManagerIO instance;
	PowerDistributionPanel pdp;
	double [] currents;
	int iterations;
	
	private PDPManagerIO() {
		pdp = new PowerDistributionPanel(0);
		currents = new double[16];
		iterations = 0;
	}
	
	public static PDPManagerIO getInstance() {
		if (instance == null) {
			instance = new PDPManagerIO();
		}
		return instance;
	}
	
	public void update() {
		for (int i = 0; i < 16; i++) {
			currents[i] = pdp.getCurrent(i);
		}
		iterations++;
	}
	
	public double getCurrent(int channel) {
		return currents[channel];
	}
	
	public int getIteration() {
		return iterations;
	}
}
