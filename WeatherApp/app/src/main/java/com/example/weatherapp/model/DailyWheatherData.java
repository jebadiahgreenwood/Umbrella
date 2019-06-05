package com.example.weatherapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyWheatherData implements Comparable<DailyWheatherData> {
    public List<HourlyWheatherData> hourlyData = new ArrayList<>();
    public String date;

    static private Pattern date_pattern =
            Pattern.compile("([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})");

    protected Integer getNumericValue()
    {
        //takes the date and turns it into a numerical value
        Matcher m = date_pattern.matcher(date);
        if (!m.find())
            return 0;
        Integer i = Integer.valueOf(m.group(1)) * 10000;
        i += Integer.valueOf(m.group(2))*100;
        i += Integer.valueOf(m.group(3));
        return i;
    }

    @Override
    public int compareTo(DailyWheatherData o) {
        return getNumericValue().compareTo(o.getNumericValue());
    }
}
