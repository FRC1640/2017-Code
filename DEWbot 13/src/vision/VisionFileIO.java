package vision;

import utilities.FileIO;

public class VisionFileIO {
	private static boolean init, failed;
	public enum TargetType {BOILER, GEAR_PLACE, GEAR_LOAD};
	private static FileIO boiler, gearPlace, gearLoad;
	private static String boilerPath = "/home/lvuser/boiler.txt", gearPlacePath, gearLoadPath;
	private static double boilerTarget, gearPlaceTarget, gearLoadTarget;
	private static final double BOILER_DEFAULT = 240, PLACE_DEFAULT = 240, LOAD_DEFAULT = 240;

	public static double getTarget(TargetType targetType){
		if(!init){
			init();
			init = true;
		}
		switch(targetType){
			case BOILER:
				return boilerTarget;
			case GEAR_PLACE:
				return gearPlaceTarget;
			case GEAR_LOAD:
				return gearLoadTarget;
		}
		return 0;
	}
	
	public static void setTarget(TargetType targetType, double value){
		if(!init){
			init();
			init = true;
		}
		String valueString = new Double(value).toString();
		switch(targetType){
			case BOILER:
				boilerTarget = value;
				boiler.clearFile();
				boiler.write(valueString);
				break;
			case GEAR_PLACE:
				gearPlaceTarget = value;
				gearPlace.clearFile();
				gearPlace.write(valueString);
				break;
			case GEAR_LOAD:
				gearLoadTarget = value;
				gearLoad.clearFile();
				gearLoad.write(valueString);
				break;		
		}
	}
	
	private static void init(){
		boiler = new FileIO(boilerPath);
		gearPlace = new FileIO(gearPlacePath);
		gearLoad = new FileIO(gearLoadPath);
		
		if(boiler != null && boiler.readLine() != null && !boiler.readLine().equals(""))
			boilerTarget = new Double(boiler.readLine());
		else
			boilerTarget = BOILER_DEFAULT;
		
		if(gearPlace != null && !gearPlace.readLine().equals(""))
			gearPlaceTarget = new Double(gearPlace.readLine());
		else
			gearPlaceTarget = PLACE_DEFAULT;
		
		if(gearLoad != null && !gearLoad.readLine().equals(""))
			gearLoadTarget = new Double(gearLoad.readLine());
		else
			gearLoadTarget = LOAD_DEFAULT;
		
	}
	

}
