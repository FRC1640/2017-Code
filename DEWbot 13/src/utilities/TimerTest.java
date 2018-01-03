package utilities;

import java.util.ArrayList;

public class TimerTest {
	private static TimerTest timerTest;
	private ArrayList<Double> iterationDiff;
	private long startTime;
	
	private TimerTest(){
		iterationDiff = new ArrayList<Double>();
	}
	
	public static TimerTest getInstance(){
		if(timerTest == null){
			timerTest = new TimerTest();
		}
		return timerTest;
	}
	
	public void update(){
		iterationDiff.add(new Double(System.nanoTime() / 1000000.0 - startTime));
		startTime = (long) (System.nanoTime() / 1000000.0);
		
		if(ControlInputs.getDriver().getPOV() == 270){
			for(Double d : iterationDiff){
				System.out.println(d);
			}
			System.out.println("--------------------------------");
			iterationDiff.clear();
		}
	}
	

}
