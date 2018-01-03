package opencv_gui;

import org.opencv.core.Mat;

public abstract class Filter {

	public Filter(){}
	
	public abstract void execute(Mat src, Mat dst, int width, int height);
	
	public abstract void setInputs(int[] inputs);
	
	public abstract int[] getInputs();
	
	public abstract Mat getMat();

	@Override
	public abstract String toString();
	
}