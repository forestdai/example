package com.dx_tech.wnlbs.smartcommunity.owner.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.dx_tech.wnlbs.smartcommunity.owner.sqlite.DBHelper;
/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 * 全局用户管理中心模块
 * 所有有关用户属性均以此单例维护
 */

public class UserInfo {
    private static Context mContext;
    /** token **/
    public String token = null;
    /** 唯一识别ID **/
    public String userId = "-1";
    /** 手机 **/
    public String mobile = null;
    /** 昵称 **/
    public String userName = null;
    /** 电子邮箱 **/
    public String email = null;
    /** 最后登录时间 **/
    public String lastLogin = null;
    /** 头像地址 **/
    public String imageUrl = null;
    /** 微信 **/
    public String weiXin = null;
    /** qq **/
    public String qq = null;
    /** 自我介绍 */
    public String signature = null;
    /** 身份证图片地址 **/
    public String idCardPic = null;
    /** 真实姓名 */
    public String realName = null;
    /** 身份证号码 */
    public String regNumber = null;
    /** 所在城市 */
    public String city = null;
    /**  */
    public String status;

    private UserInfo(){
        DBHelper dbHelper = DBHelper.getInstance(mContext).open();
        if(dbHelper.tabbleIsExist("userInfo")){
            try{
                readInfo(dbHelper);
            }catch (NullPointerException e){
                createUserInfo(dbHelper);
            }
        }else{
            createUserInfo(dbHelper);
        }
        DBHelper.getInstance(mContext).closeConnection();
        
    }

    private static class Hodler{
        private static final UserInfo INSTANCE = new UserInfo();
    }
    
    public static final UserInfo getInstance(Context context){
        mContext = context;
        return Hodler.INSTANCE;
    }
    
    private synchronized void createUserInfo(DBHelper dbHelper) {
        String sql = "DROP TABLE IF EXISTS userInfo";
        dbHelper.execSQL(sql);
        
        String user = "CREATE TABLE userInfo (id integer primary key autoincrement,"
                + "userName text, "
                + "mobile text, "
                + "userId text, "
                + "token text, "
                + "lastLogin text, "
                + "email text, "
                + "imageUrl text, "
                + "weiXin text, "
                + "qq text, "
                + "realName text, "
                + "regNumber text, "
                + "city text, "
                + "idCardPic text,"
                + "signature text,"
                + "status text)";
        dbHelper.execSQL(user);
        
        ContentValues values = new ContentValues();
        
        values.put("token", token);
        values.put("userId", userId);
        values.put("mobile", mobile);
        values.put("userName", userName);
        values.put("email", email);
        values.put("lastLogin", lastLogin);
        values.put("imageUrl", imageUrl);
        values.put("weiXin", weiXin);
        values.put("qq", qq);

        values.put("realName", realName);
        values.put("regNumber", regNumber);
        values.put("city", city);
        values.put("status", status);
        values.put("idCardPic", idCardPic);
        values.put("signature", signature);

        dbHelper.insert("userInfo", values);
    }
    


    public synchronized void upDataUserInfo(){
        DBHelper dbHelper = DBHelper.getInstance(mContext).open();
        if(!dbHelper.tabbleIsExist("userInfo")){
            createUserInfo(dbHelper);
        }
        ContentValues values = new ContentValues();

        checkPut(dbHelper, values,"token", token);
        checkPut(dbHelper, values,"userId", userId);
        checkPut(dbHelper, values,"mobile", mobile);
        checkPut(dbHelper, values,"userName", userName);
        checkPut(dbHelper, values,"email", email);
        checkPut(dbHelper, values,"lastLogin", lastLogin);
        checkPut(dbHelper, values,"imageUrl", imageUrl);
        checkPut(dbHelper, values,"weiXin", weiXin);
        checkPut(dbHelper, values,"qq", qq);
        checkPut(dbHelper, values, "signature", signature);

        checkPut(dbHelper, values, "realName", realName);
        checkPut(dbHelper, values, "regNumber", regNumber);
        checkPut(dbHelper, values, "city", city);
        checkPut(dbHelper, values, "status", status);
        checkPut(dbHelper, values, "idCardPic", idCardPic);

        dbHelper.update("userInfo", values, null, null);
        DBHelper.getInstance(mContext).closeConnection();

//        if(Utils.DEBUG){
//            Utils.moveDb();
//        }

    }

    private synchronized void checkPut(DBHelper dbHelper, ContentValues values, String key, String value){
        if(dbHelper.checkColumnExist("userInfo",key)){
            values.put(key, value);
        }else{
            ContentValues extra = new ContentValues();
            extra.put(key, value);
            dbHelper.insert("userInfo", extra);
        }
    }
    
    public synchronized void clearUserInfo(){
        DBHelper dbHelper = DBHelper.getInstance(mContext).open();
        dbHelper.deleteTable("userInfo");
        DBHelper.getInstance(mContext).closeConnection();

        token = null;
        userId = "-1";
        mobile = null;
        userName = null;
        email = null;
        lastLogin = null;
        imageUrl = null;
        weiXin = null;
        qq = null;
        realName = null;
        regNumber = null;
        city = null;
        status = null;
        idCardPic = null;
        signature = null;
    }
    
    private synchronized void readInfo(DBHelper dbHelper){

        this.token = getValue(dbHelper, "token");
        this.userId = getValue(dbHelper, "userId");
        this.mobile = getValue(dbHelper, "mobile");
        this.userName = getValue(dbHelper, "userName");
        this.email = getValue(dbHelper, "email");
        this.lastLogin = getValue(dbHelper, "lastLogin");
        this.imageUrl = getValue(dbHelper, "imageUrl");
        this.weiXin = getValue(dbHelper, "weiXin");
        this.qq = getValue(dbHelper, "qq");
        this.realName = getValue(dbHelper, "realName");
        this.regNumber = getValue(dbHelper, "regNumber");
        this.city = getValue(dbHelper, "city");
        this.status = getValue(dbHelper, "status");
        this.idCardPic = getValue(dbHelper, "idCardPic");
        this.signature = getValue(dbHelper, "signature");
    }

    private String getValue(DBHelper helper, String key){
        Cursor mCursor = helper.findOne(false, "userInfo", new String[]{key}, null, null, null, null, null, null);
        String value = mCursor.getString(mCursor.getColumnIndexOrThrow(key));
        mCursor.close();
        return value;
        
    }
}
