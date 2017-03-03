/**
 * subgraph counting for unlabeled graph; 
 * using key value abstraction
 * using rotation
 * 
 */
package edu.iu.sahad.rotation;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import edu.iu.fileformat.MultiFileInputFormat;

public class SCMapCollective extends Configured implements Tool {

	String template;
	String graphDir;
	String outDir;
	int numThreads;
	boolean useLocalMultiThread;
	ArrayList<SCSubJob> subjoblist;
	/*
	 * a template is like:  
	    i graph 5
		u5-1 u3-1 u2
		u3-1 i u2
		u2 i i
		final u5-1 5 2
	 */
	public int run(String[] args) throws Exception {
		if (args.length < 6) {
			System.err.println("Usage: edu.iu.sahad.rotation.SCMapCollective <useLocalMultiThread> <template> <graphDir> <outDir> <num threads per node>");
			ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		useLocalMultiThread = (Integer.parseInt(args[0])) == 0? false: true;
		template = args[1];
		graphDir = args[2];
		outDir = args[3];
		numThreads = Integer.parseInt(args[4]);
		System.out.println("use Local MultiThread? "+useLocalMultiThread);
		System.out.println("set number of threads: "+numThreads);
		launch();
		return 0;
	}
	private Job configureSCJob( ) throws IOException  {
		Configuration configuration = getConf();
		Job job = Job.getInstance(configuration, "subgraph counting");
		Configuration jobConfig = job.getConfiguration();
		Path jobOutDir = new Path(outDir);
		FileSystem fs = FileSystem.get(configuration);
		if (fs.exists(jobOutDir)) {
			fs.delete(jobOutDir, true);
		}
		FileInputFormat.setInputPaths(job, graphDir);
		FileOutputFormat.setOutputPath(job, jobOutDir);
		
		//job.setInputFormatClass(KeyValueTextInputFormat.class);
		//use harp multifile input format to have a better control on num of map tasks
		job.setInputFormatClass(MultiFileInputFormat.class);
		
		job.setJarByClass(SCMapCollective.class);
		job.setMapperClass(SCCollectiveMapper.class);
		org.apache.hadoop.mapred.JobConf jobConf = (JobConf) job.getConfiguration();
		jobConf.set("mapreduce.framework.name", "map-collective");
		jobConf.setInt("mapreduce.job.max.split.locations", 10000);
		job.setNumReduceTasks(0);
		jobConfig.set(SCConstants.TEMPLATE_PATH, template);
		jobConfig.set(SCConstants.OUTPUT_PATH, outDir);
		jobConfig.setBoolean(SCConstants.USE_LOCAL_MULTITHREAD, useLocalMultiThread);
		jobConfig.setInt(SCConstants.NUM_THREADS_PER_NODE,numThreads);

		return job;
	}
	
	public void launch() throws ClassNotFoundException, IOException, InterruptedException{
		boolean jobSuccess = true;
		int jobRetryCount = 0;
		Job scJob = configureSCJob();
		// ----------------------------------------------------------
		jobSuccess =scJob.waitForCompletion(true);
		do{
			if (!jobSuccess) {
				System.out.println("SubgraphCounting Job failed.retry");
				jobRetryCount++;
				if (jobRetryCount == 3) {
					break;
				}
			}
		}while(!jobSuccess);
		// ----------------------------------------------------------
	}
	
	public static void main(String[] args) throws Exception {
		
		long begintime = System.currentTimeMillis();
		int res = ToolRunner.run(new Configuration(), new SCMapCollective(), args);
		long endtinme=System.currentTimeMillis();
		long costTime = (endtinme - begintime);
		System.out.println(costTime+"ms"); 
		
		System.exit(res);
	}
}	
