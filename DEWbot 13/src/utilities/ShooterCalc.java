package utilities;


public final class ShooterCalc 
{
	/*
	 * Tuning Constants - Experimentally Produced or Derived
	 * 
	 * Overall Decisions:
	 * 
	 * 
	 * In order of the ease of explanation:
	 * ACCELERATION_GRAVITY - g in m/s
	 * MAX_EXIT_VELOCITY - cap for speed output in m/s (shooter speed limit)
	 * MIN_EXIT_ANGLE - the minimum angle the hood can adjust to (as seen by the ball) in degrees
	 * MAX_EXIT_ANGLE - the maximum angle to hood can adjust to (as seen by the ball) in degrees
	 * MIN_ENTRANCE_ANGLE - the minimum angle of impact from the horizontal to boiler in degrees
	 * BOILER_HEIGHT_ABOVE_ROBOT - the height difference between the shooter and target in m
	 * BALL_RADIUS - radius of the ball in m
	 * BALL_MASS - mass of the ball in kg
	 * BALL_TERMINAL_VELOCITY - the terminal velocity of the ball in m/s
	 * 		- 13.02 via http://hyperphysics.phy-astr.gsu.edu/hbase/airfri2.html#c3
	 * 		- 10.16 via https://www.chiefdelphi.com/forums/showthread.php?t=153168&highlight=air+resistance
	 * TIME_MAX - the maximum time the algorithm would be skimming through if no solution presented itself
	 * 		-not REAL time, but for loop stepping time
	 * 
	 * K_AIR_CONSTANT - g / (terminalVelocity)^2 - the drag constant
	 * S_MAGNUS_CONSTANT - Experimentally Determined - Using:
	 * 		- http://www.seas.upenn.edu/~meam211/slides/aero.pdfd
	 * 		Can be Determined Also Through : But It Hasn't Worked Yet
	 * 		- http://www.physics.usyd.edu.au/~cross/TRAJECTORIES/42.%20Ball%20Trajectories.pdf
	 */
	
	//Setting Variables - Magnus Effect Calculations Require Air Resistance Calculations
	private static final boolean SHOOTER_CALC_NATIVE_FIT = true;
	private static final boolean USE_AIR_RESISTANCE = true && !SHOOTER_CALC_NATIVE_FIT;
	private static final boolean USE_MAGNUS_EFFECT = USE_AIR_RESISTANCE && true;
	private static final double BOILER_FRONT_TO_CENTER = 0.4826;
	
	private static final double ACCELERATION_GRAVITY = 9.8;
	private static final double MAX_EXIT_VELOCITY = 30;
	private static final double MIN_EXIT_ANGLE = 50;
	private static final double MAX_EXIT_ANGLE = 80;
	private static final double MIN_ENTRANCE_ANGLE = 20;
	private static final double BOILER_HEIGHT_ABOVE_ROBOT = 2;
	private static final double BALL_RADIUS = 0.0635;
	private static final double BALL_MASS = 0.0707;
	private static final double BALL_TERMINAL_VELOCITY = 10.16;
	private static final double TIME_MAX = 10;
	
	private static final double K_AIR_CONSTANT = ACCELERATION_GRAVITY / (BALL_TERMINAL_VELOCITY * BALL_TERMINAL_VELOCITY);
	private static final double S_MAGNUS_CONSTANT = 0.1;
	
	//Precision Variables - Determine the Effective 'Precision' of Each Loop
	private static final double PRECISION_ANGLE = 1;
	private static final double PRECISION_VELOCITY = 0.1;
	private static final double PRECISION_TIME = 0.01;
	
	
	/*
	 * Real-World Conversion Constants
	 * 
	 * In order of relative usefulness:
	 * SHOOTER_WHEEL_RADIUS - Radius of the Shooter Wheel (Meters)
	 * LINEAR_FRICTION_LOSS_COMSTANT - The ratio of a surface wheel velocity to a calculated exit velocity
	 */
	
	private static final double SHOOTER_WHEEL_RADIUS = 0.0762;
	private static final double LINEAR_FRICTION_LOSS_CONSTANT = (8.55560609516 / 21.74453355);
	
	/*
	 * Links for Algorithm Development:
	 * https://www.chiefdelphi.com/forums/showthread.php?t=153168&highlight=air+resistance
	 * https://www.desmos.com/calculator/sjhdgb7lri
	 * https://www.chiefdelphi.com/forums/showthread.php?t=127290&highlight=air+resistance
	 * https://www.chiefdelphi.com/forums/showthread.php?t=149224&highlight=modeling+ball+shooters
	 * https://en.wikipedia.org/wiki/Trajectory_of_a_projectile
	 * http://physics.eou.edu/opensource/physics/drag/drag_math.pdf
	 * http://math.mit.edu/~bush/wordpress/wp-content/uploads/2013/11/Beautiful-Game-2013.pdf
	 * http://ffden-2.phys.uaf.edu/211_fall2010.web.dir/Patrick_Brandon/what_is_the_magnus_effect.html
	 * http://www.physics.usyd.edu.au/~cross/TRAJECTORIES/42.%20Ball%20Trajectories.pdf
	 * https://www.chiefdelphi.com/forums/showthread.php?t=78779
	 * https://www.chiefdelphi.com/media/papers/2290?
	 * https://en.wikipedia.org/wiki/Magnus_effect
	 * http://file.scirp.org/pdf/WJM_2015041415401087.pdf
	 * http://www.seas.upenn.edu/~meam211/slides/aero.pdf7x
	 */
	
