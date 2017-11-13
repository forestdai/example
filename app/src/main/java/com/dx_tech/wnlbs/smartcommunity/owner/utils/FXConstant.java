package com.dx_tech.wnlbs.smartcommunity.owner.utils;

import android.util.Log;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 戴圣伟 on 2017/10/17.
 * 接口参数适配类
 */
public class FXConstant {
//    public static final String HOST = "http://10.42.20.216:9090/";
    public static final String HOST = "http://27.17.32.34:19090/";

    public static final String APPID = "71bdd2fe3ede4716a409b76b998f8dc2";
    public static final String SECURETKEY = "5c624e9a73f54544b4ff3e3fafc34fa2";


    public static final String JSON_KEY_AVATAR ="avatar";//headphoto/b9e567db56d54974a55d9de13430f765/012011232368.png

    public static final String JSON_KEY_RET_CODE   ="RET_CODE";
    public static final String JSON_KEY_RET_MSG   ="RET_MSG";

    //缓存项目列表
    public static final String JSON_KEY_PROJECT_LIST   ="PROJECT_LIST";

    //缓存Spinner的当前选项
    public static final String JSON_KEY_MAIN_SPINNER_CUR_SELECT   ="MAIN_SPINNER_CUR_SELECT";

    //缓存历史页面中Spinner的选项项
    public static final String JSON_KEY_HISTORY_XYZ_SPINNER_CUR_SELECT   ="JSON_KEY_HISTORY_XYZ_SPINNER_CUR_SELECT";



    public static final String INTENT_KET_PROJECT_INDEX   ="project_index";

//    public static final String API_BASE_URL   = "http://10.42.20.216:9090/truemonitor/";//local
    public static final String API_BASE_URL   = "http://27.17.32.34:19090/truemonitor/";

    public static final String URL_AVATAR= HOST + "photo/";//头像获取地址

    public static final String API_BASE_URL_AVATAR   = "http://27.17.32.34:19090/TrueMonitor/upload/";//头像上传地址

    public final static String PAGESIZE = "20";



    public static String getSignString(String parameterString){
        if(parameterString.startsWith("?")){
            parameterString = parameterString.substring(1);
        }
        String[] params = parameterString.split("&");
        Map<String, String> paramTreeMap = new TreeMap<String, String>();
        for(String parameter : params){
            int pos = parameter.indexOf("=");
            String key = parameter.substring(0,pos);
            String val = parameter.substring(pos+1);
            if(key!=null && !"".endsWith(key.trim()) && !"sign".equals(key)){
                if(val!=null&&!"".equals(val.trim())) {
                    paramTreeMap.put(key, val);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        Iterator<String> iter = paramTreeMap.keySet().iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            sb.append(name).append(paramTreeMap.get(name));
        }
        sb.append(SECURETKEY);

        Log.d("tan","sb.toString() is " + sb.toString() + "\n");
        Log.d("tan","----------------------sign="+MD5Encoder.encode(sb.toString()));
        return MD5Encoder.encode(sb.toString());
    }

    public static String assembleStrings(String... params) {
        String str = "";
        if (params.length == 0 || params.length % 2 != 0) throw new IllegalArgumentException();
        for (int i = 0; i<params.length; i++) {
            if (i%2==0) {
                str += params[i];
            }
            if (i%2!=0) {
                str += "=" + params[i] + "&";
            }
        }
        String real = str.substring(0, str.length()-1);
        real=real.replace("null","");
        Log.d("tan","real " + real+ "\n");
        return real;
    }

}
