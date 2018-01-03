package opencv_new;

import java.util.ArrayList;

import org.opencv.core.Rect;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class PublishContours extends Filter {
	private boolean init;
	private NetworkTable table;
	
	@Override
	public FilterParameter execute(FilterParameter param) {
		if(!init)
			init();
		else{
			if(param.getType().equals(FilterParameter.FilterType.RECT)){
				ArrayList<Rect> rectangles = (ArrayList<Rect>) param.getParam();
				double[] x = new double[rectangles.size()];
				double[] y = new double[rectangles.size()];
				double[] area = new double[rectangles.size()];
				double[] width = new double[rectangles.size()];
				double[] height = new double[rectangles.size()];
				
				for(int i = 0; i < rectangles.size(); i++){
					x[i] = rectangles.get(i).x;
					y[i] = rectangles.get(i).y;
					area[i] = rectangles.get(i).area();
					width[i] = rectangles.get(i).width;
					height[i] = rectangles.get(i).height;
					System.out.println("---------------------");
					System.out.println("x: " + x[i] + " y: " + y[i] + " area: " + area[i]);
				}
				
				table.putNumberArray("x", x);
				table.putNumberArray("y", y);
				table.putNumberArray("area", area);
				table.putNumberArray("width", width);
				table.putNumberArray("height", height);
			}
		}
		return param;
	}
	
	private void init(){
		String tableKey = map.get(FilterManager.getInstance().PUBLISH_CONTOURS)[1];
		String tableAddress = map.get(FilterManager.getInstance().PUBLISH_CONTOURS)[0];
		NetworkTable.setClientMode();
		if(tableAddress.contains(".")){
			System.out.println(tableAddress);
			NetworkTable.setIPAddress("10.0.0.110");
		}
		else{
			NetworkTable.setTeam(new Integer(tableAddress));
		}
		table = NetworkTable.getTable(tableKey);

		init = true;
	}

}
