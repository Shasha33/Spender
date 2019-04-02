package com.project.spender;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.spender.MainActivity.parseNumbers;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void parserTest() {
        String content = " t=20190401T2022&s=134.00&fn=9282000100051444&i=31432&fp=3311754675&n=1";
        String[] res = parseNumbers(content);
        assertEquals(res[0], "9282000100051444");
        assertEquals(res[1], "31432");
        assertEquals(res[2], "3311754675");
    }
}