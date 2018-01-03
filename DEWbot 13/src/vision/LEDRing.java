package vision;

import edu.wpi.first.wpilibj.Relay;

public class LEDRing {
	private static LEDRing ledRing;
	private Relay relay;
	private Relay.Value state;
	
	private LEDRing(){
		relay = new Relay(0, Relay.Direction.kForward);
		relay.set(Relay.Value.kOn);
		state = Relay.Value.kOn;
	}
	
	public static LEDRing getInstance(){
		if(ledRing == null){
			ledRing = new LEDRing();
		}
		return ledRing;
	}
	
	public void setState(Relay.Value state){
		this.state = state;
		relay.set(state);
	}
	
	public Relay.Value getState(){
		return state;
	}

}
