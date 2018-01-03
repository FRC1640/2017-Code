package opencv_new;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public class OpenCV_Main {
	private static final boolean networkTable = false;
	

	private static String filePathGear = "test3.xml", filePathBoiler = "test4"
			+ ".xml";
	
	public static void main(String[] args){
		 System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


		LinkedHashMap<String, String[]> gearInputs = FileIO.getFilters(filePathGear);
//		LinkedHashMap<String, String[]> boilerInputs = FileIO.getFilters(filePathBoiler);

		Algorithm gear = new Algorithm(gearInputs);
//		Algorithm boiler = new Algorithm(boilerInputs);
		
		gear.start(40);
//		boiler.start(40);
	}

}
