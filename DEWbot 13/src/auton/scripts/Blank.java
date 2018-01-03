package auton.scripts;


public class Blank extends AutonScript { //blank script
	private static Blank blank;
	
	private Blank(){}
	
	public static Blank getInstance(){ //get singleton instance
		if(blank == null)
			blank = new Blank();
		return blank;
	}

}
