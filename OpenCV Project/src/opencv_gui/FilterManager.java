package opencv_gui;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

public class FilterManager {
	public static final boolean gui = false, gear = true;
	private static ArrayList<Filter> filters;
//	public static String xmlFilePath = "/home/pi/OpenCV/test.xml", url = "http://admin:@10.16.40.183:80/video1.mjpg";
	public static String xmlFileGear = "/home/pi/OpenCV/gear.xml", urlGear = "http://admin:@10.16.40.183:80/video1.mjpg";
	public static String xmlFileBoiler = "/home/pi/OpenCV/boiler.xml", urlBoiler = "http://admin:@10.16.40.184:80/video1.mjpg";
	private static int iterations;
	private static long startTime;
	private static double[] processTimes;
	public static final int PIC_WIDTH = 320, PIC_HEIGHT = 240, PIC_WIDTH_GUI = 320, PIC_HEIGHT_GUI = 240;
	
	public static void main(String args[]){
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		int picWidth = gui ? PIC_WIDTH_GUI : PIC_WIDTH;
		int picHeight = gui ? PIC_HEIGHT_GUI : PIC_HEIGHT;
		Panel p;
		FileGenerator fileGenerator = FileGenerator.getInstance(gui);
		filters = new ArrayList<Filter>();
		filters.addAll(fileGenerator.loadFilters());
		
		if(gui){
			p = Panel.getInstance();
			p.generatePanel(filters.size(), fileGenerator.getInputs().size(), fileGenerator.getSliderNames()); 
			p.loadSliders(fileGenerator.getInputs());
		}
		
		//Mat mat = Highgui.imread("C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\20.jpg");
//		Mat mat = Mat.zeros(new Size(picWidth, picHeight), CvType.CV_8UC1);
//		filters.remove(filters.size() - 1); //filter contours
		
//		processTimes = new double[100];
		int begin = 0, end = 0;
		while(true){
//			startTime = System.nanoTime() / 1000000;
			Mat mat = Mat.zeros(new Size(picWidth, picHeight), CvType.CV_8UC1);
			for(int i = 0; i < filters.size(); i++){
				if(gui){
					if(i == 1){begin = 0; end = 5;}
					if(i == 2){begin = 6; end = 6;}
					if(i == 3){begin = 7; end = 14;} //TODO: better solution
					filters.get(i).setInputs(getInputs(p.getSliderValues(), begin, end));
				}
				
				try{
					filters.get(i).execute(mat, mat, picWidth, picHeight);
				}catch(Exception e){System.out.println("There was an exception executing filter #: " + i); e.printStackTrace();}
				
				mat = filters.get(i).getMat();
				if(gui){
					p.generateFile(filters);
					p.updateLabel(filters.get(i).getMat(), i, i == 0, PIC_WIDTH_GUI, PIC_HEIGHT_GUI);
				}
			}
			mat.release();
//			double delta = (System.nanoTime() / 1000000 - startTime);
//			processTimes[iterations] = delta;
//			iterations++;
//			if(iterations % 100 == 0){
//				double avg = 0;
//				for(int i = 0; i < processTimes.length; i++)
//					avg += processTimes[i];
//				System.out.println("Avg: " + avg);
//				iterations = 0;
//			}
		}
		
	}
	
	private static int[] getInputs(int[] allInputs, int beginIndex, int endIndex){
		int[] inputs = new int[endIndex - beginIndex + 1];
		for(int i = beginIndex; i <= endIndex; i++){
			inputs[i - beginIndex] = allInputs[i];
		}
		return inputs;
	}
}
