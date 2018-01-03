package drive;

import com.kauailabs.navx.frc.AHRS;

import constants.PortConstants;
import edu.wpi.first.wpilibj.SerialPort;

public class DriveIO {
	private static DriveIO driveIO;
	
	public static DriveIO getInstance(){
		if(driveIO == null)
			driveIO = new DriveIO();
		return driveIO;
	}
		
	private CVTPivot[] pivots = new CVTPivot[4];
	private double gyroOffset = 0; //used for resetting gyro
	private AHRS ahrs; //navX
	
	private DriveIO(){
		pivots[0] = new CVTPivot(PortConstants.MotorFLDID, PortConstants.MotorFLSID, PortConstants.ResolverFL, PortConstants.ServoFL, 
				0.20019529200000002, 4.737548343 , 0.0, "fl"); //180 on prime
		pivots[1] = new CVTPivot(PortConstants.MotorFRDID, PortConstants.MotorFRSID, PortConstants.ResolverFR, PortConstants.ServoFR, 
				0.20385740100000002, 4.729003422 , 0.0, "fr");
		pivots[2] = new CVTPivot(PortConstants.MotorBLDID, PortConstants.MotorBLSID, PortConstants.ResolverBL, PortConstants.ServoBL, 
				 0.209960916, 4.742431155, 180.0, "bl"); //TODO: Offset is 180 on prime
		pivots[3] = new CVTPivot(PortConstants.MotorBRDID, PortConstants.MotorBRSID, PortConstants.ResolverBR, PortConstants.ServoBR, 
				0.205078104, 4.739989749 , 180.0, "br");
		
		ahrs = new AHRS(SerialPort.Port.kMXP);

		gyroOffset = ahrs.getYaw(); 
	}
	
	public CVTPivot[] getPivots(){
		return pivots;
	}
	
	public double getYaw(){
//		return ahrs.getYaw();
		return (360 - ahrs.getYaw()) % 360;
	}
	
	public void resetGyro(){
		gyroOffset = getYaw();
	}
}
