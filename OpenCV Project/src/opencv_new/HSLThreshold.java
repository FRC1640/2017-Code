package opencv_new;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class HSLThreshold extends Filter{
	private int minH, maxH, minL, maxL, minS, maxS;
	private boolean init;

	@Override
	public FilterParameter execute(FilterParameter param) {
		if(!init){
			init();
		}
		
		Mat dst = null;
		if(param.getType().equals(FilterParameter.FilterType.MAT)){
			dst = (Mat) param.getParam();
			Core.inRange(dst, new Scalar(minH, minS, minL), new Scalar(maxH, maxS, maxL), dst);
		}
		else{
			System.out.println("HSL expected a mat, but recieved a " + param.getType() + ". The object was : " + param.getParam());
		}
		param.setParam(dst, FilterParameter.FilterType.MAT);
		return param;
	}
	
	private void init(){
		String[] inputs = map.get(FilterManager.getInstance().HSL_THRESHOLD);
		try{
			minH = new Integer(inputs[0]);
			maxH = new Integer(inputs[1]);
			minS = new Integer(inputs[2]);
			maxS = new Integer(inputs[3]);
			minL = new Integer(inputs[4]);
			maxL = new Integer(inputs[5]);
		}catch(Exception e){e.printStackTrace();}
		
		init = true;
	}
	
}