	private ShooterCalc() {};
	
	/**
	 * Calculates the Theoretical Optimal Angle for Shooting from a distanceX and heightDifferenceY
	 * @param distanceX - Distance to Target along the ground (Meters)
	 * @param heightDifferenceY - Difference in Height between the two objects (Meters)
	 * @return optimalShootingAngle - The Theoretical 'Optimal' Angle (degrees) to Shoot Angle, IF paired with an 'Optimal' Velocity
	 */
	private static double calculateOptimalShootingAngle(double distanceX, double heightDifferenceY)
	{
		//Makes the entrance angle greater, necessary for 'top-open' systems like hoops (or 'boilers')
		double optimalShootingAngle = 45;
		optimalShootingAngle += 0.5 * Math.toDegrees(Math.atan2(heightDifferenceY, distanceX));
		return optimalShootingAngle;
	}
	
	/**
	 * Calculates the Theoretical Optimal Velocity for Shooting
	 * @param distanceX - Distance to Target along the ground (Meters)
	 * @param heightDifferenceY - Difference in Height between the two objects (Meters)
	 * @return optimalExitVelocity - The optimal Shooter Exit Velocity to hit the goal (M/s)
	 */
	private static double calculateOptimalExitVelocity(double distanceX, double heightDifferenceY)
	{
		return Math.sqrt(ACCELERATION_GRAVITY * (heightDifferenceY + Math.sqrt( (distanceX * distanceX) + (heightDifferenceY * heightDifferenceY) )));
	}
	
	/**
	 * Calculates the Velocity Necessary to Reach a Goal from a Certain Angle (Non - Optimal)
	 * @param distanceX - Distance to Target along the ground (Meters)
	 * @param heightDifferenceY - Difference in Height between the two objects (Meters)
	 * @param theta - Angle (Degrees) of Ballistic / Shooter
	 * @return Necessary Velocity (Meters / Second)
	 */
	private static double calculateSetAngleVelocity(double distanceX, double heightDifferenceY, double theta)
	{
		return Math.sqrt(ACCELERATION_GRAVITY * (distanceX * distanceX) / (2 * Math.cos(Math.toRadians(theta)) * (Math.tan(Math.toRadians(theta)) * distanceX - heightDifferenceY)) );
	}
	
	/**
	 * Calculates the Angle Given a Velocity Necessary to Hit a Point (the Boiler)
	 * Assumes the Upper-more Angle is Preferred for the more realistic entrance angle
	 * @param distanceX - Distance to Target along the ground (Meters)
	 * @param heightDifferenceY - Difference in Height between the two objects (Meters)
	 * @param velocity - Current Shooter / Exit Velocity
	 * @return Necessary Angle (Degrees)
	 */
	private static double calculateSetVelocityAngle(double distanceX, double heightDifferenceY, double velocity)
	{
		return Math.toDegrees(Math.atan2(((velocity * velocity) + Math.sqrt(Math.pow(velocity, 4) - ACCELERATION_GRAVITY * (ACCELERATION_GRAVITY * distanceX * distanceX + 2 * heightDifferenceY * velocity * velocity) )), (ACCELERATION_GRAVITY * distanceX)));
	}
	
	/**
	 * Quickly Determines the Best Optimal Velocity Angle Pair for Idealistic Calculations
	 * @param distanceX - Distance to Target along the ground (Meters)
	 * @param heightDifferenceY - Difference in Height between the two objects (Meters)
	 * @param minAngle - Minimum Exit Angle Possible
	 * @param maxAngle - Maximum Exit Angle Possible
	 * @param maxVelocity - Maximum Velocity Possible
	 * @return
	 */
	private static double[] findOptimalShootingConditionsIdealistic(double distanceX, double heightDifferenceY, double minAngle, double maxAngle, double maxVelocity)
	{
		double[] returnData = new double[3];

		returnData[0] = calculateOptimalShootingAngle(distanceX, heightDifferenceY);
		returnData[1] = calculateOptimalExitVelocity(distanceX, heightDifferenceY);
		returnData[2] = 0;
		
		if(returnData[0] < minAngle)
		{
				returnData[0] = minAngle;
				returnData[1] = calculateSetAngleVelocity(distanceX, heightDifferenceY, minAngle);
		}
		else if(returnData[0] > maxAngle)
		{
			returnData[0] = maxAngle;
			returnData[1] = calculateSetAngleVelocity(distanceX, heightDifferenceY, maxAngle);
		}
			
		if(returnData[1] > maxVelocity)
		{
			returnData[1] = maxVelocity;
			returnData[0] = calculateSetVelocityAngle(distanceX, heightDifferenceY, maxVelocity);
				
			if(returnData[0] > minAngle || returnData[0] < maxAngle)
			{
				returnData[0] = 0;
				returnData[1] = 0;
				returnData[2] = Utilities.magnitude(distanceX, heightDifferenceY);
				
				return returnData;
			}
		}		
		
		return returnData;
	}
	
