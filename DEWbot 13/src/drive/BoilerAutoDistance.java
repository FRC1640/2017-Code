package drive;

import java.util.ArrayList;

import org.usfirst.frc.team1640.robot.Robot;
import org.usfirst.frc.team1640.robot.Robot.State;

import shooter.ShooterHoodIO;
import shooter.ShooterIO;
import utilities.ControlInputs;
import utilities.Controller;
import utilities.ShooterCalc;
import utilities.Utilities;
import vision.VisionData;
import vision.VisionTarget;

public class BoilerAutoDistance extends DriveControlDecorator{
	private double BOILER_AREA_BUFFER = 100, BOILER_HEIGHT_BUFFER = 100, BOILER_X_BUFFER = 100, DEFAULT_TARGET = 480/2;// - 50;
	private double pictureHeight = 480;
	private double A = 0, B = 0; // distance = A/(centerY-pictureHeight/2)+B
	private ShooterIO shooterIO = ShooterIO.getInstance();
	private ShooterHoodIO shooterHoodIO = ShooterHoodIO.getInstance();
	private DriveIO driveIO = DriveIO.getInstance();
	private boolean prevButton = false;
	
	public BoilerAutoDistance(DriveControl driveControl) {
		super(driveControl);
	}
	
	@Override
	public void execute(double x1, double y1, double x2, double y2){
		
		boolean button = ControlInputs.getOp().getButton(Controller.Button.BUTTON_B);
		if(button && !prevButton){
			double distance = getDistanceToBoilerVision();
			
			/**
			 * An Alternative Option
			 * double distance = getDistanceToBoilerSonar();
			 * 
			 * We could also Compare the Two (And Average, Maybe)
			 */
			
			if (distance > 0) {
				double [] shootingConditions = ShooterCalc.findOptimalShootingConditions(distance + ShooterCalc.frontToCenterDistance());
				//double [] shootingConditions = ShooterCalc.fastFindOptimalShootingConditions(distance + ShooterCalc.frontToCenterDistance());
				
				if(!ShooterCalc.useNativeFit()) {
					shooterIO.setShootingSpeed(ShooterCalc.convertToRPM(shootingConditions[0]));
					shooterHoodIO.setAngle(shooterHoodIO.horizontalToNativeDegrees((int) Math.round(shootingConditions[1])));
					System.out.println("Theoretical Inaccuracy: " + shootingConditions[2]);
				}
				else {
					shooterIO.setShootingSpeed(shootingConditions[0]);
					shooterHoodIO.setAngle((int) Math.round(shootingConditions[1]));
					System.out.println("Theoretical Inaccuracy: " + shootingConditions[2]);
				}
			}
		}
		prevButton = button;
		
		super.execute(x1, y1, x2, y2);
	}
	
	private double getDistanceToBoilerSonar(){
		return Utilities.inchesToMeters(driveIO.getLeftSonarInches());
	}
	
	private double getDistanceToBoilerVision(){
		ArrayList<VisionTarget> targets = VisionData.getInstance().getBoilerTargets();
		double centerY = 999;
		
		//Shouldn't these be in Math.abs()?
		for(VisionTarget t1 : targets){
			for(VisionTarget t2 : targets){
				if(t1.getCenterX() - (t2.getCenterX()) < BOILER_X_BUFFER){
					if(t1.getArea() - (t2.getArea() * 2) < BOILER_AREA_BUFFER){
						if(t1.getHeight() - t2.getHeight() * 2 < BOILER_HEIGHT_BUFFER){
							centerY = (t1.getCenterY()+t2.getCenterY())/2;
						}
					}
				}
			}
		}
		
		if(centerY != 999){
			return Utilities.inchesToMeters((A*1/(centerY-pictureHeight/2)+B) / 12);
		}
		return 0;
	}
}
