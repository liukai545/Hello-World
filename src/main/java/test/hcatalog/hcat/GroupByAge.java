package test.hcatalog.hcat; /**
 * Copyright 2013 Cloudera Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hive.hcatalog.mapreduce.*;
import org.apache.hive.hcatalog.data.*;
import org.apache.hive.hcatalog.data.schema.*;

/**
 * MapReduce use HCatalog
 */
public class GroupByAge extends Configured implements Tool {

    public static class Map extends Mapper<WritableComparable, HCatRecord, Text, IntWritable> {
        String groupname;

        @Override
        protected void map( WritableComparable key,
                            HCatRecord value,
                            org.apache.hadoop.mapreduce.Mapper<WritableComparable, HCatRecord,
                                    Text, IntWritable>.Context context)
                throws IOException, InterruptedException {
            // The group table from /etc/group has name, 'x', id
            groupname = (String) value.get(0);
            int id = (Integer) value.get(2);
            // Just select and emit the name and ID
            context.write(new Text(groupname), new IntWritable(id));
        }
    }

    public static class Reduce extends Reducer<Text, IntWritable,
            WritableComparable, HCatRecord> {

        protected void reduce( Text key,
                               java.lang.Iterable<IntWritable> values,
                               org.apache.hadoop.mapreduce.Reducer<Text, IntWritable,
                                       WritableComparable, HCatRecord>.Context context)
                throws IOException, InterruptedException {
            // Only expecting one ID per group name
            Iterator<IntWritable> iter = values.iterator();
            IntWritable iw = iter.next();
            int id = iw.get();
            // Emit the group name and ID as a record
            HCatRecord record = new DefaultHCatRecord(2);
            record.set(0, key.toString());
            record.set(1, id);
            context.write(null, record);
        }
    }

    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        args = new GenericOptionsParser(conf, args).getRemainingArgs();

        // Get the input and output table names as arguments
        String inputTableName = args[0];
        String outputTableName = args[1];
        // Assume the default database
        String dbName = null;

        Job job = new Job(conf, "UseHCat");
        HCatInputFormat.setInput(job, dbName, inputTableName);
        job.setJarByClass(GroupByAge.class);
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        // An HCatalog record as input
        job.setInputFormatClass(HCatInputFormat.class);

        // Mapper emits a string as key and an integer as value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // Ignore the key for the reducer output; emitting an HCatalog record as value
        job.setOutputKeyClass(WritableComparable.class);
        job.setOutputValueClass(DefaultHCatRecord.class);
        job.setOutputFormatClass(HCatOutputFormat.class);

        HCatOutputFormat.setOutput(job, OutputJobInfo.create(dbName,
                outputTableName, null));
        HCatSchema s = HCatOutputFormat.getTableSchema(job.getConfiguration());
        System.err.println("INFO: output schema explicitly set for writing:" + s);
        HCatOutputFormat.setSchema(job, s);
        return (job.waitForCompletion(true) ? 0 : 1);
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new GroupByAge(), args);
        System.exit(exitCode);
    }
}