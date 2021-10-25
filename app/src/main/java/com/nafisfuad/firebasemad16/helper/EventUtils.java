package com.nafisfuad.firebasemad16.helper;

import android.widget.SimpleExpandableListAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EventUtils {
    public static final String WEATHER_CONDITION_ICON_PREFIX = "https://openweathermap.org/img/wn";
    public static final String DEGREE = "\u00B0";
    public static final String UNIT_CELCIUS = "metric";
    public static final String UNIT_FAHRENHEIT = "imperial";
    public static final String UNIT_CELCIUS_SYMBOL = "C";
    public static final String UNIT_FAHRENHEIT_SYMBOL = "F";
    public static String getCurrentDateTime() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }
    public static String getFormattedDate(long dt) {
        Date date = new Date(dt * 1000);
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
    }
}