	/**
	 * Finds the Closest Point Set of Air Resistance Calculates
	 * @param distanceX - Distance in Meters
	 * @param heightDifferenceY - Height Difference Between Shooter And Target in Meters
	 * @param initialVelocity - Exit Velocity of Ball in M/s
	 * @param initialAngle - Exit Angle of Ball in Degrees
	 * @param minEntranceAngle - The Angle Target from the Horizontal
	 * @param constantOfAir - The 'K' Constant Of Air Resistance
	 * @param timeMax - Maximum Future Loop Time
	 * @param timeStep - Prevents Block Ignorance of Distance - 0.1 maximum
	 * @return The Closest Distance the Parameters Hit
	 */
	private static double findClosestPointSetAir(double distanceX, double heightDifferenceY, double initialVelocity, double initialAngle, double minEntranceAngle, double constantOfAir, double timeMax, double timeStep)
	{
		double lastDistanceFromPoint = Utilities.magnitude(distanceX, heightDifferenceY);
		double currentDistanceFromPoint = lastDistanceFromPoint;
		
		double lastX = 0;
		double lastY = 0;
		
		double lastVX = initialVelocity * Math.cos(Math.toRadians(initialAngle));
		double lastVY = initialVelocity * Math.sin(Math.toRadians(initialAngle));
		double lastV = initialVelocity;
		
		double lastTheta = Math.toDegrees(Math.atan2(lastVY, lastVX));
		
		double lastAX = -constantOfAir * lastVX * lastV;
		double lastAY = -ACCELERATION_GRAVITY - constantOfAir * lastVY * lastV;
		
		double time = 0;
		
		do{
			lastDistanceFromPoint = currentDistanceFromPoint;
			
			double AX = -constantOfAir * lastVX * lastV;
			double AY = -ACCELERATION_GRAVITY - constantOfAir * lastVY * lastV;
			
			double VX = lastVX + lastAX * timeStep;
			double VY = lastVY + lastAY * timeStep;
			
			double X = lastX + (VX + lastVX) / 2 * timeStep;
			double Y = lastY + (VY + lastVY) / 2 * timeStep;
			
			currentDistanceFromPoint = Utilities.magnitude((X - distanceX), (Y - heightDifferenceY));
			
			lastX = X;
			lastY = Y;
			
			lastVX = VX;
			lastVY = VY;
			lastV = Utilities.magnitude(VX, VY);
			lastTheta = Math.toDegrees(Math.atan2(VY, VX));
			
			lastAX = AX;
			lastAY = AY;
			
			time += timeStep;
		}
		while((lastDistanceFromPoint - currentDistanceFromPoint >= 0 || lastVY > 0 || lastTheta > -minEntranceAngle) && time < timeMax);
		
		return currentDistanceFromPoint;
	}
	
	/**
	 * Finding the Distance of the Closest Point - Magnus + Air
	 * @param distanceX - Distance in Meters
	 * @param heightDifferenceY - Height Difference Between Shooter And Target in Meters
	 * @param initialVelocity - Exit Velocity of Ball in M/s
	 * @param initialAngle - Exit Angle of Ball in Degrees
	 * @param minEntranceAngle - The Angle Target from the Horizontal
	 * @param constantOfAir - The 'K' Constant Of Air Resistance
	 * @param constantOfMagnusFriction - The 'S' Constant of the Magnus Force
	 * @param ballRadius - Ball Radius
	 * @param ballMass - Ball Mass
	 * @param timeMax - Maximum Loop Time
	 * @param timeStep - Time Stepping Accuracy
	 * @return The Distance of The Closest Point Given Air and Magnus Forces
	 */
	private static double findClosestPointSetMagnus(double distanceX, double heightDifferenceY, double initialVelocity, double initialAngle, double minEntranceAngle, double constantOfAir, double constantOfMagnusFriction, double ballRadius, double ballMass, double timeMax, double timeStep)
	{
		double lastDistanceFromPoint = Utilities.magnitude(distanceX, heightDifferenceY);
		double currentDistanceFromPoint = lastDistanceFromPoint;
		
		double lastX = 0;
		double lastY = 0;
		
		double lastVX = initialVelocity * Math.cos(Math.toRadians(initialAngle));
		double lastVY = initialVelocity * Math.sin(Math.toRadians(initialAngle));
		double lastV = initialVelocity;
		
		double lastTheta = Math.toDegrees(Math.atan2(lastVY, lastVX));
		
		double lastAX = -constantOfAir * lastVX * lastV;
		double lastAY = -ACCELERATION_GRAVITY - constantOfAir * lastVY * lastV;
		
		double time = 0;
		
		do{
			lastDistanceFromPoint = currentDistanceFromPoint;
			
			//http://ffden-2.phys.uaf.edu/211_fall2010.web.dir/Patrick_Brandon/what_is_the_magnus_effect.html
			
			double magnus = ((lastV / ballRadius) * constantOfMagnusFriction) / ballMass * timeStep;
			
			//Alternative Solution for Magnus - Currently Inoperable
			//double magnus = ((1 / (2 + (lastV / lastV))) * Utilities.magnitude(lastAX, lastAY) * (lastV * lastV / 2)) / ballMass;
			
			double AX = -constantOfAir * lastVX * lastV + (Math.cos(Math.toRadians(lastTheta)) * magnus);
			double AY = -ACCELERATION_GRAVITY - constantOfAir * lastVY * lastV + (Math.sin(Math.toRadians(lastTheta)) * magnus);
			
			double VX = lastVX + lastAX * timeStep;
			double VY = lastVY + lastAY * timeStep;
			
			double X = lastX + (VX + lastVX) / 2 * timeStep;
			double Y = lastY + (VY + lastVY) / 2 * timeStep;
			
			currentDistanceFromPoint = Utilities.magnitude((X - distanceX), (Y - heightDifferenceY));
			
			lastX = X;
			lastY = Y;
			
			lastVX = VX;
			lastVY = VY;
			lastV = Utilities.magnitude(VX, VY);
			lastTheta = Math.toDegrees(Math.atan2(VY, VX));
			
			lastAX = AX;
			lastAY = AY;
			
			time += timeStep;
		}
		while((lastDistanceFromPoint - currentDistanceFromPoint >= 0 || lastVY > 0 || lastTheta > -minEntranceAngle) && time < timeMax);
		
		return currentDistanceFromPoint;
	}
	
