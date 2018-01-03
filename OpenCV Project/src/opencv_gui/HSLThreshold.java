package opencv_gui;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class HSLThreshold extends Filter{
	private final int NUM_INPUTS = 6;
	private int[] inputs;
	private Mat mat;
	
	public HSLThreshold(){
		this(new int[0]);
	}
	
	public HSLThreshold(int[] inputs){
		super();
		this.inputs = new int[NUM_INPUTS];
		setInputs(inputs);

	}
	
	@Override
	public void execute(Mat src, Mat dst, int width, int height){
		int hMin = inputs[0];
		int hMax = inputs[1];
		int sMin = inputs[2];
		int sMax = inputs[3];
		int lMin = inputs[4];
		int lMax = inputs[5];
		//Imgproc.resize(src, dst, new Size(width, height));
		Core.inRange(src, new Scalar(hMin, lMin, sMin), new Scalar(hMax, lMax, sMax), dst);
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
		String hsl = "HSL: ";
		for(int input : inputs){
			hsl += " " + input + ", ";
		}
		return hsl;
	}

}
