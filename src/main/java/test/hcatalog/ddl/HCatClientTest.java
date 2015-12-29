package test.hcatalog.ddl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hive.hcatalog.api.*;
import org.apache.hive.hcatalog.common.HCatException;
import org.apache.hive.hcatalog.data.schema.HCatFieldSchema;

import java.util.List;


/**
 * Created by liukai on 2015/10/24.
 */
public class HCatClientTest {

    public static void main(String[] args) {
        HCatClient hCatClient = null;

        Configuration configuration = new Configuration();

        try {
            hCatClient = HCatClient.create(configuration);

            HCatDatabase database = hCatClient.getDatabase("root");
            System.out.println(database.getComment());
            System.out.println(database.getLocation());

            HCatCreateDBDesc hCatCreateDBDesc = HCatCreateDBDesc.create(args[0]).build();
            hCatClient.createDatabase(hCatCreateDBDesc);

            HCatTable table = new HCatTable(args[0], args[1]);
            List<HCatFieldSchema> hCatFieldSchemaList = new java.util.ArrayList<HCatFieldSchema>();

            for (int i = 0; i < 5; i++) {
                HCatFieldSchema hCatFieldSchema = new HCatFieldSchema("col" + i, HCatFieldSchema.Type.STRING, "");
                hCatFieldSchemaList.add(hCatFieldSchema);
            }

            table.cols(hCatFieldSchemaList);

            HCatCreateTableDesc hCatCreateTableDesc = HCatCreateTableDesc.create(table).build();
            hCatClient.createTable(hCatCreateTableDesc);

        } catch (HCatException e) {
            e.printStackTrace();
        }

    }
}
