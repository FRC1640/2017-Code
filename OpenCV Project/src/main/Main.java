package main;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class Main{
	static enum FILTER_TYPE {AREA, WIDTH, HEIGHT, PERIMETER};

	
	public static void main(String[] args) {
		//Get Image
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//VideoCapture vid = new VideoCapture("http://root:root@10.16.40.181/jpg/image.jpg?size=3");
		Mat image = Highgui.imread("C:\\Users\\Laura\\Pictures\\OpenCV\\RealFullField\\0.jpg");
		//Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HLS);
		
		image.convertTo(image, -1, 2.2, 50);
		Highgui.imwrite("C:\\Users\\Laura\\Pictures\\OpenCV\\test2.png", image);

		//HSL Threshold
		Core.inRange(image, new Scalar(50,50,50), new Scalar(255, 255, 255), image);
		
		//Dialate
		Imgproc.dilate(image, image, Mat.ones(3, 3, CvType.CV_8UC1));
		
		//Find and display contours
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		Imgproc.drawContours(image, contours, -1, new Scalar(255));
		
		//Find rectangles
		List<Rect> rectangles = new ArrayList<Rect>();
		for(MatOfPoint contour: contours){
			rectangles.add(Imgproc.boundingRect(contour));
		}

		//Display rectangles
		drawRectangles(image, rectangles);
		Imgproc.drawContours(image, contours, -1, new Scalar(127));
		Highgui.imwrite("C:\\Users\\Laura\\Pictures\\OpenCV\\test3.png", image);
		
		//Display filtered rectanges, and all contours
		rectangles = filterContours(rectangles, FILTER_TYPE.AREA, 10000, 640 * 480); 
		Mat filtered = Mat.zeros(480, 640, CvType.CV_8UC1);
		drawRectangles(filtered, rectangles);
		Imgproc.drawContours(filtered, contours, -1, new Scalar(127));
		Highgui.imwrite("C:\\Users\\Laura\\Pictures\\OpenCV\\test4.png", filtered);
	}
	
	private static List<Rect> filterContours(List<Rect> rectangles, FILTER_TYPE filter, double min, double max){
		int i = 0; double dimension = 0;
		while(i < rectangles.size()){
			switch (filter){
				case AREA: {
					dimension = rectangles.get(i).area();
					break;
				}
				case WIDTH: {
					dimension = rectangles.get(i).width;
					break;
				}
				case HEIGHT: {
					dimension = rectangles.get(i).height;
					break;
				}
				case PERIMETER: {
					dimension = 2 * rectangles.get(i).width + 2 * rectangles.get(i).height;
				}
			}
			if(dimension < min || dimension > max){
				rectangles.remove(i);
				System.out.println("Removing: " + filter + " : " + dimension);
			}
			else{
				i++;
				System.out.println("Keeping: " + filter + " : " + dimension);
			}
		}
		return rectangles;
	}
	
	private static void drawRectangles(Mat img, List<Rect> rectangles){
		for(Rect rectangle: rectangles){
			Core.rectangle(img, rectangle.tl(), rectangle.br(), new Scalar(255));
		}
	}

}
