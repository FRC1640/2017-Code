package opencv_new;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Algorithm extends Threading{
	private LinkedHashMap<String, String[]> map;
	private ArrayList<Filter> filters = new ArrayList<Filter>();
	private FilterParameter nullParam = new FilterParameter(null, FilterParameter.FilterType.MAT);
	
	public Algorithm(LinkedHashMap<String, String[]> map){
		this.map = map;
	}

	@Override
	public void init() {
		for(String key : map.keySet()){
			filters.add(FilterManager.getInstance().getFilter(key));
		}
		
		for(Filter f : filters){
			f.setInputs(map);
		}
	}
	
	@Override
	public void update() {
		FilterParameter param = nullParam;
		for(Filter f : filters){
			param = f.execute(param);
		}
	}
}
