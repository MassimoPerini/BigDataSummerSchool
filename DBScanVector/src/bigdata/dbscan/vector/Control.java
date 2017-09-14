package bigdata.dbscan.vector;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

public class Control {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Computing...\n\n");
	    long startTime = System.currentTimeMillis();

		DBScan dbS=new DBScan(Parser.parse("example1.csv"));
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
			Vector<Point> pts=clusters.get(i).getPoints();
			for (int j=0;j<pts.size();j++)
			{
				bw.write("id = "+pts.get(j).getId()+" x = "+pts.get(j).getX()+" y = "+pts.get(j).getY()+"\n");
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
