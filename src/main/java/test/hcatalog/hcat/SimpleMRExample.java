package test.hcatalog.hcat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hive.hcatalog.data.DefaultHCatRecord;
import org.apache.hive.hcatalog.data.HCatRecord;
import org.apache.hive.hcatalog.data.schema.HCatSchema;
import org.apache.hive.hcatalog.mapreduce.HCatInputFormat;
import org.apache.hive.hcatalog.mapreduce.HCatOutputFormat;
import org.apache.hive.hcatalog.mapreduce.OutputJobInfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by liukai on 2015/10/23.
 */

/**
 * 通过Hcatalog读取hive，进行简单的mapRed，将结果写入hive表
 */
public class SimpleMRExample extends Configured implements Tool {

    public static class Map extends Mapper<WritableComparable, HCatRecord, Text, IntWritable> {
        HCatSchema schema;
        Text host = new Text();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            schema = HCatInputFormat.getTableSchema(context.getConfiguration());
            if (schema == null) {
                throw new RuntimeException("schema is null");
            }
        }

        @Override
        protected void map(WritableComparable key, HCatRecord value, Context context) throws IOException, InterruptedException {
            host.set(value.getString("column4", schema));
            context.write(host, new IntWritable(1));
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable, WritableComparable, HCatRecord> {
        HCatSchema schema;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            schema = HCatOutputFormat.getTableSchema(context.getConfiguration());
        }

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            Iterator<IntWritable> iterator = values.iterator();
            int sum = 0;
            while (iterator.hasNext()) {
                sum++;
                iterator.next();
            }
            HCatRecord record = new DefaultHCatRecord(2);
            record.setString("name", schema, key.toString());
            record.setInteger("count", schema, sum);
            context.write(null, record);
            ;
        }
    }

    public int run(String[] args) throws Exception {
        Configuration conf = getConf();

        String inputTable = args[0];
        //                    dt>="${1}" and dt<="${2}"
        //String filter = "dt>=\"" + args[1] + "\" and " + "dt<=\"" + args[2] + "\"";
        String outputTable = args[1];
        //String outputFilter = args[2];
        int reduceNum = Integer.parseInt(args[2]);
        String dbname = "root";

        Job job = Job.getInstance(conf, "testMR-group-count");
        //System.out.println("filter =" + filter);

        HCatInputFormat.setInput(job, dbname, inputTable);

        job.setInputFormatClass(HCatInputFormat.class);
        job.setJarByClass(SimpleMRExample.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(WritableComparable.class);
        job.setOutputValueClass(DefaultHCatRecord.class);
        job.setNumReduceTasks(reduceNum);

        HashMap<String, String> partitions = new HashMap<String, String>(1);
        //partitions.put("dt", outputFilter);

        HCatOutputFormat.setOutput(job, OutputJobInfo.create("root", outputTable, partitions));
        HCatSchema schema = HCatOutputFormat.getTableSchema(job.getConfiguration());
        HCatOutputFormat.setSchema(job, schema);
        job.setOutputFormatClass(HCatOutputFormat.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) {
        int exitCode = 0;
        try {
            exitCode = ToolRunner.run(new SimpleMRExample(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(exitCode);
    }
}
