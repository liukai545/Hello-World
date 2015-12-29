package test.hcatalog.hcat;

/**
 * Created by liukai on 2015/10/27.
 */

import iie.udps.common.hcatalog.SerHCatOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hive.hcatalog.api.HCatClient;
import org.apache.hive.hcatalog.api.HCatCreateTableDesc;
import org.apache.hive.hcatalog.api.HCatTable;
import org.apache.hive.hcatalog.data.DefaultHCatRecord;
import org.apache.hive.hcatalog.data.HCatRecord;
import org.apache.hive.hcatalog.data.schema.HCatFieldSchema;
import org.apache.hive.hcatalog.data.schema.HCatSchema;
import org.apache.hive.hcatalog.mapreduce.HCatInputFormat;
import org.apache.hive.hcatalog.mapreduce.OutputJobInfo;
import org.apache.spark.SerializableWritable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;


import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 从hive中表总读取数据
 */
public class SimpleSparkExample implements Serializable {
    public static void main(String[] args) {
        new SimpleSparkExample().run(args);
    }

    public void run(String[] args) {
        try {
            String dbName = args[0];
            String tbName = args[1];
            String outFile = args[2];
            String outTbName = args[3];


            SparkConf sparkConf = new SparkConf().setAppName("spark-hcatalog-test");
            JavaSparkContext jsc = new JavaSparkContext(sparkConf);

            //设置hcatInputFormat
            Configuration configuration = new Configuration();

            HCatInputFormat.setInput(configuration, dbName, tbName);

            HCatSchema tableSchema = HCatInputFormat.getTableSchema(configuration);

            //workcount
            JavaPairRDD<WritableComparable, HCatRecord> rdd =
                    jsc.newAPIHadoopRDD(configuration, HCatInputFormat.class, WritableComparable.class, HCatRecord.class);

            JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = rdd.mapToPair(new mapFunction(tableSchema));

            JavaPairRDD<String, Integer> stringIntegerJavaPairRDD1 = stringIntegerJavaPairRDD.reduceByKey(new Function2<Integer, Integer, Integer>() {
                public Integer call(Integer v1, Integer v2) throws Exception {
                    return v1 + v2;
                }
            });


            FileSystem fs = FileSystem.get(configuration);
            Path path = new Path(outFile);
            if (fs.exists(path)) {
                fs.delete(path, true);
            }
            stringIntegerJavaPairRDD1.saveAsTextFile(outFile);


            //rdd.foreach(new myFunction(tableSchema));

            //创建输出表
            HCatClient hCatClient = HCatClient.create(new Configuration());

/*            HCatTable outTable = hCatClient.getTable(dbName, outTbName);// new HCatTable(dbName, outTbName);

            hCatClient.dropTable(dbName, outTbName, true);*/

            HCatTable outTable = new HCatTable(dbName, outTbName);

            List<HCatFieldSchema> fieldSchemas = new java.util.ArrayList<HCatFieldSchema>();
            fieldSchemas.add(new HCatFieldSchema("word", TypeInfoFactory.stringTypeInfo, ""));
            fieldSchemas.add(new HCatFieldSchema("count", TypeInfoFactory.intTypeInfo, ""));

            outTable.cols(fieldSchemas);
            HCatCreateTableDesc tableDesc = HCatCreateTableDesc.create(outTable).build();
            hCatClient.createTable(tableDesc);

            //保存结果到输出表

            Job outputJob = Job.getInstance();
            outputJob.setOutputFormatClass(SerHCatOutputFormat.class);
            outputJob.setOutputKeyClass(WritableComparable.class);
            outputJob.setOutputValueClass(SerializableWritable.class);
            SerHCatOutputFormat.setOutput(outputJob, OutputJobInfo.create(dbName, outTbName, new HashMap<String, String>()));
            SerHCatOutputFormat.setSchema(outputJob, SerHCatOutputFormat.getTableSchema(outputJob.getConfiguration()));

            JavaPairRDD<WritableComparable, SerializableWritable<HCatRecord>> result =
                    stringIntegerJavaPairRDD1.mapToPair(new convertToRecord(SerHCatOutputFormat.getTableSchema(outputJob.getConfiguration())));

            result.saveAsNewAPIHadoopDataset(outputJob.getConfiguration());

            jsc.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class mapFunction implements PairFunction<Tuple2<WritableComparable, HCatRecord>, String, Integer> {
        HCatSchema hCatSchema;

        public mapFunction(HCatSchema hCatSchema) {
            this.hCatSchema = hCatSchema;
        }


        public Tuple2<String, Integer> call(Tuple2<WritableComparable, HCatRecord> writableComparableHCatRecordTuple2) throws Exception {
            HCatRecord record = writableComparableHCatRecordTuple2._2();
            String string = record.getString("e", hCatSchema);
            return new Tuple2<String, Integer>(string, 1);
        }
    }

    class convertToRecord implements PairFunction<Tuple2<String, Integer>, WritableComparable, SerializableWritable<HCatRecord>> {
        HCatSchema hCatSchema;

        public convertToRecord(HCatSchema hCatSchema) {
            this.hCatSchema = hCatSchema;
        }

        public Tuple2<WritableComparable, SerializableWritable<HCatRecord>> call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
            HCatRecord record = new DefaultHCatRecord(2);
            record.set("word", hCatSchema, stringIntegerTuple2._1());
            record.set("count", hCatSchema, stringIntegerTuple2._2());
            return new Tuple2<WritableComparable, SerializableWritable<HCatRecord>>(NullWritable.get(), new SerializableWritable<HCatRecord>(record));
        }
    }

    class myFunction implements VoidFunction<Tuple2<WritableComparable, HCatRecord>> {
        HCatSchema hCatSchema;

        public myFunction(HCatSchema hCatSchema) {
            this.hCatSchema = hCatSchema;
        }

        public void call(Tuple2<WritableComparable, HCatRecord> writableComparableHCatRecordTuple2) throws Exception {
            HCatRecord record = writableComparableHCatRecordTuple2._2();
            System.out.print(record.getString("a", hCatSchema) + "  ");
            System.out.print(record.getString("b", hCatSchema) + "  ");
            System.out.print(record.getString("c", hCatSchema) + "  ");
            System.out.print(record.getString("d", hCatSchema) + "  ");
            System.out.println(record.getString("e", hCatSchema) + "  ");

        }
    }
}