	/**
	 * Finds the Optimal Parameters for a Ball Shot
	 * @param distanceX - Distance in Meters
	 * @param heightDifferenceY - Height Difference Between Shooter And Target in Meters
	 * @param minAngle - Minimum Angle Possible
	 * @param maxAngle - Maximum Angle Possible
	 * @param minEntranceAngle - Minimum Angle from the Horizontal of Entrance
	 * @param maxVelocity - Maximum Velocity of Exit
	 * @param useMagnus - Whether to Compensate for the Magnus Effect
	 * @param precisionAngle - Angle Precision - Assume 1
	 * @param precisionVelocity - Velocity Precision - Assume 0.1
	 * @param timeStep - Time Precision - Assume 0.01
	 * @return A double Array of 0 - Angle, 1 - Velocity, 2 - Inaccuracy
	 */
	private static double[] findOptimalShootingConditionsImpedance(double distanceX, double heightDifferenceY, double minAngle, double maxAngle, double minEntranceAngle, double maxVelocity, boolean useMagnus, double precisionAngle, double precisionVelocity, double timeStep)
	{
		double[] returnData = new double[3];
		double currentFoundDistance = Utilities.magnitude(distanceX, heightDifferenceY);
		double optimalVelocity = calculateOptimalExitVelocity(distanceX, heightDifferenceY);
		double optimalAngle = calculateOptimalShootingAngle(distanceX, heightDifferenceY);
		
		//Default Values Should Optimization Prove Impossible - (Too Far, Too Fast, Too High, Too Low...)
		returnData[0] = 0;
		returnData[1] = 0;
		returnData[2] = currentFoundDistance;
		
		if(optimalAngle < minAngle)
		{
			optimalAngle = minAngle;
			optimalVelocity = calculateSetAngleVelocity(distanceX, heightDifferenceY, minAngle);
		}
		else if(optimalAngle > maxAngle)
		{
			optimalAngle = maxAngle;
			optimalVelocity = calculateSetAngleVelocity(distanceX, heightDifferenceY, maxAngle);
		}
		
		if(optimalVelocity > maxVelocity)
		{
			optimalVelocity = maxVelocity;
			optimalAngle = calculateSetVelocityAngle(distanceX, heightDifferenceY, maxVelocity);
			
			if(optimalAngle > maxAngle  || optimalAngle < minAngle)
			{
				return returnData;
			}
		}
		
		if(useMagnus)
		{
			for(double velocity = optimalVelocity; velocity < maxVelocity; velocity += precisionVelocity)
			{
				for(double angle = optimalAngle; angle < maxAngle; angle += precisionAngle)
				{
					currentFoundDistance = findClosestPointSetAir(distanceX, heightDifferenceY, velocity, angle, minEntranceAngle, K_AIR_CONSTANT, TIME_MAX, timeStep);
					
					if(currentFoundDistance < returnData[2])
					{
						returnData[0] = angle;
						returnData[1] = velocity;
						returnData[2] = currentFoundDistance;
					}
				}
			}
		}
		else
		{
			for(double velocity = optimalVelocity; velocity < maxVelocity; velocity += precisionVelocity)
			{
				for(double angle = optimalAngle; angle < maxAngle; angle += precisionAngle)
				{
					currentFoundDistance = findClosestPointSetMagnus(distanceX, heightDifferenceY, velocity, angle, minEntranceAngle, K_AIR_CONSTANT, S_MAGNUS_CONSTANT, BALL_RADIUS, BALL_MASS, TIME_MAX, timeStep);
					
					if(currentFoundDistance < returnData[2])
					{
						returnData[0] = angle;
						returnData[1] = velocity;
						returnData[2] = currentFoundDistance;
					}
				}
			}
		}
		
		return returnData;
	}
	
