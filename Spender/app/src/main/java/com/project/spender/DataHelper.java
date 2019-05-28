package com.project.spender;

public abstract class DataHelper {


    public static final String DEFAULT_BEGIN = "1999-03-22";
    public static final String DEFAULT_END = "now";

    /**
     * Converts date from DD.MM.YYYY to YYYY-MM-DD
     * terrible code (todo) fix it
     */
    public static String dateConvert(String date) throws IllegalArgumentException {
        String[] args = date.split("\\.");

        if (args.length != 3) {
            throw new IllegalArgumentException();
        }

        //:(
        String tmp = args[0];
        args[0] =  args[2];
        args[2] = tmp;

        if (!args[2].matches("[0-9]{2}") || !args[1].matches("[0-9]{2}")
                || !args[0].matches("[0-9]{4}")) {
            throw new IllegalArgumentException();
        }

        return args[0] + "-" + args[1] + "-" + args[2];
    }
}
