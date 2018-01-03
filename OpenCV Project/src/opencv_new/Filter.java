package opencv_new;

import java.util.LinkedHashMap;

public abstract class Filter {
	protected LinkedHashMap<String, String[]> map;
	
	protected Filter(LinkedHashMap<String, String[]> map){
		this.map = map;
	}
	
	protected Filter(){}
	
	public void setInputs(LinkedHashMap<String, String[]> map){
		this.map = map;
	}
	
	public abstract FilterParameter execute(FilterParameter param);

}
