package opencv_new;


public class FilterManager {
	private static FilterManager filterManager;
	public final String GET_IMAGE = "GetImage", 
			HSL_THRESHOLD = "HSL", 
			CLOSE = "Close", 
			FILTER_CONTOURS = "FilterContours", 
			PUBLISH_CONTOURS = "PublishContours";
	
	private FilterManager(){}
	
	public static FilterManager getInstance(){
		if(filterManager == null){
			filterManager = new FilterManager();
		}
		return filterManager;
	}
	
	
	public Filter getFilter(String key){
		switch(key){
			case GET_IMAGE: return new GetImage();
			case HSL_THRESHOLD: return new HSLThreshold();
			case CLOSE: return new Close();
			case FILTER_CONTOURS: return new FilterContours();
			case PUBLISH_CONTOURS: return new PublishContours();
			default: return null;
		}
	}

}
