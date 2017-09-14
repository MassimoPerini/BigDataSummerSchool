package bigdata.dbscan.vector;

public class Point
{
	private boolean noise;
	private boolean visited;
	private double x,y;
	private String id;
	private Cluster cluster;
	
	public Point (String id, double x, double y)
	{
		this.x=x;
		this.y=y;
		this.id=id;
		this.noise=false;
		this.visited=false;
		cluster=null;
	}
	
	public float distance (Point p)
	{
		return (float) Math.sqrt(Math.pow(this.x-p.getX(), 2)+Math.pow(this.y-p.getY(), 2));
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public double getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean isNoise() {
		return noise;
	}

	public void setNoise(boolean noise) {
		this.noise = noise;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster c) {
		this.cluster = c;
	}
	
	
}
