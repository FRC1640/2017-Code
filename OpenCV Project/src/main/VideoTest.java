package main;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

public class VideoTest {
	private static JFrame frame = new JFrame();
	private static JPanel panel = new JPanel();
	
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary("opencv_ffmpeg2413");
		frame.getContentPane().add(panel);
		frame.setSize(640, 480);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);

		FlowLayout border = new FlowLayout();
		panel.setLayout(border);
		border.setAlignment(FlowLayout.LEFT);

		Mat mat = new Mat();
		VideoCapture video = new VideoCapture();
		//System.out.println(video.open(0));
		System.out.println(video.open("http://FRC:FRC@10.16.40.181/mjpg/video.mjpg"));
		//"http://FRC:FRC@10.16.40.181:80/jpg/image.jpg?size=3"
		//"http://FRC:FRC@10.16.40.181/mjpg/video.mjpg"
		//"http://FRC:FRC@10.16.40.181:80/axis-cgi/mjpg/video.cgi?date=1&clock=1&resolution=320x240"
		JLabel label;
		//video.read(mat);
		mat = Highgui.imread("C:\\Users\\Laura\\pictures\\OpenCV\\RealFullField\\20.jpg");
		Imgproc.resize(mat, mat, new Size(100, 100));
		label = imageToLabel(matToImage(mat, true), 0, 0, 100, 100);
		panel.add(label);
		Mat oldMat = mat;
		panel.repaint();
		
		int i = 100;
		while(video.isOpened()){
			panel.remove(label);
			mat = new Mat();
			System.out.println("position: " + video.set(2, 1));
			System.out.println("frames: " + video.set(7, 1));
			video.read(mat);
			System.out.println(oldMat.get(1, 1) == mat.get(1, 1));
			Imgproc.resize(mat, mat, new Size(i, i));
			//i += 100;
			label = imageToLabel(matToImage(mat, true), 0, 0, 100, 100);
			
			oldMat = mat;

			panel.add(label);
			panel.repaint();
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}

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
	
	public static BufferedImage mat2Img(Mat in)
    {
        BufferedImage out;
        byte[] data = new byte[320 * 240 * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(320, 240, type);

        out.getRaster().setDataElements(0, 0, 320, 240, data);
        return out;
    } 
}
