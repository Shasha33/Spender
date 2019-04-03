package com.project.spender;

import org.junit.Test;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.project.spender.MainActivity.parseNumbers;
import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void parserTest() {
        String content = "t=20190401T2022&s=134.00&fn=9282000100051444&i=31432&fp=3311754675&n=1";
        List<String> res = parseNumbers(content);
        assertEquals(Arrays.asList("20190401", "2022", "13400",
                "9282000100051444", "31432", "3311754675", "1"), res);
    }

    @Test
    public void dataBaseTest() throws SQLException {
        DataBase db = new DataBase();
        db.add("a", "34", "12.10", 121, "5orochka");
        List<Good> res = db.getAllByName("a");
        db.clear();
        assertEquals(1, res.size());
    }

    @Test
    public void dataBaseTestSubstring() throws SQLException {
        DataBase db = new DataBase();
        db.add("aaa", "34", "12.10", 121, "5orochka");
        List<Good> res = db.getAllByName("aa");
        db.clear();
        assertEquals(1, res.size());
    }

    @Test
    public void dataBaseTestSearchByDate() throws SQLException {
        DataBase db = new DataBase();
        db.add("aaa", "2017.34.02", "12.10", 121, "5orochka");
        List<Good> res = db.getAllByDateRange("2017.32.01", "2019.32.02");
        assertEquals(1, res.size());
    }

    @Test
    public void dataBaseTestSearchByDateNonExisting() throws SQLException {
        DataBase db = new DataBase();
        db.add("aaa", "2019.34.02", "12.10", 121, "5orochka");
        List<Good> res = db.getAllByDateRange("2017.32.01", "2019.32.02");
        assertEquals(0, res.size());
    }

}