package edu.iu.sahad.rotation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import edu.iu.harp.partition.Partition;
import edu.iu.harp.resource.IntArray;
import edu.iu.harp.schdynamic.Task;

public class GraphLoadTask implements Task<String, ArrayList<Partition<IntArray>>>{
	protected static final Log LOG = LogFactory
		    .getLog(GraphLoadTask.class);
	 private Configuration conf;

	 public GraphLoadTask(Configuration conf){
		 this.conf = conf;
	 }
	 
	@Override
	public ArrayList<Partition<IntArray>> run(String input) throws Exception {
		// TODO Auto-generated method stub
		 String fileName = (String) input;
		 ArrayList<Partition<IntArray>> partialGraphDataList = new ArrayList<Partition<IntArray>>();
		 Path pointFilePath = new Path(fileName);
		 FileSystem fs =pointFilePath.getFileSystem(conf);
		 FSDataInputStream in = fs.open(pointFilePath);
		 BufferedReader br  = new BufferedReader(new InputStreamReader(in));
		 try {
		      String line ="";
		      while((line=br.readLine())!=null){
		      String keyText = line.split("\t")[0];
		      String valueText = line.split("\t")[1];
		      
		      int key = Integer.parseInt(keyText.toString());
				 String[] itr = valueText.toString().split(",");
				 // the last one is " "; so discard it.
				 int length = itr.length-1;
				 //if the  length is zeor, it means that this vertex doesn't have any neighbors. So just skip it.
				 //-Ethan 03/17/2016
				 /*if(length == 0){
					 System.out.println("skip data:"+keyText.toString()+":"+valueText.toString()+";"+"length="+length);
					 continue;
				 }*/
				 int[] intValues = new int[length];
				 for(int i=0; i< length; i++){
					 intValues[i]= Integer.parseInt(itr[i]);
			     }
				 Partition<IntArray> partialgraph = new Partition<IntArray>(key, new IntArray(intValues, 0, length));
				 partialGraphDataList.add(partialgraph);
		      }
		  	} finally {
		      in.close();
		 }
		return partialGraphDataList;
	}

}
