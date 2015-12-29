package test.hcatalog.readerwriter;

import org.apache.hive.hcatalog.common.HCatException;
import org.apache.hive.hcatalog.data.DefaultHCatRecord;
import org.apache.hive.hcatalog.data.HCatRecord;
import org.apache.hive.hcatalog.data.transfer.DataTransferFactory;
import org.apache.hive.hcatalog.data.transfer.HCatWriter;
import org.apache.hive.hcatalog.data.transfer.WriteEntity;
import org.apache.hive.hcatalog.data.transfer.WriterContext;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.*;

/**
 * Created by liukai on 2015/10/24.
 */
public class HCatWriterTest {
    public static void main(String[] args) {
        String dbName = args[0];
        String tbName = args[1];
        String inputFile = args[2];

        WriteEntity.Builder builder = new WriteEntity.Builder();
        WriteEntity entity = builder.withDatabase(dbName).withTable(tbName).build();

        HCatWriter hCatWriter = DataTransferFactory.getHCatWriter(entity, null);

        try {
            WriterContext writerContext = hCatWriter.prepareWrite();
            HCatWriter writer = DataTransferFactory.getHCatWriter(writerContext);

            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            List<HCatRecord> recordList = new java.util.ArrayList<HCatRecord>();

            String line = br.readLine();
            while (line != null) {
                String[] row = line.split(",");
                HCatRecord record = new DefaultHCatRecord(Arrays.<Object>asList(row));
                recordList.add(record);
                line = br.readLine();
            }

            writer.write(recordList.iterator());
            writer.commit(writerContext);

        } catch (HCatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
