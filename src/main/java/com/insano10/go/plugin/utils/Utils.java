package com.insano10.go.plugin.utils;

import java.util.Map;

public class Utils
{
    public static String getKeyLike(final Map<String, Object> map, final String regex)
    {
        for (String key : map.keySet())
        {
            if (key.matches(regex))
            {
                return key;
            }
        }
        return null;
    }

}
