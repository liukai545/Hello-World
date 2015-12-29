package test.hcatalog.readerwriter;

import org.apache.hive.hcatalog.common.HCatException;
import org.apache.hive.hcatalog.data.HCatRecord;
import org.apache.hive.hcatalog.data.transfer.DataTransferFactory;
import org.apache.hive.hcatalog.data.transfer.HCatReader;
import org.apache.hive.hcatalog.data.transfer.ReadEntity;
import org.apache.hive.hcatalog.data.transfer.ReaderContext;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by liukai on 2015/10/22.
 */
public class HCatReaderTest {
    public static void main(String[] args) {
        ReadEntity.Builder builder = new ReadEntity.Builder();

        String dbName = args[0];
        String tbName = args[1];
        String outputFile = args[2];

        ReadEntity entity = builder.withDatabase(dbName).withTable(tbName).build();

        FileWriter writer = null;
        try {
             writer = new FileWriter(new java.io.File(outputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        HCatReader reader = DataTransferFactory.getHCatReader(entity, null);
        try {
            ReaderContext context = reader.prepareRead();

            for (int i = 0; i < context.numSplits(); i++) {
                HCatReader splitReader = DataTransferFactory.getHCatReader(context, i);
                Iterator<HCatRecord> iterator = splitReader.read();
                while (iterator.hasNext()) {
                    HCatRecord record = iterator.next();

                    for (Object col : record.getAll()) {
                        writer.write(col.toString() + "  ,  ");
                    }
                    writer.write(System.lineSeparator());
                }
            }

        } catch (HCatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
