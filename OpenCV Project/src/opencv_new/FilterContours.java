package opencv_new;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class FilterContours extends Filter {
	private boolean init;
	private int minArea, minHeight, minWidth, minPerimeter;
	private int maxArea, maxHeight, maxWidth, maxPerimeter;
	
	@Override
	public FilterParameter execute(FilterParameter param) {
		if(!init){
			init();
		}
		else{
			if(param.getType().equals(FilterParameter.FilterType.MAT)){
				Mat dst = (Mat) param.getParam();
				ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
				Imgproc.findContours(dst, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);

				ArrayList<Rect> rectangles = new ArrayList<Rect>();
				for(MatOfPoint contour: contours){
					rectangles.add(Imgproc.boundingRect(contour));
				}

				param.setParam(filterContours(rectangles), FilterParameter.FilterType.RECT);
			}
		}
		return param;
	}
	
	private void init(){
		String[] inputs = map.get(FilterManager.getInstance().FILTER_CONTOURS);
		try{
			minArea = new Integer(inputs[0]);
			maxArea = new Integer(inputs[1]);
			minWidth = new Integer(inputs[2]);
			maxWidth = new Integer(inputs[3]);
			minHeight = new Integer(inputs[4]);
			maxHeight = new Integer(inputs[5]);
			minPerimeter = new Integer(inputs[6]);
			maxPerimeter = new Integer(inputs[7]);
		}catch(Exception e){e.printStackTrace();}
		init = true;
	}
	
	private ArrayList<Rect> filterContours(ArrayList<Rect> rectangles){
		int i = 0;
		while(i < rectangles.size()){
			double area = rectangles.get(i).area();
			int width = rectangles.get(i).width;
			int height = rectangles.get(i).height;
			int perimeter = 2 * rectangles.get(i).width + 2 * rectangles.get(i).height;
			if(removeRectangle(area, minArea, maxArea) 
					|| removeRectangle(width, minWidth, maxWidth) 
					|| removeRectangle(height, minHeight, maxHeight)
					|| removeRectangle(perimeter, minPerimeter, maxPerimeter)){
				rectangles.remove(i);
			}else{
				i++;
			}
		}
		return rectangles;
	}
	
	private boolean removeRectangle(double dimension, int min, int max){
		if(dimension < min || dimension > max){
			return true;
		}
		return false;
	}

}
