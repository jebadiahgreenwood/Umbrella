package com.example.weatherapp.view.WheatherRendering;

import java.util.HashMap;
import java.util.Map;

public class SelectRendering {
    private Map<String, WheatherRendering> mapRenderings;
    private SelectRendering()
    {
        mapRenderings = new HashMap<>();
        mapRenderings.put("clear sky", new ClearRendering());
        mapRenderings.put("overcast clouds", new CloudyRendering());
        ScatteredCloudsRendering scatteredClouds = new ScatteredCloudsRendering();
        mapRenderings.put("broken clouds", scatteredClouds);
        mapRenderings.put("few clouds", scatteredClouds);
        mapRenderings.put("scattered clouds", scatteredClouds);
        mapRenderings.put("light rain", new RainRendering());
        mapRenderings.put("moderate rain", new RainRendering());
        mapRenderings.put("heavy intensity rain", new HeavyRainRendering());



    }
    private static SelectRendering singleton = new SelectRendering();

    public static SelectRendering getInstance()
    {
        return singleton;
    }

    public WheatherRendering getRendering(String strDescription)
    {
        if (mapRenderings.containsKey(strDescription))
            return mapRenderings.get(strDescription);
        return null;
    }


}
