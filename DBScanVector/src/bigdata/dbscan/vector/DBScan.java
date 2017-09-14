package bigdata.dbscan.vector;

import java.util.LinkedList;
import java.util.Vector;

public class DBScan
{
	
	private Vector <Point> points;
	private Vector<Cluster> clusters;
	
	public DBScan(Vector<Point> points) {
		super();
		this.points = points;
		clusters=new Vector<Cluster> ();
	}
	
	public void dbScan (double eps, int minPts)
	{
		System.out.println("Start DBSCAN");
		int size=points.size();
		for (int i=0;i<size;i++)
		{
			Point p=points.get(i);
		//	System.out.println("Point "+p.getId());
			if (!p.isVisited())
			{
				p.setVisited(true);
				LinkedList <Point> neighb=regionQuery(p, eps);
				int sizeNeigh=neighb.size();
				if (sizeNeigh<minPts)
				{
					p.setNoise(true);
				}
				else
				{
					Cluster c=new Cluster();
					clusters.add(c);
					// -------------
					addPointToCluster(p,c);
					while (neighb.size()>0)
					{
						Point pNeigh=neighb.get(0);
				//		System.out.println("         Point neigh:"+pNeigh.getId());
						if (!pNeigh.isVisited())
						{
							pNeigh.setVisited(true);
							LinkedList <Point> neighb2=regionQuery(pNeigh, eps);
							if (neighb2.size()>=minPts)
							{
								neighb.addAll(neighb2);
							//	System.out.println("         New size: "+neighb.size());
							}
						}
						if (pNeigh.getCluster()==null)
						{
							pNeigh.setNoise(false);
							addPointToCluster(pNeigh,c);
							
						}
						neighb.remove(0);
					}
				}
			}
		}
	}
	
	private void addPointToCluster (Point p, Cluster c)
	{
		p.setCluster(c);
		c.getPoints().add(p);
	}
	
	private LinkedList<Point> regionQuery(Point p, double eps)
	{
		LinkedList<Point> result=new LinkedList <Point> ();
		int size=points.size();
		Point p1;
		for (int i=0;i<size;i++)
		{
			p1=points.get(i);
			if (p.distance(p1)<eps)
			{
		//		System.out.println("         D: "+p.distance(p1));
				result.add(p1);
			}
		}
		return result;
	}

	public Vector<Cluster> getClusters() {
		return clusters;
	}

}
