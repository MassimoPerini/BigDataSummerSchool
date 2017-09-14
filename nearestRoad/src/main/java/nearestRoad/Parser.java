package nearestRoad;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import watford.test.quadtree.TestConfig;
import watford.util.quadtree.QuadTree;

public class Parser {
	
	static int maxX=Integer.MIN_VALUE;
	static int maxY=Integer.MIN_VALUE;
	static int minX=Integer.MAX_VALUE;
	static int minY=Integer.MAX_VALUE;
	
	public static QuadTree parseRoad (String pathPoint, String pathRow)
	{

		try {
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(conf);
			FSDataInputStream a = fs.open(new Path(pathPoint));
			BufferedReader br = new BufferedReader(new InputStreamReader(a));
			String line="";
			HashMap <String, int[]>rowIntersec=new HashMap <String, int[]>();
			
			while ((line=br.readLine())!=null)
			{
				String [] val=line.split(",");
		/*		if (val[0].equals("1210108"))
					System.out.println(val[2]+"    "+Float.parseFloat(val[2])*1000000);*/
				int [] v={(int) (Double.parseDouble(val[1])*1000000), (int) (Double.parseDouble(val[2])*1000000)};
				if (v[0]>maxX)
					maxX=(int)v[0];
				if (v[0]<minX)
					minX=(int)v[0];
				if (v[1]>maxY)
					maxY=(int)v[1];
				if (v[1]<minY)
					minY=(int)v[1];
				
				rowIntersec.put(val[0], v);
			}
			br.close();
			a.close();
			a = fs.open(new Path(pathRow));
			br = new BufferedReader(new InputStreamReader(a));
			//upper-left, width, heigh
			QuadTree quadTree= new QuadTree(new Rectangle(minX-1, minY-1,(maxX-minX)+1,(maxY-minY)+1), TestConfig.QUADTREE_BUCKET);
		//	int i=0;
			while ((line=br.readLine())!=null)
			{
				String [] val=line.split(",");
				int [] startRoad=rowIntersec.get(val[1]);
				int [] endRoad=rowIntersec.get(val[2]);
	/*			if (startRoad[0]==116246296 && startRoad[1]==39971456)
					System.out.println("i: "+i+" , "+startRoad[1]+" - "+endRoad[0]+", "+endRoad[1]);*/
				Road r=new Road(val[0], Double.parseDouble(val[3]));
				r.add(startRoad[0],startRoad[1]);
				r.add(endRoad[0], endRoad[1]);
				quadTree.add(r);
		//		i++;
			}
			br.close();
			a.close();
			System.out.println("Finished parsing... ");
			return quadTree;
		} 
		catch (FileNotFoundException e) {
			System.out.println(e);
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return null;
	}
	
	public static Car parseCars(String line)
	{
		
				String [] val=line.split(",");
				int x=(int)(Float.parseFloat(val[2])*1000000);
				int y=(int)(Float.parseFloat(val[3])*1000000);
				Point p=new Point(x,y);
				if (x>maxX)
					maxX=x;
				if (x<minX)
					minX=x;
				if (y>maxY)
					maxY=y;
				if (y<minY)
					minY=y;
				
				Car c=new Car(p, val[0]);
				return c;
		/*	}
			return res;

		}
		catch (FileNotFoundException e) {
			System.out.println(e);
		}
		catch(IOException e){
			System.out.println(e);
		}
		return res;
		*/

	}
	
	
}
