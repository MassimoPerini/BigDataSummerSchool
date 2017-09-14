package bigdata.dbscan.vector;

import java.util.Vector;

public class Cluster
{
	private int id;
	private static int autoId=0;
	private Vector<Point> points;
	
	public Cluster()
	{
		points=new Vector<Point> ();
		this.id=autoId;
		autoId++;
	}
	
	public void addPoint (Point p)
	{
		points.add(p);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vector<Point> getPoints() {
		return points;
	}

	public void setPoints(Vector<Point> points) {
		this.points = points;
	}	
}