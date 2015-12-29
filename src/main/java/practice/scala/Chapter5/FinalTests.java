package practice.scala.Chapter5;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liukai on 2015/11/6.
 */
public class FinalTests {
    private final List<String> list = new java.util.ArrayList<String>();

    public static void main(String[] args) {
        FinalTests tests = new FinalTests();

        tests.list.add("ddd");

       /* tests.list = new LinkedList<>();*/

    }

}
