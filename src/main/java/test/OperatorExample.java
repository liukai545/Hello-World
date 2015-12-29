package test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by liukai on 2015/7/25.
 */
public class OperatorExample extends Configured implements Tool {

    Logger logger = LoggerFactory.getLogger(OperatorExample.class);

    private int clusterNumber = 0;
    private int maxInterationTime = 0;
    private String[] cols = null;
    private String inport1 = "";
    private String outport1 = "";

    public static class MapClass extends MapReduceBase implements

            Mapper<LongWritable, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text("total words:");

        public void map(LongWritable key, Text value,
                        OutputCollector<Text, IntWritable> output, Reporter reporter)
                throws IOException {
            String line = value.toString();
            StringTokenizer itr = new StringTokenizer(line);
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                output.collect(word, one);
            }
        }
    }

    /**
     * A reducer class that just emits the sum of the input values.
     */
    public static class Reduce extends MapReduceBase implements
            Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text key, Iterator<IntWritable> values,
                           OutputCollector<Text, IntWritable> output, Reporter reporter)
                throws IOException {
            int sum = 0;
            while (values.hasNext()) {
                sum += values.next().get();
            }
            output.collect(key, new IntWritable(sum));
        }
    }

/*	static int printUsage() {
        System.out
				.println("CSVReader [-m <maps>] [-r <reduces>] <input> <output>");
		ToolRunner.printGenericCommandUsage(System.out);
		return -1;
	}*/

    /**
     * The main driver for word count map/reduce program. Invoke this method to
     * submit the map/reduce job.
     *
     * @throws IOException When there is communication problems with the job tracker.
     */
    public int run(String[] args) throws Exception {



        if (true)
            return 1;

        Configuration haconfig = getConf();
       /* haconfig.set("hadoop.job.ugi", args[1] + "," + args[2]);*/

        UserGroupInformation ugi = UserGroupInformation.getCurrentUser();

        logger.info("currentUser:" + ugi.getUserName());
        for (String str : ugi.getGroupNames()) {
            logger.info(str);
        }

        ugi = UserGroupInformation.getLoginUser();

        logger.info("loginUser:" + ugi.getUserName());
        for (String str : ugi.getGroupNames()) {
            logger.info(str);
        }

        JobConf conf = new JobConf(haconfig, OperatorExample.class);
        conf.setJobName("wordcount");
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(IntWritable.class);

        conf.setMapperClass(MapClass.class);
        conf.setCombinerClass(Reduce.class);
        conf.setReducerClass(Reduce.class);

        /////////////////////////读取参数/////////////////////////
/*        clusterNumber = configuration.getInt("clusterNumber",0);
        maxInterationTime = configuration.getInt("maxInterationTime",0);
        cols = configuration.getStrings("columns");*/

/*        inport1 = configuration.get("inport1");
        outport1 = configuration.get("outport1");*/

        outport1 = "/user/cloudera/out";
        org.apache.hadoop.fs.FileSystem fs = org.apache.hadoop.fs.FileSystem.get(java.net.URI.create(outport1), new Configuration());
        Path path = new Path(outport1);
        if (fs.exists(path)) {
            fs.delete(path);
        }
        FileInputFormat.setInputPaths(conf, new Path(args[0]));
        FileOutputFormat.setOutputPath(conf, new Path(outport1));
        System.out.println("key" + System.getProperty("key"));
        RunningJob runningJob = JobClient.runJob(conf);

        System.out.println(runningJob.getFailureInfo());
        System.out.println(runningJob.getID());
        System.out.println(runningJob.getTrackingURL());
        System.out.println(runningJob.getHistoryUrl());
        System.out.println("out...");
        System.err.println("err...");

        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new OperatorExample(), args);
        System.exit(res);
    }
}