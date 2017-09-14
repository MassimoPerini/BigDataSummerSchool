package bigdata.dbscan.rtree;

public class MyPoint
{
	private boolean noise;
	private boolean visited;
	private String id;
	private Cluster cluster;
	
	public MyPoint (String id)
	{
		this.id=id;
		this.noise=false;
		this.visited=false;
		cluster=null;
	}
	

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
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

