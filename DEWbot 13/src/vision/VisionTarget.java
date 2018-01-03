package vision;

public class VisionTarget{
	private double centerX, centerY, width, height, area;
	
	public VisionTarget(double centerX, double centerY, double width, double height, double area){
		this.centerX = centerX;
		this.centerY = centerY;
		this.width = width;
		this.height = height;
		this.area = area;
	}
	
	public double getCenterX(){
		return centerX;
	}
	
	public double getCenterY(){
		return centerY;
	}
	
	public double getWidth(){
		return width;
	}
	
	public double getHeight(){
		return height;
	}
	
	public double getArea(){
		return area;
	}

	public void setCenterX(double centerX){
		this.centerX = centerX;
	}	

	public void setCenterY(double centerY){
		this.centerY = centerY;
	}	

	public void setWidth(double width){
		this.width = width;
	}	
	
	public void setHeight(double height){
		this.height = height;
	}	
	
	public void setArea(double area){
		this.area = area;
	}		
	
}
