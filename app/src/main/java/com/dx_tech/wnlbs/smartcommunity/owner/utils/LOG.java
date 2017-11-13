package com.dx_tech.wnlbs.smartcommunity.owner.utils;

import android.util.Log;

/**
 * Created by 戴圣伟 on 0017 2017/10/17.
 * Log管理工具
 *
 */

public class LOG {
//    private static boolean APP_DEBUG = Config.DEBUG;
    private static int LOG_LEVEL = 0;
    private static int ERROR = 1;
    private static int WARN = 2;
    private static int INFO = 3;
    private static int DEBUG = 4;
    private static int VERBOS = 5;


    public static void e(String tag,String msg){
        if(LOG_LEVEL>ERROR)
            Log.e(tag, msg);
    }

    public static void w(String tag,String msg){
        if(LOG_LEVEL>WARN)
            Log.w(tag, msg);
    }
    public static void i(String tag,String msg){
        if(LOG_LEVEL>INFO)
            Log.i(tag, msg);
    }
    public static void d(String tag,String msg){
        if(LOG_LEVEL>DEBUG)
            Log.d(tag, msg);
    }
    public static void v(String tag,String msg){
        if(LOG_LEVEL>VERBOS)
            Log.v(tag, msg);
    }
}
