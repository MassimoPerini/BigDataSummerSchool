package bigdata.dbscan.rtree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Point;

public class DBScan
{
	
	private RTree<MyPoint, Point> points;
	private Vector<Cluster> clusters;
	
	public DBScan(RTree<MyPoint, Point> points) {
		super();
		this.points = points;
		clusters=new Vector<Cluster> ();
	}
	
	public void dbScan (double eps, int minPts)
	{
		Iterator<Entry<MyPoint, Point>> results = points.entries().toBlocking().toIterable().iterator();
		while(results.hasNext())
		{
			Entry<MyPoint, Point> p=results.next();
			
			if (!p.value().isVisited())
			{
				p.value().setVisited(true);
				LinkedList<Entry<MyPoint, Point>> neighb=regionQuery(p.geometry(), eps);
				int sizeNeigh=neighb.size();
				if (sizeNeigh<minPts)
				{
					p.value().setNoise(true);
				}
				else
				{
					Cluster c=new Cluster();
					clusters.add(c);
					// -------------
					addPointToCluster(p,c);
					
					
					while(neighb.size()>0)
					{
						Entry<MyPoint, Point> pNeigh=neighb.get(0);
						if (!pNeigh.value().isVisited())
						{
							pNeigh.value().setVisited(true);
							
							LinkedList<Entry<MyPoint, Point>> neighb2=regionQuery(pNeigh.geometry(), eps);
							if (neighb2.size()>=minPts)
							{
								neighb.addAll(neighb2);
							}
						}
						if (pNeigh.value().getCluster()==null)
						{
							pNeigh.value().setNoise(false);
							addPointToCluster(pNeigh,c);
						}
						neighb.remove(0);

					}
				}
			}
		}
	}
	
	private void addPointToCluster (Entry<MyPoint, Point> p, Cluster c)
	{
		p.value().setCluster(c);
		c.addPoint(p);
	}
	
	private LinkedList<Entry<MyPoint, Point>> regionQuery(Point pLoc, double eps)
	{
		Iterator<Entry<MyPoint, Point>> results =points.search(pLoc,eps).toBlocking().toIterable().iterator();
		LinkedList <Entry<MyPoint, Point>> r=new LinkedList <Entry<MyPoint, Point>> ();
		while (results.hasNext())
		{
			r.add(results.next());
		}
		
		return r;
	/*	LinkedList <Entry<MyPoint, Point>> list=new LinkedList <Entry<MyPoint, Point>> ();
		
		while(results.hasNext())
		{
			 list.add(results.next());
		}
		return list;*/
	}

	public Vector<Cluster> getClusters() {
		return clusters;
	}
}