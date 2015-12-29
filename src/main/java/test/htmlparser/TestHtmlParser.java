package test.htmlparser;


import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liukai on 2015/11/24.
 */
public class TestHtmlParser {
    public static void main(String[] args) throws IOException, ParserException {
        Parser parser = new Parser((HttpURLConnection) (new URL("http://www.zhihu.com/question/21391305/answer/44542857").openConnection()));

        NodeFilter filter = new TagNameFilter("div");
        NodeFilter content = new HasAttributeFilter("class", "zm-editable-content clearfix");
        NodeList nodes = parser.extractAllNodesThatMatch(content);

        if (nodes != null) {
            for (int i = 0; i < nodes.size(); i++) {
                Node textnode = (Node) nodes.elementAt(i);

                System.out.println(("getText:" + textnode.toHtml().replaceAll("<br>",System.lineSeparator()).replaceAll("<.*?[b|u|div].*?>"," ")));
                System.out.println(("================================================="));
            }
        }
    }
}