	/**
	 * Finds the Minimum Time Between Shots that Should Be Possible (Optimally, But Only Works for Air/Magnus Calcs
	 * @param distanceX - Distance in Meters
	 * @param heightDifferenceY - Height Difference Between Shooter And Target in Meters
	 * @param angle - Exit Angle in Degrees
	 * @param velocity - Exit Velocity in Meters/s
	 * @param useMagnus - Air / Magnus Calc Set
	 * @param ballTimePrecision - Ball Loop Time Step
	 * @param minBallDistance - Minimum Distance Between the Two Balls Allowed
	 * @param maxBallSpaceTime - Max Loop Time for the Testing Loop
	 * @param timeStep - The Time Step for the Testing Loops
	 * @return The Ideal (i.e. Stay ABOVE THIS) timing Between Ball Shots
	 */
	public static double findOptimalBallTiming(double distanceX, double heightDifferenceY, double angle, double velocity, boolean useMagnus, double ballTimePrecision, double minBallDistance, double maxBallSpaceTime, double timeStep)
	{
		double optimalBallTiming = 0;
		
		if(useMagnus)
		{
			for(optimalBallTiming = 0; optimalBallTiming < maxBallSpaceTime; optimalBallTiming += ballTimePrecision)
			{
				 if(findClosestDistanceBetweenBallsMagnus(distanceX, heightDifferenceY, velocity, angle, MIN_ENTRANCE_ANGLE, K_AIR_CONSTANT, S_MAGNUS_CONSTANT, BALL_RADIUS, BALL_MASS, optimalBallTiming, TIME_MAX, timeStep) > minBallDistance)
				 {
					 return optimalBallTiming;
				 }
			}
		}
		else
		{
			for(optimalBallTiming = 0; optimalBallTiming < maxBallSpaceTime; optimalBallTiming += ballTimePrecision)
			{
				 if(findClosestDistanceBetweenBallsAir(distanceX, heightDifferenceY, velocity, angle, MIN_ENTRANCE_ANGLE, K_AIR_CONSTANT, optimalBallTiming, TIME_MAX, timeStep) > minBallDistance)
				 {
					 return optimalBallTiming;
				 }
			}
		}
		
		return optimalBallTiming;
	}
	
	/**
	 * Finds the Closest Point the Balls Achieve to One Another - Air
	 * @param distanceX - Distance in Meters
	 * @param heightDifferenceY - Height Difference Between Shooter And Target in Meters
	 * @param initialVelocity - Exit Velocity of Ball in M/s
	 * @param initialAngle - Exit Angle of Ball in Degrees
	 * @param minEntranceAngle - The Angle Target from the Horizontal
	 * @param constantOfAir - The 'K' Constant Of Air Resistance
	 * @param timeBetweenShots - Time Between the Exit of Each Shot
	 * @param timeMax - Maximum Future Loop Time
	 * @param timeStep - Prevents Block Ignorance of Distance - 0.1 maximum
	 * @return The Closest the Two Stepper Balls Ever Become
	 */
	private static double findClosestDistanceBetweenBallsAir(double distanceX, double heightDifferenceY, double initialVelocity, double initialAngle, double minEntranceAngle, double constantOfAir, double timeBetweenShots, double timeMax, double timeStep)
	{
		double lastDistanceFromPointA = Utilities.magnitude(distanceX, heightDifferenceY);
		double currentDistanceFromPointA = lastDistanceFromPointA;
		//double lastDistanceFromPointB = lastDistanceFromPointA;
		//double currentDistanceFromPointB = lastDistanceFromPointA;
		
		double lastXA = 0;
		double lastYA = 0;
		double lastXB = 0;
		double lastYB = 0;
		
		double lastVXA = initialVelocity * Math.cos(Math.toRadians(initialAngle));
		double lastVYA = initialVelocity * Math.sin(Math.toRadians(initialAngle));
		double lastVA = initialVelocity;
		double lastVXB = initialVelocity * Math.cos(Math.toRadians(initialAngle));
		double lastVYB = initialVelocity * Math.sin(Math.toRadians(initialAngle));
		double lastVB = initialVelocity;
		
		double lastThetaA = Math.toDegrees(Math.atan2(lastVYA, lastVXA));
		//double lastThetaB = Math.toDegrees(Math.atan2(lastVYB, lastVXB));
		
		double lastAXA = -constantOfAir * lastVXA * lastVA;
		double lastAYA = -ACCELERATION_GRAVITY - constantOfAir * lastVYA * lastVA;
		double lastAXB = -constantOfAir * lastVXB * lastVB;
		double lastAYB = -ACCELERATION_GRAVITY - constantOfAir * lastVYB * lastVB;
		
		double time = 0;
		double closestBallDistance = currentDistanceFromPointA;
		
		do
		{
			lastDistanceFromPointA = currentDistanceFromPointA;
			
			double AXA = -constantOfAir * lastVXA * lastVA;
			double AYA = -ACCELERATION_GRAVITY - constantOfAir * lastVYA * lastVA;
			
			double VXA = lastVXA + lastAXA * timeStep;
			double VYA = lastVYA + lastAYA * timeStep;
			
			double XA = lastXA + (VXA + lastVXA) / 2 * timeStep;
			double YA = lastYA + (VYA + lastVYA) / 2 * timeStep;
			
			currentDistanceFromPointA = Utilities.magnitude((XA - distanceX), (YA - heightDifferenceY));
			
			lastXA = XA;
			lastYA = YA;
			
			lastVXA = VXA;
			lastVYA = VYA;
			lastVA = Utilities.magnitude(VXA, VYA);
			lastThetaA = Math.toDegrees(Math.atan2(VYA, VXA));
			
			lastAXA = AXA;
			lastAYA = AYA;
			
			time += timeStep;
			
		}
		while((lastDistanceFromPointA - currentDistanceFromPointA >= 0 || lastVYA > 0 || lastThetaA > -minEntranceAngle) && time < timeMax && time < timeBetweenShots);
		
		while((lastDistanceFromPointA - currentDistanceFromPointA >= 0 || lastVYA > 0 || lastThetaA > -minEntranceAngle) && time < timeMax && closestBallDistance > 0.001)
		{
			lastDistanceFromPointA = currentDistanceFromPointA;
			
			double AXA = -constantOfAir * lastVXA * lastVA;
			double AYA = -ACCELERATION_GRAVITY - constantOfAir * lastVYA * lastVA;
			
			double VXA = lastVXA + lastAXA * timeStep;
			double VYA = lastVYA + lastAYA * timeStep;
			
			double XA = lastXA + (VXA + lastVXA) / 2 * timeStep;
			double YA = lastYA + (VYA + lastVYA) / 2 * timeStep;
			
			currentDistanceFromPointA = Utilities.magnitude((XA - distanceX), (YA - heightDifferenceY));
			
			lastXA = XA;
			lastYA = YA;
			
			lastVXA = VXA;
			lastVYA = VYA;
			lastVA = Utilities.magnitude(VXA, VYA);
			lastThetaA = Math.toDegrees(Math.atan2(VYA, VXA));
			
			lastAXA = AXA;
			lastAYA = AYA;
			
			//lastDistanceFromPointB = currentDistanceFromPointB;
			
			double AXB = -constantOfAir * lastVXB * lastVB;
			double AYB = -ACCELERATION_GRAVITY - constantOfAir * lastVYB * lastVB;
			
			double VXB = lastVXB + lastAXB * timeStep;
			double VYB = lastVYB + lastAYB * timeStep;
			
			double XB = lastXB + (VXB + lastVXB) / 2 * timeStep;
			double YB = lastYB + (VYB + lastVYB) / 2 * timeStep;
			
			//currentDistanceFromPointB = Utilities.magnitude((XB - distanceX), (YB - heightDifferenceY));
			
			lastXB = XB;
			lastYB = YB;
			
			lastVXB = VXB;
			lastVYB = VYB;
			lastVB = Utilities.magnitude(VXB, VYB);
			//lastThetaB = Math.toDegrees(Math.atan2(VYB, VXB));
			
			lastAXB = AXB;
			lastAYB = AYB;
			
			System.out.println("XA " + XA);
			System.out.println("XB " + XB);
			
			double closestBallDistanceTemp = Utilities.magnitude((XB - XA), (YB - YA));
			if(closestBallDistanceTemp < closestBallDistance)
			{
				closestBallDistance = closestBallDistanceTemp;
			}
			time += timeStep;
		}
		
		return closestBallDistance;
	}
	
