package bigdata.dbscan.rtree;

import java.util.Vector;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Point;

public class Cluster
{
	private int id;
	private static int autoId=0;
	private Vector<Entry<MyPoint, Point>> points;
	
	public Cluster()
	{
		points=new Vector<Entry<MyPoint, Point>> ();
		this.id=autoId;
		autoId++;
	}
	
	public void addPoint (Entry<MyPoint, Point> p)
	{
		points.add(p);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vector<Entry<MyPoint, Point>> getPoints() {
		return points;
	}

	public void setPoints(Vector<Entry<MyPoint, Point>> points) {
		this.points = points;
	}	
}
