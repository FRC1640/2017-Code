package drive;


import utilities.Movement;
import utilities.Vector;

import com.kauailabs.navx.frc.AHRS;

import constants.PortConstants;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;



public class DriveIO {
	
	private AHRS ahrs; //navX
	
	//singleton instance
	private static DriveIO driveIO = null;
	
	//joystick instance
	private Joystick driver;
	
	private CVTPivot[] pivots = new CVTPivot[4];
	
	
	private double gyroOffset = 0, fcOffset = 0; //used for resetting gyro
	private double turnAngle = 999;
	private double roughTurnAngle = 999;
	private Vector translation = Vector.makeCartesian(0, 0);
	private Movement movement = new Movement(new Vector(), 0);
	private boolean aligning;
	private boolean gearStrafe = false;
	
	private AnalogInput frontSonar, leftSonar, rightSonar;
	private final double VOLTS_PER_INCH = 0.0098;
	
	private Encoder omniWheel;
	private final double OMNI_WHEEL_DIAMETER = 3;
	private final double COUNTS_PER_ROTATION = 2048;
	private final double INCHES_PER_PULSE = 2*Math.PI*OMNI_WHEEL_DIAMETER/2/COUNTS_PER_ROTATION;
	
	private boolean turnWheel, opControl;
	public enum GearStatus {SHIFTED_LEFT, SHIFTED_RIGHT, NO_SHIFT, FAILED};
	private GearStatus gearStatus = GearStatus.NO_SHIFT;
	
	private double alignOffset;

	private DriveIO() {
		// For testing gear place auto align on DEWBot 10
		/*pivots[0] = new CVTPivot(3, 1, 0, 0, 9, 0.2233886420726776, 4.7277827, 0.0, "fl");
		pivots[1] = new CVTPivot(6, 0, 1, 1, 8, 0.2111816257238388, 4.754638195037842, 0.0, "fr");
		pivots[2] = new CVTPivot(2, 8, 2, 2, 7, 0.2111816257238388, 4.735106945037842, 180.0, "bl");
		pivots[3] = new CVTPivot(5, 4, 3, 3, 6, 0.2307128608226776, 4.747313976287842, 180.0, "br");*/
		
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
		fcOffset = ahrs.getYaw();
		
		frontSonar = new AnalogInput(PortConstants.FRONT_SONAR);
		leftSonar = new AnalogInput(PortConstants.LEFT_SONAR);
		rightSonar = new AnalogInput(PortConstants.RIGHT_SONAR);

		
		omniWheel = new Encoder(PortConstants.OmniWheelA, PortConstants.OmniWheelB, false);
		omniWheel.setDistancePerPulse(INCHES_PER_PULSE);
	}
	
	public static DriveIO getInstance() {
		//if the singleton class hasn't been created yet, create one
		if (driveIO == null) 
			driveIO = new DriveIO();
		return driveIO;
	}
	
	public CVTPivot[] getPivots(){
		return pivots;
	}
	
	public void resetEncoders(){
		for(Pivot p : pivots)
			p.resetEncoder();
	}
	
	
	//Gyro functions
	public void resetGyro() {
		System.out.println("Previous Yaw: " + getYaw());
		gyroOffset = driveIO.ahrs.getYaw(); //use the current gyro reading as the offset
		System.out.println("Resetting Yaw: " + getYaw());
	}
	
	public void resetFC(){
		fcOffset = ahrs.getYaw();
		System.out.println("Reset FC");
	}
	
	public void setFC(double fcOffset){
		this.fcOffset = fcOffset;
		System.out.println("set FC: " + fcOffset);
	}
	
	public void setFCOffset(double fcOffset) {
		this.fcOffset = fcOffset;
	}
	
	public double getFCOffset(){
		return fcOffset;
	}
	public double getYaw() {
		//swith direction of gyro and keep it between 0 and 360
		return (driveIO.ahrs.getYaw() + 360 - gyroOffset) % 360;
	}
	
	public double getFCYaw(){
		return (driveIO.ahrs.getYaw() + 360 - fcOffset) % 360;
	}
	
	public double getRawYaw() {
		return driveIO.ahrs.getYaw() - gyroOffset;
	}
	
	public double getPitch() {
		return driveIO.ahrs.getPitch();
	}
	
	public double getRoll() {
		return driveIO.ahrs.getRoll();
	}
	
	public double getOmniWheel() {
		return omniWheel.getDistance();
	}
	
	public void resetOmniWheel() {
		omniWheel.reset();
	}
	
	public double getEncoder(String id) {
		double value = 0;
		
		for (int i = 0; i < getPivots().length; i++) {
			Pivot p = getPivots()[i];
			if (id.equals(p.getID())) value = p.getPosition();
		}
		
		return value;
	}
	
	public double getInches(String id) {
		double value = 0;
		
		for (int i = 0; i < getPivots().length; i++) {
			Pivot p = getPivots()[i];
			if (id.equals(p.getID())) value = p.getInches();
		}
		
		return value;
	}
	
	public double getTurnAngle(){
		return turnAngle;
	}
	
	public void setTurnAngle(double value){
		turnAngle = value;
	}
	
	public double getRoughTurnAngle() {
		return roughTurnAngle;
	}
	
	public void setRoughTurnAngle(double value) {
		roughTurnAngle = value;
	}
	
	public void setTranslationVector(Vector translation){
		System.out.println("Set Translation Vector: " + translation.getX() + " : " + translation.getY());
		this.translation = translation;
	}
	
	public Vector getTranslationVector(){
		return translation;
	}

	public Movement getMovement() {
		return movement;
	}
	
	public void setAligning(boolean aligning){
		this.aligning = aligning;
	}
	
	public boolean getAligning(){
		return aligning;
	}
	
	public double getDisplacementY(){
		return ahrs.getVelocityY();//ahrs.getDisplacementY();
	}
	
	public double getDisplacementX(){
		return ahrs.getVelocityX();//ahrs.getDisplacementX();
	}
	
	public void resetDisplacement(){
		ahrs.resetDisplacement();
	}
	
	public double getFrontSonarVoltage(){
		return frontSonar.getVoltage();
	}
	
	public double getLeftSonarVoltage(){
		return leftSonar.getVoltage();
	}
	
	public double getRightSonarVoltage(){
		return rightSonar.getVoltage();
	}
	
	public double getFrontSonarInches(){
		return frontSonar.getVoltage() / VOLTS_PER_INCH;
	}
	
	public double getLeftSonarInches(){
		return leftSonar.getVoltage() / VOLTS_PER_INCH;
	}
	
	public double getRightSonarInches(){
		return rightSonar.getVoltage() / VOLTS_PER_INCH;
	}
	
	public boolean getTurnWheel(){
		return turnWheel;
	}
	
	public void setTurnWheel(boolean turnWheel){
		this.turnWheel = turnWheel;
	}
	
	public GearStatus getGearStatus(){
		return gearStatus;
	}
	
	public void setGearStatus(GearStatus gearStatus){
		this.gearStatus = gearStatus;
	}
	
	public boolean getGearStrafe() {
		return gearStrafe;
	}
	
	public void setGearStrafe(boolean gearStrafe) {
		this.gearStrafe = gearStrafe;
	}
	
	public boolean getOpControl(){
		return opControl;
	}
	
	public void setOpControl(boolean opControl){
		this.opControl = opControl;
	}
	
	public void setAlignOffset(double alignOffset){
		this.alignOffset = alignOffset;
	}
	
	public double getAlignOffset(){
		return alignOffset;
	}
}

