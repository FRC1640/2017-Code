package drive;

import constants.Constants;
import utilities.PIDOutputDouble;
import utilities.PIDSourceDouble;
import utilities.Utilities;
import edu.wpi.first.wpilibj.PIDController;

public class PositionPID extends DriveControl{
	private static PositionPID positionPID;
	private Pivot[] pivots;
	private PIDController[] pids = new PIDController[3];
	private PIDSourceDouble[] positions = new PIDSourceDouble[3];
	private PIDOutputDouble[] outputSpeeds = new PIDOutputDouble[3];
	
	public static PositionPID getInstance(){
		if(positionPID == null)
			positionPID = new PositionPID();
		return positionPID;
	}
	
	private PositionPID(){
		pivots = DriveIO.getInstance().getPivots();
		
		for(int i = 0; i < 3; i++){
			positions[i] = new PIDSourceDouble();
			outputSpeeds[i] = new PIDOutputDouble();
			pids[i] = new PIDController(0.000025, 0.000005, 0.0000001, positions[i], outputSpeeds[i]);
			pids[i].enable();
		}
	}

	@Override
	protected void execute(double x1, double y1, double x2, double y2) {
		if(x1 != 0 && y1 != 0){
			double angle = Utilities.angle(x1, y1);
			for(Pivot p : pivots){
				p.setTargetAngle(angle);
			}
		}
		
		double speed = Utilities.magnitude(x1, y1);
		pivots[Constants.MASTER_PIVOT].setDrive(speed);
//		pivots[2].setDrive(speed);
//		pivots[3].setDrive(speed);
		
		int j = 0;
		for(int i = 0; i < 4; i++){
			if(Constants.MASTER_PIVOT == i)
				i++;
			if(i == 4)
				break;
			pids[j].setSetpoint(Math.abs(pivots[Constants.MASTER_PIVOT].getPosition()));
			positions[j].setValue(Math.abs(pivots[i].getPosition()));
//			System.out.println((i + 1) + " : " + outputSpeeds[i].getValue() + " : " + pids[i].getError() + "/n");
			pivots[i].setDrive(outputSpeeds[j].getValue() + speed);
			j++;
		}
	}

}
