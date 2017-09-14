package bigdata.dbscan.vector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

public class Parser
{
	public static Vector<Point> parse(String inPath)
	{
		Vector <Point> points=null;
		try 
		{
			BufferedReader bf=new BufferedReader(new FileReader(inPath));
			String line="";
			StringTokenizer st;
			points=new Vector<Point> ();
			while ((line=bf.readLine())!=null)
			{
				st=new StringTokenizer(line, ",");
				String id=st.nextToken();
				st.nextToken();
				st.nextToken();
				double y=Double.parseDouble(st.nextToken());
				double x=Double.parseDouble(st.nextToken());
				Point p=new Point(id, y,x);
				points.add(p);
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