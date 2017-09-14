package bigdata.dbscan.rtree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import com.github.davidmoten.rtree.RTree;
import com.github.davidmoten.rtree.geometry.Geometries;
import com.github.davidmoten.rtree.geometry.Point;

public class Parser
{
	public static RTree<MyPoint, Point> parse(String inPath)
	{
		RTree<MyPoint, Point> points = null;
		
		try 
		{
			BufferedReader bf=new BufferedReader(new FileReader(inPath));
			String line="";
			StringTokenizer st;
			points=RTree.create();
			while ((line=bf.readLine())!=null)
			{
				st=new StringTokenizer(line, ",");
				String id=st.nextToken();
				st.nextToken();
				st.nextToken();
				double y=Double.parseDouble(st.nextToken());
				double x=Double.parseDouble(st.nextToken());
				Point p=Geometries.point(y,x);
				MyPoint myPoint=new MyPoint(id);
				// Point point = Geometries.pointGeographic(lon, lat);
				points = points.add(myPoint, p);
			}
			bf.close();
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return points;
	}
}