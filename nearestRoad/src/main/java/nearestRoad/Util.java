
package nearestRoad;

import java.awt.Point;

public class Util{
	
	private static double px;
	public static double getPx() {
		return px;
	}

	public static double getPy() {
		return py;
	}

	private static double py;
	
	public static double distanceFromLine(double cx, double cy, double ax, double ay , double bx, double by)
	{
	
	double r_numerator = (cx-ax)*(bx-ax) + (cy-ay)*(by-ay);
	double r_denomenator = (bx-ax)*(bx-ax) + (by-ay)*(by-ay);
	double r = r_numerator / r_denomenator;
	//
	px = ax + r*(bx-ax);
	py = ay + r*(by-ay);
	//
	double s =  ((ay-cy)*(bx-ax)-(ax-cx)*(by-ay) ) / r_denomenator;
	
	double distanceLine = Math.abs(s)*Math.sqrt(r_denomenator);
	return distanceLine;
	}
}

