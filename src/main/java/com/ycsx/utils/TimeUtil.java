package com.ycsx.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String getTime(){
        SimpleDateFormat stf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        return stf.format(new Date());
    }
}
