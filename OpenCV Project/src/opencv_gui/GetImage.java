package opencv_gui;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;


public class GetImage extends Filter{
	private final int NUM_INPUTS = 0;
	private int[] inputs;
	private Mat mat;
	private static VideoCapture video;
	private String url;
	
	public GetImage(){
		this(new int[0]);
		if(video == null){
//			url = "http://admin:@10.16.40.184/video.cgi?.mjpg";//"http://admin:@10.16.40.183/video.cgi?.mjpg";
			url = FilterManager.gear ? FilterManager.urlGear : FilterManager.urlBoiler;
			System.out.println("Opening camera: " + url);
			video = new VideoCapture();
//			url = "http://FRC:FRC@10.16.40.181/mjpg/video.mjpg";
			System.out.println("Camera: " + video.open(url));
			//System.out.println("Camera: " + video.open(url));
			//	"http://FRC:FRC@10.16.40.181/mjpg/video.mjpg"
			//video.open("out.mp4");
		}
	}
	
	public GetImage(int[] inputs){
		super();
		this.inputs = new int[NUM_INPUTS];
		setInputs(inputs);
	}
	
	@Override
	public void execute(Mat src, Mat dst, int width, int height){
		//dst = Highgui.imread("C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\20.jpg");
		//dst = Highgui.imread("/home/pi/OpenCV/testImage.jpg");
		try{
			if(!video.isOpened()){
				if(FilterManager.gui)
					dst = Highgui.imread("C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\20.jpg");
				else
					dst = Mat.zeros(new Size(FilterManager.PIC_WIDTH, FilterManager.PIC_HEIGHT), CvType.CV_8UC1);
					//dst = Highgui.imread("/home/pi/OpenCV/testImage.jpg");
				System.out.println("Opening... " + video.open(url));
			    
			}	
			else{
				if(!video.read(dst))
					dst = Mat.zeros(new Size(FilterManager.PIC_WIDTH, FilterManager.PIC_HEIGHT), CvType.CV_8UC1);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			dst = Mat.zeros(new Size(FilterManager.PIC_WIDTH, FilterManager.PIC_HEIGHT), CvType.CV_8UC1);
//			dst = Highgui.imread("/home/pi/OpenCV/testImage.jpg");
			
		}
		Imgproc.resize(dst, dst, new Size(width, height));
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
		String getImage = "GetImage: ";
		for(int input : inputs){
			getImage += " " + input + ", ";
		}
		return getImage;
	}


}
