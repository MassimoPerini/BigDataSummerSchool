package bigdata.dbscan.rtree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

import com.github.davidmoten.rtree.Entry;
import com.github.davidmoten.rtree.geometry.Point;

public class Control {
	public static void main(String [] args)
	{
	    long startTime = System.currentTimeMillis();
		System.out.println("Computing...\n\n");

		DBScan dbS=new DBScan(Parser.parse(args[0]));
		dbS.dbScan(0.001,4);
		
		Vector<Cluster> clusters=dbS.getClusters();
		
	    long endTime = System.currentTimeMillis();
	    long elTime=endTime-startTime;
		
	    String outPath= "result.txt";
	    
	    if (args.length>1)
	    	outPath=args[1];

	    try{
	    	FileWriter fw = new FileWriter(outPath);
			BufferedWriter bw = new BufferedWriter(fw);
			
		for (int i=0;i<clusters.size();i++)
		{
			bw.write("Cluster "+clusters.get(i).getId()+"\n");
			Vector<Entry<MyPoint, Point>> pts=clusters.get(i).getPoints();
			for (int j=0;j<pts.size();j++)
			{
				bw.write("id = "+pts.get(j).value().getId()+" x = "+pts.get(j).geometry().x()+" y = "+pts.get(j).geometry().y()+"\n");
			}
		}
		bw.close();
		fw.close();
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Error in writing file");
	    }
		System.out.println("END\nEXECUTION TIME: "+elTime+" ms");
	}
}
