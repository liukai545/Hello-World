import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerFactory;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liukai on 2015/10/21.
 */
public class Test {

    private static Logger logger = Logger.getLogger(Test.class);

    public static void main(String[] args) throws InterruptedException {

/*        PropertyConfigurator.configure("C:\\Users\\liukai\\Desktop\\log4j.properties");

        System.out.println(logger.isInfoEnabled());


        logger.debug("This is debug message.");

        logger.info("This is info message.");

        logger.error("This is error message.");*/

        //href = '<a href="/uhome/privilege?privilege=0" target="_blank"></a>'.match(/\<a\shref\=\"([^\"]+)/)[1]

/*        String html = "<a href=\"http://www.autohome.com.cn/780/\" style=\"text-decoration:none;\" target=\"_blank\" title=\"[D] 大众UP频道\" >[D] 大众UP频道</a>";
        String href = parseHref(html);
        System.out.println(href);*/

        StringBuffer sb = new StringBuffer();

        xxx(sb);

        System.out.println(sb);

    }

    public static void xxx(StringBuffer sb) {
        sb = new StringBuffer("Iii");
    }

    public static String parseHref(String html) {
        String regex = "<a[\\s]+href[\\s]*=[\\s]*\"([^<\"]+)\"";
        //String regex = "[^.]";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(html);

        StringBuffer ret = new StringBuffer();
        while (m.find()) {
            ret.append(m.group(1));
        }

        return ret.toString();
    }
}


class TT {
    public static void sleep() {

    }
}