	/**
	 * Finds the Closest Point the Balls Achieve to One Another - Magnus
	 * @param distanceX - Distance in Meters
	 * @param heightDifferenceY - Height Difference Between Shooter And Target in Meters
	 * @param initialVelocity - Exit Velocity of Ball in M/s
	 * @param initialAngle - Exit Angle of Ball in Degrees
	 * @param minEntranceAngle - The Angle Target from the Horizontal
	 * @param constantOfAir - The 'K' Constant Of Air Resistance
	 * @param constantOfMagnusFriction - The 'S' Constant of the Magnus Force
	 * @param ballRadius - Ball Radius
	 * @param ballMass - Ball Mass
	 * @param timeBetweenShots - Time Between the Exit of Each Shot
	 * @param timeMax - Maximum Loop Time
	 * @param timeStep - Time Stepping Accuracy
	 * @return The Closest the Two Stepper Balls Ever Become
	 */
	private static double findClosestDistanceBetweenBallsMagnus(double distanceX, double heightDifferenceY, double initialVelocity, double initialAngle, double minEntranceAngle, double constantOfAir, double constantOfMagnusFriction, double ballRadius, double ballMass, double timeBetweenShots, double timeMax, double timeStep)
	{
		double lastDistanceFromPointA = Utilities.magnitude(distanceX, heightDifferenceY);
		double currentDistanceFromPointA = lastDistanceFromPointA;
		double lastDistanceFromPointB = lastDistanceFromPointA;
		double currentDistanceFromPointB = lastDistanceFromPointA;
		
		double lastXA = 0;
		double lastYA = 0;
		double lastXB = 0;
		double lastYB = 0;
		
		double lastVXA = initialVelocity * Math.cos(Math.toRadians(initialAngle));
		double lastVYA = initialVelocity * Math.sin(Math.toRadians(initialAngle));
		double lastVA = initialVelocity;
		double lastVXB = initialVelocity * Math.cos(Math.toRadians(initialAngle));
		double lastVYB = initialVelocity * Math.sin(Math.toRadians(initialAngle));
		double lastVB = initialVelocity;
		
		double lastThetaA = Math.toDegrees(Math.atan2(lastVYA, lastVXA));
		double lastThetaB = Math.toDegrees(Math.atan2(lastVYB, lastVXB));
		
		double lastAXA = -constantOfAir * lastVXA * lastVA;
		double lastAYA = -ACCELERATION_GRAVITY - constantOfAir * lastVYA * lastVA;
		double lastAXB = -constantOfAir * lastVXB * lastVB;
		double lastAYB = -ACCELERATION_GRAVITY - constantOfAir * lastVYB * lastVB;
		
		double time = 0;
		double closestBallDistance = currentDistanceFromPointA;
		
		do
		{
			lastDistanceFromPointA = currentDistanceFromPointA;
			
			//Alternative Solution for Magnus - Currently Inoperable
			//double magnus = ((1 / (2 + (lastV / lastV))) * Utilities.magnitude(lastAX, lastAY) * (lastV * lastV / 2)) / ballMass;
			
			double magnusA = ((lastVA / ballRadius) * constantOfMagnusFriction) / ballMass * timeStep;
			
			double AXA = -constantOfAir * lastVXA * lastVA + (Math.cos(Math.toRadians(lastThetaA)) * magnusA);
			double AYA = -ACCELERATION_GRAVITY - constantOfAir * lastVYA * lastVA + (Math.sin(Math.toRadians(lastThetaA)) * magnusA);
			
			double VXA = lastVXA + lastAXA * timeStep;
			double VYA = lastVYA + lastAYA * timeStep;
			
			double XA = lastXA + (VXA + lastVXA) / 2 * timeStep;
			double YA = lastYA + (VYA + lastVYA) / 2 * timeStep;
			
			currentDistanceFromPointA = Utilities.magnitude((XA - distanceX), (YA - heightDifferenceY));
			
			lastXA = XA;
			lastYA = YA;
			
			lastVXA = VXA;
			lastVYA = VYA;
			lastVA = Utilities.magnitude(VXA, VYA);
			lastThetaA = Math.toDegrees(Math.atan2(VYA, VXA));
			
			lastAXA = AXA;
			lastAYA = AYA;
			
			time += timeStep;
			
		}
		while((lastDistanceFromPointA - currentDistanceFromPointA >= 0 || lastVYA > 0 || lastThetaA > -minEntranceAngle) && time < timeMax && time < timeBetweenShots);
		
		while((lastDistanceFromPointA - currentDistanceFromPointA >= 0 || lastVYA > 0 || lastThetaA > -minEntranceAngle) && time < timeMax && closestBallDistance > 0.001 && (lastDistanceFromPointB - currentDistanceFromPointB >= 0 || lastVYB > 0 || lastThetaB > -minEntranceAngle))
		{
			lastDistanceFromPointA = currentDistanceFromPointA;
			
			double magnusA = ((lastVA / ballRadius) * constantOfMagnusFriction) / ballMass * timeStep;
			
			double AXA = -constantOfAir * lastVXA * lastVA + (Math.cos(Math.toRadians(lastThetaA)) * magnusA);
			double AYA = -ACCELERATION_GRAVITY - constantOfAir * lastVYA * lastVA + (Math.sin(Math.toRadians(lastThetaA)) * magnusA);
			
			double VXA = lastVXA + lastAXA * timeStep;
			double VYA = lastVYA + lastAYA * timeStep;
			
			double XA = lastXA + (VXA + lastVXA) / 2 * timeStep;
			double YA = lastYA + (VYA + lastVYA) / 2 * timeStep;
			
			currentDistanceFromPointA = Utilities.magnitude((XA - distanceX), (YA - heightDifferenceY));
			
			lastXA = XA;
			lastYA = YA;
			
			lastVXA = VXA;
			lastVYA = VYA;
			lastVA = Utilities.magnitude(VXA, VYA);
			lastThetaA = Math.toDegrees(Math.atan2(VYA, VXA));
			
			lastAXA = AXA;
			lastAYA = AYA;
			
			lastDistanceFromPointB = currentDistanceFromPointB;
			
			double magnusB = ((lastVB / ballRadius) * constantOfMagnusFriction) / ballMass * timeStep;
			
			double AXB = -constantOfAir * lastVXB * lastVB + (Math.cos(Math.toRadians(lastThetaB)) * magnusB);
			double AYB = -ACCELERATION_GRAVITY - constantOfAir * lastVYB * lastVB + (Math.sin(Math.toRadians(lastThetaB)) * magnusB);
			
			double VXB = lastVXB + lastAXB * timeStep;
			double VYB = lastVYB + lastAYB * timeStep;
			
			double XB = lastXB + (VXB + lastVXB) / 2 * timeStep;
			double YB = lastYB + (VYB + lastVYB) / 2 * timeStep;
			
			currentDistanceFromPointB = Utilities.magnitude((XB - distanceX), (YB - heightDifferenceY));
			
			lastXB = XB;
			lastYB = YB;
			
			lastVXB = VXB;
			lastVYB = VYB;
			lastVB = Utilities.magnitude(VXB, VYB);
			lastThetaB = Math.toDegrees(Math.atan2(VYB, VXB));
			
			lastAXB = AXB;
			lastAYB = AYB;
			
			double closestBallDistanceTemp = Utilities.magnitude((XB - XA), (YB - YA));
			if(closestBallDistanceTemp < closestBallDistance)
			{
				closestBallDistance = closestBallDistanceTemp;
			}
			time += timeStep;
		}
		
		return closestBallDistance;
	}
	
