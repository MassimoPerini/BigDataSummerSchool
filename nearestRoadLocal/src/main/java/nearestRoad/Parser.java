package nearestRoad;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

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
			BufferedReader br = new BufferedReader(new FileReader(pathPoint));
			String line="";
			HashMap <String, double[]>rowIntersec=new HashMap <String, double[]>();
			
			while ((line=br.readLine())!=null)
			{
				String [] val=line.split(",");
		/*		if (val[0].equals("1210108"))
					System.out.println(val[2]+"    "+Float.parseFloat(val[2])*1000000);*/
				double [] v={(Double.parseDouble(val[1])*1000000), (Double.parseDouble(val[2])*1000000)};
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
			br = new BufferedReader(new FileReader(pathRow));
			//upper-left, width, heigh
			QuadTree quadTree= new QuadTree(new Rectangle(minX-1, minY-1,(maxX-minX)+1,(maxY-minY)+1), TestConfig.QUADTREE_BUCKET);
		//	int i=0;
			while ((line=br.readLine())!=null)
			{
				String [] val=line.split(",");
				double [] startRoad=rowIntersec.get(val[1]);
				double [] endRoad=rowIntersec.get(val[2]);
	/*			if (startRoad[0]==116246296 && startRoad[1]==39971456)
					System.out.println("i: "+i+" , "+startRoad[1]+" - "+endRoad[0]+", "+endRoad[1]);*/
				Road r=new Road(val[0], Double.parseDouble(val[3]));
				r.add(startRoad[0],startRoad[1]);
				r.add(endRoad[0], endRoad[1]);
				quadTree.add(r);
		//		i++;
			}
			System.out.println("Finished parsing... ");
			return quadTree;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static LinkedList<Car> parseCars(String path)
	{
		LinkedList<Car> res=new LinkedList<Car>();
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line="";
			while ((line=br.readLine())!=null)
			{
				String [] val=line.split(",");
				double x=(Double.parseDouble(val[2])*1000000);
				double y=(Double.parseDouble(val[3])*1000000);
				Point p=new Point((int)x,(int)y);
				if (x>maxX)
					maxX=(int)x;
				if (x<minX)
					minX=(int)x;
				if (y>maxY)
					maxY=(int)y;
				if (y<minY)
					minY=(int)y;
				
				Car c=new Car(p, x,y,val[0]);
				res.add(c);
			}
			return res;

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return res;
		

	}
	
	
}
