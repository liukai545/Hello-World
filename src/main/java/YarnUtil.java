

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.QueueInfo;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;


/**
 * Created by liukai on 2015/9/22.
 */
public class YarnUtil extends Configured {

    private static YarnUtil instance = null;

    YarnClient yarnClient;

    private YarnUtil() throws IOException {

        Configuration configuration = new Configuration();
        configuration.addResource("yarn-site.xml");
        configuration.addResource("core-site.xml");
        configuration.addResource("hdfs-site.xml");
        configuration.addResource("mapred-site.xml");
        configuration.addResource("ssl-client.xml");

        YarnConfiguration yarnconf = new YarnConfiguration(configuration);

        yarnClient = YarnClient.createYarnClient();
        yarnClient.init(yarnconf);
    }

    public static YarnUtil getInstance() throws IOException {
        if (instance == null) {
            return new YarnUtil();
        } else {
            return instance;
        }
    }

    public boolean killApplicationJob(String applicationId) throws IOException, YarnException {

        yarnClient.start();

        try {
            String[] applicationID = applicationId.split("_");
            long clusterTimeStamp = Long.parseLong(applicationID[1]);
            int id = Integer.parseInt(applicationID[2]);

            ApplicationId app_id = ApplicationId.newInstance(clusterTimeStamp, id);

            ApplicationReport applicationReport = yarnClient.getApplicationReport(app_id);

            YarnApplicationState yarnApplicationState = applicationReport.getYarnApplicationState();
            System.out.println("state" + yarnApplicationState);

            if ((yarnApplicationState.equals(YarnApplicationState.FINISHED)
                    || yarnApplicationState.equals(YarnApplicationState.KILLED)
                    || yarnApplicationState.equals(YarnApplicationState.FAILED))) {
                return true;
            } else {

                yarnClient.killApplication(app_id);
                return true;
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            throw new IOException("applicationId 格式错误：" + applicationId);
        } finally {
            yarnClient.stop();
        }
    }

    public void getQueueInfo(String queueName) {
        yarnClient.start();

        try {
            //List<QueueInfo> allQueues = yarnClient.getAllQueues();

            QueueInfo queueInfo = yarnClient.getQueueInfo(queueName);

            System.out.println("application detail");
            for (ApplicationReport applicationReport : queueInfo.getApplications()) {
                System.out.println(applicationReport.getName() + applicationReport.getApplicationId());
            }
            System.out.println("capacity:" + queueInfo.getCapacity());
            System.out.println("CurrentCapacity:" + queueInfo.getCurrentCapacity());
            System.out.println("MaximumCapacity" + queueInfo.getMaximumCapacity());
            System.out.println("QueueState().name" + queueInfo.getQueueState().name());
            System.out.println("QueueState().ordinal" + queueInfo.getQueueState().ordinal());
            System.out.println("QueueState().toString()" + queueInfo.getQueueState().toString());


        } catch (YarnException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        YarnUtil yarnUtil = YarnUtil.getInstance();

        yarnUtil.getQueueInfo(args[0]);
    }
}