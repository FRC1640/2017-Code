package main;

import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class NTCoreTest {
	

	public static void main(String[] args){
		System.out.println(System.getProperty("java.library.path"));
		//System.loadLibrary("ntcore");
		NetworkTable.setClientMode();
		NetworkTable.setIPAddress("10.16.40.19");
		NetworkTable table = NetworkTable.getTable("OpenCV");
		table.putBoolean("bool", false);
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
			System.out.println(table.getNumber("test", 5));
		}
		
	}
}
