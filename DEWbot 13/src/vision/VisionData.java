package vision;

import java.util.ArrayList;
import java.util.Arrays;

import drive.DriveIO;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionData {
	private static VisionData visionData;
	
	private ArrayList<VisionTarget> boilerTargets, gearTargets;
	private NetworkTable boilerTable, gearTable;
	private boolean initialized;
	private double[] defaultValue = {};
	private int iterations = 0;


	private VisionData(){
		//NetworkTable.setClientMode();
	}
	
	public static VisionData getInstance(){
		if(visionData == null)
			visionData = new VisionData();
		return visionData;
	}
	
	public void update(){
		if(!initialized){
			try{
				boilerTable = NetworkTable.getTable("OpenCVBoiler");
				gearTable = NetworkTable.getTable("OpenCVGear");
				initialized = true;
			}catch(Exception e){e.printStackTrace();}
		}
		else{
			boilerTargets = createTargets(boilerTable);
			gearTargets = createTargets(gearTable);
//			if (++iterations % 100 == 0) System.out.println("updating");
		}
		LEDRing.getInstance().setState(Relay.Value.kOn);
//		System.out.println(DriveIO.getInstance().getSonarInches());
	}
	
//	public ArrayList<VisionTarget> getAllTargets(){
//		return targets;
//	}
	
	public ArrayList<VisionTarget> getBoilerTargets(){
		if(boilerTargets == null)
			return new ArrayList<VisionTarget>();
		return boilerTargets;
	}
	
	public ArrayList<VisionTarget> getGearTargets(){
		if(gearTargets == null)
			return new ArrayList<VisionTarget>();
		return gearTargets;
	}
	
	private ArrayList<VisionTarget> createTargets(NetworkTable table){
		double[] centerX = table.getNumberArray("x", defaultValue);
		double[] centerY = table.getNumberArray("y", defaultValue);
		double[] width = table.getNumberArray("width", defaultValue);
		double[] height = table.getNumberArray("height", defaultValue);
		double[] area = table.getNumberArray("area", defaultValue); //TODO find a way to load these synchronously
		ArrayList<VisionTarget> t = new ArrayList<VisionTarget>();
		int[] length = new int[]{centerX.length, centerY.length, width.length, height.length, area.length};
		Arrays.sort(length);
		int smallest = length[0];
		for(int i = 0; i < smallest; i++){
			if(centerY[i] != 1)
				t.add(new VisionTarget(centerX[i], centerY[i], width[i], height[i], area[i]));
		}
		
		return t;
	}
	
	public int getBoilerFrameCount(){
		return (int) boilerTable.getNumber("frameCount", -1);
	}
	
	public int getGearFrameCount(){
		return (int) gearTable.getNumber("frameCount", -1);
	}
	
}