	/**
	 * Finds All Shooting Parameters Based on Distance and Constants
	 * @param distance - Distance to Target
	 * @return double Array of 0 - Angle, 1 - Exit Velocity, 2 - Theoretical Inaccuracy
	 */
	public static double[] findOptimalShootingConditions(double distance)
	{
		double[] returnData = new double[3];
		
		if(!useNativeFit())
		{
			if(!USE_AIR_RESISTANCE)
			{
				returnData = findOptimalShootingConditionsIdealistic(distance, BOILER_HEIGHT_ABOVE_ROBOT, MIN_EXIT_ANGLE, MAX_EXIT_ANGLE, MAX_EXIT_VELOCITY);
			}
			else
			{
				returnData = findOptimalShootingConditionsImpedance(distance, BOILER_HEIGHT_ABOVE_ROBOT, MIN_EXIT_ANGLE, MAX_EXIT_ANGLE, MIN_ENTRANCE_ANGLE, MAX_EXIT_VELOCITY, USE_MAGNUS_EFFECT, PRECISION_ANGLE, PRECISION_VELOCITY, PRECISION_TIME);
			}
		}
		else
		{
			returnData = fastFindOptimalShootingConditions(distance);
		}
		
		return returnData;
	}
	
	/**
	 * Produces Fast Estimated Results Using Quick Taylor Series Estimations
	 * 		- Reset Fast Find Approximation Equations Whenever a Constant is Altered
	 * @param distance - Distance to Target
	 * @return double Array of 0 - Angle, 1 - Exit Velocity, 2 - Theoretical Inaccuracy (0 or Distance)
	 */
	public static double[] fastFindOptimalShootingConditions(double distance)
	{
		double[] returnData = new double[3];
		
		returnData[0] = 0;
		returnData[1] = 0;
		
		//Fast Find Cannot Easily Check Accuracy Without Sacrificing The Whole 'Fast' Part
		returnData[2] = Utilities.magnitude(distance, BOILER_HEIGHT_ABOVE_ROBOT);
		
		if(!useNativeFit())
		{
			if(distance >= 0.01 && distance <= 10)
			{
				//Fast Find Cannot Easily Check Accuracy Without Sacrificing The Whole 'Fast' Part
				returnData[2] = 0;
			
				//https://docs.google.com/spreadsheets/d/1w-xfE697yoKkYCJSfL_wq_sTZL_X_8LEyKl_IW81o1o/edit?usp=sharing
				if(!USE_AIR_RESISTANCE)
				{
					//Already is Fast
					returnData = findOptimalShootingConditionsIdealistic(distance, BOILER_HEIGHT_ABOVE_ROBOT, MIN_EXIT_ANGLE, MAX_EXIT_ANGLE, MAX_EXIT_VELOCITY);
				}
				else if(!USE_MAGNUS_EFFECT)
				{
					//From A1-1 using 10.16 and 0.1 for Vel and Angle Precision, and 0.01 for Time Precision
					returnData[0] = 1.325 * distance * distance - 12.334 * distance + 89.728;
					returnData[1] = 0.098 * distance * distance + 0.274 * distance + 6.818;
				}
				else
				{
					//From M2-2 Using 10.16 as T, 0.1 as Magnus Constant, and 0.01 for each precision
					returnData[0] = 1.189 * distance * distance - 12.1 * distance + 89.066;
					//From M3-1 Using 10.16
					//returnData[1] = 0.099 * distance * distance + 0.376 * distance + 7.115;
					returnData[1] = 0.08 * distance * distance + 0.502 * distance + 6.816;
				}
			}
			else
			{
				//Not Within Range of Fast Find
			}
		}
		else
		{
			if(distance >= 0.01 && distance <= 10)
			{
				returnData = nativeMappedOptimalShootingConditions(distance);
			}
			else
			{
				//Not Within Range of Fast Find
			}
		}
		
		return returnData;
	}
	
