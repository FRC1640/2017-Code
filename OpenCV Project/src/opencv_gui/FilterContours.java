package opencv_gui;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class FilterContours extends Filter{
	
	private final int NUM_INPUTS = 8;
	private int iterations = 0, frameCount = 0;
	private int[] inputs;
	private Mat mat;
	private NetworkTable table;
	
	public FilterContours(){
		this(new int[0]);
	}
	
	public FilterContours(int[] inputs){
		super();
		this.inputs = new int[NUM_INPUTS];
		setInputs(inputs);
//		contours = new ArrayList<MatOfPoint>();
		if(!FilterManager.gui){
			NetworkTable.setClientMode();
//			NetworkTable.setIPAddress("10.16.40.19");
			NetworkTable.setTeam(1640);
			String tableName = FilterManager.gear ? "OpenCVGear" : "OpenCVBoiler";
			table = NetworkTable.getTable(tableName);
		}
	}
	private Mat m =  Mat.zeros(new Size(FilterManager.PIC_WIDTH_GUI, FilterManager.PIC_HEIGHT_GUI), CvType.CV_8UC1);
	@Override
	public void execute(Mat src, Mat dst, int picWidth, int picHeight){
		int minArea = inputs[0];
		int maxArea = inputs[1];
		int minWidth = inputs[2];
		int maxWidth = inputs[3];
		int minHeight = inputs[4];
		int maxHeight = inputs[5];
		int minPerim = inputs[6];
		int maxPerim = inputs[7];
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
//		Mat mat = src.clone();
//		Mat srcCopy = src.clone();
		Imgproc.findContours(src, contours, m, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
//		srcCopy.release();
		src.release();
		dst.release();
//		m.release();
		dst = Mat.zeros(new Size(FilterManager.PIC_WIDTH_GUI, FilterManager.PIC_HEIGHT_GUI), CvType.CV_8UC1);
		
		//Find rectangles
		ArrayList<Rect> rectangles = new ArrayList<Rect>();
		for(MatOfPoint contour: contours){
			rectangles.add(Imgproc.boundingRect(contour));
		}


		int i = 0;
		while(i < rectangles.size()){
			double area = rectangles.get(i).area();
			int width = rectangles.get(i).width;
			int height = rectangles.get(i).height;
			int perimeter = 2 * rectangles.get(i).width + 2 * rectangles.get(i).height;
			if(removeRectangle(area, minArea, maxArea) 
					|| removeRectangle(width, minWidth, maxWidth) 
					|| removeRectangle(height, minHeight, maxHeight)
					|| removeRectangle(perimeter, minPerim, maxPerim)){
				rectangles.remove(i);
			}else{
				i++;
			}
		}
		if(FilterManager.gui){
			drawRectangles(dst, rectangles);
		}
		else{
			publishData(rectangles);
		}
		mat = dst;
	}
	
	private void publishData(List<Rect> data){
		double[] x = new double[data.size()];
		double[] y = new double[data.size()];
		double[] area = new double[data.size()];
		double[] width = new double[data.size()];
		double[] height = new double[data.size()];
		
		for(int rec = 0; rec < data.size(); rec++){
			x[rec] = data.get(rec).x;
			y[rec] = data.get(rec).y;
			area[rec] = data.get(rec).area();
			width[rec] = data.get(rec).width;
			height[rec] = data.get(rec).height;
		}
		table.putNumberArray("x", x);
		table.putNumberArray("y", y);
		table.putNumberArray("area", area);
		table.putNumberArray("width", width);
		table.putNumberArray("height", height);
		table.putNumber("frameCount", frameCount);
		frameCount++;
	}
	
	@Override
	public Mat getMat(){
		return mat;
	}
	
	
	private void drawRectangles(Mat img, List<Rect> rectangles){
		for(Rect rectangle: rectangles){
			Core.rectangle(img, rectangle.tl(), rectangle.br(), new Scalar(255));
		}
	}
	
	private boolean removeRectangle(double dimension, int min, int max){
		if(dimension < min || dimension > max){
			return true;
		}
		return false;
	}
	
	@Override
	public String toString(){
		String filter = "FilterContours: ";
		for(int input : inputs){
			filter += " " + input + ", ";
		}
		return filter;
	}

	@Override
	public void setInputs(int[] inputs){
		for(int i = 0; i < this.inputs.length; i++){
			this.inputs[i] = inputs[i];
		}
	}
	
	@Override
	public int[] getInputs(){
		return inputs;
	}

}
