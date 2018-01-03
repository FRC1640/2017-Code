package opencv_new;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class Close extends Filter {
	private int radius;
	private boolean init;

	@Override
	public FilterParameter execute(FilterParameter param) {
		if(!init){
			init();
		}
		Mat dst = null;
		if(param.getType().equals(FilterParameter.FilterType.MAT)){
			dst = (Mat) param.getParam();
			Imgproc.morphologyEx(dst, dst, Imgproc.MORPH_CLOSE, Mat.ones(radius, radius, CvType.CV_8UC1));
		}
		param.setParam(dst, FilterParameter.FilterType.MAT);
		return param;
	}
	
	private void init(){
		try{
			radius = new Integer(map.get(FilterManager.getInstance().CLOSE)[0]);
		}catch(Exception e){e.printStackTrace();}
	}

}
