package com.nolankuza.theultimatealliance.util;

public class DataUtil {
    public static String clean(String s) {
        return s.replaceAll("[\n\r|\t,]", " ");
    }
}
