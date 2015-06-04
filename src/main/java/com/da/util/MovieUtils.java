package com.da.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by puri on 6/4/2015.
 */
public class MovieUtils {
    public static final Pattern pattern = Pattern.compile("\\(([0-9\\?].*?)\\)");//Pattern.compile("(.+?)\\((.+?)\\)")

    public static Movie getMovie(String lin) {
        final String line = lin.trim();
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            final int count = matcher.groupCount();
            for (int i = 1; i <= count; i++) {
                String year = matcher.group(i);
                if (year.matches(".*[0-9].*")) {
                    try {
                        int yearNum = Integer.parseInt((year.contains("/")) ?
                                (year.substring(0, year.indexOf('/')))
                                : year);
                        return new Movie(line, yearNum);
                    } catch (Exception ex) {
                        return new Movie(line, -3);
                    }
                } else if (year.contains("?")) {
                    return new Movie(line, -1);
                }
            }
            throw new RuntimeException("Could not extract movie: " + line);
        } else {
            System.err.println("Could not extract movie: " + line);
            return new Movie(line, -2);
        }

    }
}
