package auton.scripts;

import java.util.ArrayList;

import auton.AutonCommand;

public class ScriptArrayList extends ArrayList<AutonCommand>{
	private static final long serialVersionUID = 1L;

	public int indexOfName(String name){
		int index = -1;
		for(int i = 0; i < this.size(); i++){
			if(this.get(i).getName().equals(name))
				index = i;
		}
		return index;
	}
	
}
