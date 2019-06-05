package com.example.weatherapp.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HourlyWheatherData implements Comparable<HourlyWheatherData> {
    public String time;
    public String description;
    public String iconId;
    public String temp;
    public double temp_int;
    public boolean isHottest = false;
    public boolean isColdest = false;
    static Pattern time_pattern = Pattern.compile("([0-9]{1,2})\\s.*");

    protected Integer getNumericValue()
    {
        if (time.matches("12 AM")) return 0;
        Matcher matcher = time_pattern.matcher(time);
        if(!matcher.find()) return 0; 
        int number_value = Integer.valueOf(matcher.group(1));
        number_value += time.matches(".*PM.*") ? 12 : 0;
        return number_value;
    }

    @Override
    public int compareTo(HourlyWheatherData o) {
        return getNumericValue().compareTo(o.getNumericValue());
    }
}
