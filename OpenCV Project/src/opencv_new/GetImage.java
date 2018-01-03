package opencv_new;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class GetImage extends Filter{
	private static VideoCapture video;
	private String url;
	private NetworkTable table;
	private String testImage = /*"C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\20.jpg";*/"/home/pi/OpenCV/testImage.jpg";
	private boolean init = false;

	protected GetImage() {

	}

	@Override
	public FilterParameter execute(FilterParameter param) {
		if(!init){
			init();
		}
		Mat dst = Mat.zeros(new Size(320, 240), CvType.CV_8UC1);

		if(!video.isOpened()){
			System.out.println("Not opened");
			dst = Highgui.imread(testImage);
			video.open(url);
		}	
		else{
			if(!video.read(dst)){
				dst = Highgui.imread(testImage);
				System.out.println("Not reading");
			}
			
			//dst = Highgui.imread(testImage);
		}
		param.setParam(dst, FilterParameter.FilterType.MAT);
		
		return param;
	}
	
	private void init(){
		String[] input = map.get(FilterManager.getInstance().GET_IMAGE);
		int bufferSize = 0;
		if(input.length == 2){
			url = map.get(FilterManager.getInstance().GET_IMAGE)[0];
			bufferSize = new Integer(map.get(FilterManager.getInstance().GET_IMAGE)[1]);
		}
		else{
			System.out.println("Incorrect # of arguments for GetImage. " + input.length + " inputs were given");
			url = "";
		}
		video = new VideoCapture();
		if(video.open(url)){
			System.out.println("Camera Opened");
		}
		
		video.set(Highgui.CV_CAP_PROP_BUFFERSIZE, bufferSize);
		init = true;
	}

}
