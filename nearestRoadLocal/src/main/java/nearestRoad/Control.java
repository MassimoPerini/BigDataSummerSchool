package nearestRoad;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.PathIterator;
import java.util.Iterator;
import java.util.LinkedList;

import watford.util.quadtree.QuadTree;

public class Control {
	
	public static void main (String [] args)
	{
		LinkedList<Car> cars = Parser.parseCars("ucarBeijing-small.csv");
		QuadTree kqt = Parser.parseRoad("road-node-small.csv", "road-edge-small.csv");
		
		for (int i=0;i<cars.size();i++){
			Car c=cars.get(i);
			Rectangle rect=new Rectangle(c.getX()-5, c.getY()-5, 5,5);	//Square 10x10 centered in c
			
			int sRes=kqt.objectsInside(null, rect);
			Rectangle min=new Rectangle(rect);
			while (sRes==0){
				min=new Rectangle(rect);
				rect.grow((int)rect.getWidth(), (int)rect.getHeight());
				sRes=kqt.objectsInside(null, rect);
			}
			Rectangle max=new Rectangle (rect);
			while (sRes>1 && rect.getHeight()>1)
			{
				max=new Rectangle (rect);
				rect.grow((int)(-rect.getWidth()/2), (int) (-rect.getHeight()/2));
				sRes=kqt.objectsInside(null, rect);
			}
			
			if (sRes==0){
				rect=binarySpatialSearch(kqt, min, max);
			}
			
			int rad=(int) (Math.sqrt(2*Math.pow(rect.getHeight()/2,2))-(rect.getHeight()/2)+1);
		//	System.out.println(rect.getHeight()+" GROW "+rad);
			rect.grow(rad, rad);
			LinkedList result=new LinkedList();
			kqt.objectsInside(result, rect);
			
			double[] r=findNearestRoad(c, result);
			System.out.println(c.getId()+":  "+ r[0]+", "+r[1]);
			
		}
		System.out.println("END");
	}
	
	public static Rectangle binarySpatialSearch(QuadTree kqt, Rectangle min, Rectangle max)
	{
		Rectangle mid=new Rectangle();
		int result=0;
		while(max.getBounds().getHeight()-1>min.getBounds().getHeight()){
			mid=new Rectangle((min.x+max.x)/2, (min.y+max.y)/2, (min.width+max.width)/2, (min.height+max.height)/2);
			result=kqt.objectsInside(null, mid);
			if (result==0)
			{
				min=new Rectangle(mid);
			}
			if (result>1)
			{
				max=new Rectangle(mid);
			}
			if (result==1)
				return mid;
	//		System.out.print(result+", ");
		}
//		System.out.println("\n");
		return max;
	}
	
	public static double[] findNearestRoad (Car c, LinkedList s)
	{
		Iterator i=s.iterator();
		int index=0;
		int bestIndex=0;
		double bestD=Double.MAX_VALUE;
		while (i.hasNext())
		{
			Road r=(Road) i.next();
			PathIterator i2=r.points().getPathIterator(null);
			double[] coords = new double[6];
			i2.currentSegment(coords);
			double d=Util.distanceFromLine(c.getRealX(), c.getRealY(), coords[0], coords[1], r.getLastX(), r.getLastY());

			if (d<bestD){
				bestIndex=index;
				bestD=d;
			}
			
			index++;
		}
		Road bestRoad=(Road) s.get(bestIndex);
		PathIterator i2=bestRoad.points().getPathIterator(null);
		float[] coords = new float[6];
		i2.currentSegment(coords);
		double d=Util.distanceFromLine(c.getRealX(), c.getRealY(), coords[0], coords[1], bestRoad.getLastX(), bestRoad.getLastY());
		double[]res=new double[2];
		res[0]=Util.getPx();
		res[1]=Util.getPy();
		return res;
	}
	
}
