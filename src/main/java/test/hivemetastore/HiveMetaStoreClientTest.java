package test.hivemetastore;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException;
import org.apache.hive.hcatalog.common.HCatUtil;
import org.apache.thrift.TException;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by liukai on 2015/10/26.
 */
public class HiveMetaStoreClientTest {
    public static void main(String[] args) {

        HiveConf hiveConf = null;
        HiveMetaStoreClient hiveMetaStoreClient = null;
        String dbName = null;

        try {
            hiveConf = HCatUtil.getHiveConf(new Configuration());
            hiveMetaStoreClient = new HiveMetaStoreClient(hiveConf);

            dbName = args[0];

            getDatabase(hiveMetaStoreClient, dbName);


        } catch (MetaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
            System.out.println("===============");
            System.out.println("database " + args[0] + "not exists");
            System.out.println("===============");
            createDatabase(hiveMetaStoreClient, dbName);
            try {
                getDatabase(hiveMetaStoreClient, dbName);
            } catch (TException e1) {
                e1.printStackTrace();
                System.out.println("TMD");
            }
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public static Database getDatabase(HiveMetaStoreClient hiveMetaStoreClient, String dbName) throws TException {
        Database database = null;

        database = hiveMetaStoreClient.getDatabase(dbName);

        System.out.println(database.getLocationUri());
        System.out.println(database.getOwnerName());

        for (String key : database.getParameters().keySet()) {
            System.out.println(key + " = " + database.getParameters().get(key));
        }
        return database;
    }

    public static Database createDatabase(HiveMetaStoreClient hiveMetaStoreClient, String dbName) {
        HashMap<String, String> map = new HashMap<String,String>();
        Database database = new Database(dbName, "desc", null, map);
        try {
            hiveMetaStoreClient.createDatabase(database);
        } catch (TException e) {
            e.printStackTrace();
            System.out.println("创建数据库失败");
        }
        return database;
    }
}