	/**
	 * Returns the Curve-Fit Data Of the Shooter RPM and Angle
	 * Needs to Be Determined Entirely Experimentally
	 * @param distance - Distance to Target
	 * @return  the Curve-Fit Data Of the Shooter RPM and Angle at a Distance
	 */
	private static double[] nativeMappedOptimalShootingConditions(double distance)
	{
		double[] returnData = new double[3];
		
		//Based off of Native Units (Servo Angles and RPM from ShooterIO) - Needs Point-By-Point Testing to Work
		//Currently these are simple linear fits, but their direct mapping means that they must work properly at, at the very least, the two point we already know work
		returnData[0] = 96.332 * distance + 2497.657;
		returnData[1] = -57.8 * distance + 246.406;
		
		//Utilizes fast find to range-check the distance for sanity of the input
		returnData[2] = fastFindOptimalShootingConditions(distance)[2];
		
		return returnData;
	}
	
	/**
	 * Does a Simple Linear Conversion Between The Exit Velocity and the Linear Velocity of the Surface of the Shooter Wheel 
	 * @param theoretical - Given Ball Exit Velocity
	 * @return Linear Surface Velocity of the Fly-Wheel
	 */
	private static double theoreticalExitVelocityToSurfaceWheelSpeed(double theoretical)
	{
		return (theoretical / LINEAR_FRICTION_LOSS_CONSTANT);
	}
	
	/**
	 * Turns an Optimal Exit Velocity to Rotational Per Minute for PID Purposes
	 * @param exitVelocity - Given Ball Exit Velocity
	 * @return The RPM Needed (I hope) to Achieve to the Given Exit Velocity
	 */
	public static double convertToRPM(double exitVelocity)
	{
		return (theoreticalExitVelocityToSurfaceWheelSpeed(exitVelocity) / (2 * Math.PI * SHOOTER_WHEEL_RADIUS)) * 60;
	}
	
	/**
	 * Whether to Avoid Conversion Units For Native-Mapped Fit Curves
	 * @return That ^
	 */
	public static boolean useNativeFit()
	{
		return SHOOTER_CALC_NATIVE_FIT;
	}
	
	/**
	 * Returns Distance (Meters) from the Base Of the Boiler to Its Center
	 * @return That ^
	 */
	public static double frontToCenterDistance()
	{
		return BOILER_FRONT_TO_CENTER;
	}
}