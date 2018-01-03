package org.usfirst.frc.team1640.constants;

public class DimensionConstants {
	
	//WHEELS
		public static final double WHEEL_COUNTS_PER_ROTATION = 64;
		public static final double WHEEL_RADIUS = 2.0; // inches
		public static final double WHEEL_CIRCUMFERENCE = 2*Math.PI*WHEEL_RADIUS; // inches
	
	//DIMENSIONS
		public static final double LENGTH = 29; // inches
		public static final double WIDTH = 33; // inches
		public static final double DIAGONAL = Math.sqrt(Math.pow(WIDTH, 2) + Math.pow(LENGTH, 2)); // inches
		public static final double WIDTH_TO_DIAGONAL_RATIO = WIDTH / DIAGONAL;
		public static final double LENGTH_TO_DIAGONAL_RATIO = LENGTH / DIAGONAL;
		
}