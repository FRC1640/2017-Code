package main; 

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
 
public class Test{
	private static ArrayList<JLabel> labels = new ArrayList<JLabel>();
	private static ArrayList<JSlider> sliders = new ArrayList<JSlider>();
	private static JFrame frame = new JFrame();
	private static JPanel panel = new JPanel();
	private static Mat mat;
	private static List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	private static enum FILTER_TYPE {AREA, WIDTH, HEIGHT, PERIMETER};
	private static VideoCapture video;
	
	public Test(){
		 
	}
   
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		int numImages = 5;
		for(int i = 0; i < numImages; i++){
			//Mat mat = Highgui.imread("C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\20.jpg");
			Mat mat = Highgui.imread("C:\\Users\\Laura\\Documents\\Programming\\FRC Testing\\Copy of OpenCV\\image.jpg");
			//Imgproc.resize(mat, mat, new Size(100, 100));
			labels.add(imageToLabel(matToImage(mat, true), 0, 0, 100, 100));
			
		}
		
		/*try{
			File outputfile = new File("image.jpg");
			ImageIO.write(image, "jpg", outputfile);
		}catch(Exception e){System.out.println(e);}*/
		   
		initPanel();
		
		int numSliders = 6;
		int x = 450;
		int y = 0;
		for(int i = 0; i < numSliders; i++){
			JSlider slider = new JSlider(0, 255);
			slider.setBounds(x, y, 100, 30);
			if(i < 3)
				slider.setValue(50);
			else 
				slider.setValue(255);
			sliders.add(slider);
			y += 30;
		}
		JSlider areaSlider = new JSlider(0, 1000);
		areaSlider.setBounds(x, y, 100, 30);
		areaSlider.setValue(10000);
		sliders.add(areaSlider);
		for(JSlider slider : sliders){
			panel.add(slider);
		}
		panel.add(labels.get(0));
		
		String[] names = {"min hue", "min sat", "min lum", "max hue", "max sat", "max lum", "min area"};
		
		y = 0;
		for(int i = 0; i < names.length; i++){
			JLabel label = new JLabel(names[i]);
			label.setBounds(550, y, 100, 30);
			panel.add(label);
			y += 30;
		}
		
		//video = new VideoCapture();
		//video.open("http://root:root@10.16.40.181/jpg/image.jpg?size=3");
		
		while(true)
			updatePanel();
	}
	
	
	
	private static void initPanel(){
		frame.getContentPane().add(panel);
		frame.setSize(640, 480);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		
		FlowLayout border = new FlowLayout();
		panel.setLayout(border);
		border.setAlignment(FlowLayout.LEFT);
	}
	
	private static void updatePanel(){
		for(int i = 1; i < labels.size(); i++){
			panel.remove(labels.get(i));
		}
		/*for(int i = 0; i < labels.size(); i++){
			panel.remove(labels.get(i));
		}*/
		
		//getImage();
		HSLThreshold();
		dialate();
		findContours();
		int area = sliders.get(6).getValue();
		filterContours(FILTER_TYPE.AREA, area, 480*640);
		
		for(int i = 1; i < labels.size(); i++){
			panel.add(labels.get(i));
		}
		/*for(int i = 0; i < labels.size(); i++){
			panel.add(labels.get(i));
		}*/
		
		panel.repaint();
		try{
			Thread.sleep(500);
		}catch(Exception e){System.out.println(e);}
	}
	
	private static void getImage(){
		//video.read(mat);
		Imgproc.resize(mat, mat, new Size(100, 100));
		labels.set(0, imageToLabel(matToImage(mat, false), 150, 0, 100, 100));
	}
	
	private static void HSLThreshold(){
		int hMin = sliders.get(0).getValue();
		int hMax = sliders.get(3).getValue();
		int sMin = sliders.get(1).getValue();
		int sMax = sliders.get(4).getValue();
		int lMin = sliders.get(2).getValue();
		int lMax = sliders.get(5).getValue();
		//System.out.println(hMin);
		mat = Highgui.imread("C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\0.jpg");
		Imgproc.resize(mat, mat, new Size(100, 100));
		Core.inRange(mat, new Scalar(hMin, lMin, sMin), new Scalar(hMax, lMax, sMax), mat);
		labels.set(1, imageToLabel(matToImage(mat, false), 150, 0, 100, 100));
	}
	
	private static void dialate(){
		int radius = 3; 
		Imgproc.dilate(mat, mat, Mat.ones(radius, radius, CvType.CV_8UC1));
		labels.set(2, imageToLabel(matToImage(mat, false), 300, 0, 100, 100));
	}
	
	private static void findContours(){
		//Find and display contours
		contours = new ArrayList<MatOfPoint>();
		Imgproc.findContours(mat, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_NONE);
		mat = Mat.zeros(new Size(100, 100), CvType.CV_8UC1);
		Imgproc.drawContours(mat, contours, -1, new Scalar(255));
		Highgui.imwrite("C:\\Users\\Laura\\Pictures\\OpenCV\\test2.png", mat);
				
		
		labels.set(3, imageToLabel(matToImage(mat, false), 0, 150, 100, 100));
	}
	
	private static void filterContours( FILTER_TYPE filter, int min, int max){
		int i = 0; double dimension = 0;
		//Find rectangles
				List<Rect> rectangles = new ArrayList<Rect>();
				for(MatOfPoint contour: contours){
					rectangles.add(Imgproc.boundingRect(contour));
				}
				//drawRectangles(mat, rectangles);
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
			}
			else{
				i++;
			}
		}
		drawRectangles(mat, rectangles);
		labels.set(4, imageToLabel(matToImage(mat, false), 150, 150, 100, 100));
	}
	
	private static void drawRectangles(Mat img, List<Rect> rectangles){
		for(Rect rectangle: rectangles){
			Core.rectangle(img, rectangle.tl(), rectangle.br(), new Scalar(255));
		}
	}
	
	private static JLabel imageToLabel(BufferedImage image, int x, int y, int width, int height){
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon((Image) image));
		label.setBounds(x, y, width, height);
		return label;
	}
	
	public static BufferedImage matToImage(Mat mat, boolean color){
		int type = color ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
		byte[] data = new byte[mat.cols() * mat.rows() * (int) mat.elemSize()];
		mat.get(0, 0, data);
		byte b;  
		if(color){
	        for(int i=0; i<data.length; i=i+3) {  
	          b = data[i];  
	          data[i] = data[i+2];  
	          data[i+2] = b;  
	        }  
		}
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return image;
	}
 }  