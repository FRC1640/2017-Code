package utilities;


public class ControlInputs {
	private static Controller driver;
	private static Controller op;
	
	public static Controller getDriver(){
		if(driver == null){
			driver = new Controller(0);
		}
		return driver;
	}
	
	public static Controller getOp(){
		if(op == null){
			op = new Controller(1);
		}
		return op;
	}
	
}
