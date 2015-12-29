package test.filesystem;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.IOException;


/**
 * Created by liukai on 2015/11/24.
 */
public class FileSystemLocal {
    public static void main(String[] args) {
        Configuration conf = new Configuration();

        try {
            FileSystem fs = FileSystem.getLocal(conf);

            FSDataInputStream inputStream = fs.open(new Path("/root/liukai/stdin.xml"));

            String str = IOUtils.toString(inputStream);

            System.out.println(str);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
