package opencv_gui;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Close extends Filter{
	private final int NUM_INPUTS = 1;
	private int[] inputs;
	private Mat mat;
	
	public Close(){
		this(new int[0]);
	}
	
	public Close(int[] inputs){
		super();
		this.inputs = new int[NUM_INPUTS];
		setInputs(inputs);
	}
	
	@Override
	public void execute(Mat src, Mat dst, int width, int height){
		int radius = inputs[0];
//		Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_CLOSE, Mat.ones(radius, radius, CvType.CV_8UC1));
		Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_ERODE, Mat.ones(radius, radius, CvType.CV_8UC1));
		mat = dst;
	}
	
	@Override
	public Mat getMat(){
		return mat;
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
	
	@Override
	public String toString(){
		String close = "Close: ";
		for(int input : inputs){
			close += " " + input + ", ";
		}
		return close;
	}
	
}