package opencv_new;

public class FilterParameter {
	public enum FilterType {MAT, RECT};
	private Object param;
	private FilterType type;
	
	public FilterParameter(Object param, FilterType type){
		this.param = param;
		this.type = type;
	}
	
	public FilterType getType(){
		return type;
	}
	
	public Object getParam(){
		return param;
	}
	
	public void setParam(Object param, FilterType type){
		this.param = param;
		this.type = type;
	}
}
