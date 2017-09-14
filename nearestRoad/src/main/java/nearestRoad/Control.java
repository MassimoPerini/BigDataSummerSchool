package nearestRoad;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.PathIterator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;
import watford.util.quadtree.QuadTree;

class Wrapper{
	private static QuadTree kqt;

	public static QuadTree getKqt() {
		return kqt;
	}

	public static void setKqt(QuadTree kqt) {
		Wrapper.kqt = kqt;
	}
	
}

public class Control {

	public static void main (String [] args)
	{
		SparkConf conf=new SparkConf().setAppName("hw3");
		JavaSparkContext sc=new JavaSparkContext(conf);
		JavaRDD<String> txtCar=sc.textFile(args[2]);
		JavaRDD <Car> parCar=txtCar.map(new Function<String, Car>(){
			public Car call(String v1) throws Exception {
				return Parser.parseCars(v1);
			}
			
		}).cache();
	//	LinkedList<Car> cars = Parser.parseCars(args[2]);
	//	Wrapper.setKqt(Parser.parseRoad(args[0], args[1]));
		final QuadTree kqt=Parser.parseRoad(args[0], args[1]);
	//	JavaRDD <Car> parCar=sc.parallelize(cars);
		JavaRDD<String> pairs = parCar.map(new Function<Car, String>() {
			private static final long serialVersionUID = 1L;
			public String call(Car arg0) throws Exception {
				float[] res=compute(arg0, kqt);
				String r=arg0.getId()+" "+res[0]+" "+res[1];
				return r;
			}
        });

		pairs.saveAsTextFile(args[3]);
	}
	
	public static float[] compute (Car c, QuadTree kqt)
	{
		Rectangle rect=new Rectangle(c.getX()-5, c.getY()-5, 5,5);	//Square 10x10 centered in c
		if (kqt==null)
			System.out.println("NULL QUADTREE");
		System.out.println("RECTANGLE "+rect);
		int sRes=kqt.objectsInside(null, rect);
		System.out.println("COMPUTATION: "+sRes+"\n");
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
		
		float[] r=findNearestRoad(c, result);
		return r;
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
	
	public static float[] findNearestRoad (Car c, LinkedList s)
	{
		Iterator i=s.iterator();
		int index=0;
		int bestIndex=0;
		double bestD=Double.MAX_VALUE;
		while (i.hasNext())
		{
			Road r=(Road) i.next();
			PathIterator i2=r.points().getPathIterator(null);
			float[] coords = new float[6];
			i2.currentSegment(coords);
			double d=Util.getDistanceToSegment((int)coords[0], (int)coords[1], r.getLastX(), r.getLastY(), (int)c.getPoint().getX(), (int)c.getPoint().getY());

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
		Point p=Util.getClosestPointOnSegment((int)coords[0], (int)coords[1], bestRoad.getLastX(), bestRoad.getLastY(), (int)c.getPoint().getX(), (int)c.getPoint().getY());
		float[]res=new float[2];
		res[0]=(float) (p.getX()/(float)1000000);
		res[1]=(float)(p.getY()/(double)1000000);
		return res;
	}
	
}
