package com.dx_tech.wnlbs.smartcommunity.owner.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 戴圣伟 on 0016 2017/10/16.
 * 全局数据库
 * 全局对数据库的读写行为均从此入口
 */

public class DBHelper {

    private static DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static final String DATABASE_NAME = "smartcommunity.db";

    /**
     * 数据库SDCard额外存储路径适配
    * */
    private static final String DATABASE_PATH = Environment.getDataDirectory() + "...";

    private static int DATABASE_VERSION = 1;

    private static Context mContext;
    

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    private DBHelper() {
        mDbHelper = new DatabaseHelper(mContext);
    }
    
    private static class Hodler{
        private static final DBHelper INSTANCE = new DBHelper();
    }
    
    public static synchronized final DBHelper getInstance(Context context){
        mContext = context;
        return Hodler.INSTANCE;
    }

    public synchronized DBHelper open() throws SQLException {
        if(mOpenCounter.incrementAndGet() == 1){
            File file = new File(DATABASE_PATH);
            if(file.isFile() && file.exists()){
                mDb = SQLiteDatabase.openOrCreateDatabase(file, null);
            }else{
                mDb = mDbHelper.getWritableDatabase();
            }
//            mDb = mDbHelper.getWritableDatabase();
        }
        return this;
    }

    public int getDatabaseVersion(){
        return mDb.getVersion();
    }
    /**
     * 关闭数据源
     * 
     * @author SHANHY
     */
    public synchronized void closeConnection() {
        if(mOpenCounter.decrementAndGet() == 0) {
            if (mDb != null && mDb.isOpen())
                mDb.close();
            if (mDbHelper != null)
                mDbHelper.close();
        }
    }

    /**
     * 插入数据 参数
     * 
     * @param tableName 表名
     * @param initialValues 要插入的列对应值
     * @return
     * @author SHANHY
     */
    public long insert(String tableName, ContentValues initialValues) {

        return mDb.insert(tableName, null, initialValues);
    }
    
    /**
     * 是否存在table
     * 
     * @param tableName 表名
     */
    public boolean tabbleIsExist(String tableName) {
        boolean result = false;
        if(tableName == null){
                return false;
        }
        Cursor cursor = null;
        String sql = "SELECT COUNT(*) FROM sqlite_master where type='table' and name='"+tableName+"'";
        cursor = mDb.rawQuery(sql, null);
        if(cursor.moveToNext()){
            int count = cursor.getInt(0);
            if(count>0){
                result = true;
            }
        }
        cursor.close();
        return result;
    }

    /**
     * 方法1：检查某表列是否存在
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public boolean checkColumnExist(String tableName
            , String columnName) {
        boolean result = false ;
        Cursor cursor = null ;
        try{
            //查询一行
            cursor = mDb.rawQuery( "SELECT * FROM " + tableName + " LIMIT 0"
                    , null );
            result = cursor != null && cursor.getColumnIndex(columnName) != -1 ;
        }catch (Exception e){
            Log.e("DBHelper", "checkColumnExists..." + e.getMessage()) ;
        }finally{
            if(null != cursor && !cursor.isClosed()){
                cursor.close() ;
            }
        }

        return result ;
    }

    /**
     * 删除数据
     * 
     * @param tableName 表名
     * @param deleteCondition 条件
     * @param deleteArgs 条件对应的值（如果deleteCondition中有“？”号，将用此数组中的值替换，一一对应）
     * @return
     * @author SHANHY
     */
    public boolean delete(String tableName, String deleteCondition, String[] deleteArgs) {

        return mDb.delete(tableName, deleteCondition, deleteArgs) > 0;
    }

    
    public void deleteTable(String tablename){
        String sql = "DROP TABLE IF EXISTS "+ tablename;
        mDb.execSQL(sql);
    }
    
    /**
     * 更新数据
     * 
     * @param tableName 表名
     * @param initialValues 要更新的列
     * @param selection 更新的条件
     * @param selectArgs 更新条件中的“？”对应的值
     * @return
     * @author SHANHY
     */
    public boolean update(String tableName, ContentValues initialValues, String selection, String[] selectArgs) {
        return mDb.update(tableName, initialValues, selection, selectArgs) > 0;
    }

    /**
     * 取得一个列表
     * 
     * @param distinct 是否去重复
     * @param tableName 表名
     * @param columns 要返回的列
     * @param selection 条件
     * @param selectionArgs 条件中“？”的参数值
     * @param groupBy 分组
     * @param having 分组过滤条件
     * @param orderBy 排序
     * @return
     * @author SHANHY
     */
    public Cursor findList(boolean distinct, String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        if(isTableExist(tableName) && columns != null){
            for(String colum : columns){
                if(!isColumnExist(tableName ,colum)){
                    return null;
                }
            }
        }
        return mDb.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 取得单行记录
     * 
     * @param distinct 是否去重复
     * @param tableName 表名
     * @param columns 获取的列数组
     * @param selection 条件
     * @param selectionArgs 条件中“？”对应的值
     * @param groupBy 分组
     * @param having 分组条件
     * @param orderBy 排序
     * @param limit 数据区间
     * @param distinct 是否去重复
     * @return
     * @throws SQLException
     * @author SHANHY
     */
    public Cursor findOne(boolean distinct, String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException {

        Cursor mCursor = findList(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * 执行SQL(带参数)
     * 
     * @param sql
     * @param args SQL中“？”参数值
     * @author SHANHY
     */
    public void execSQL(String sql, Object[] args) {
        mDb.execSQL(sql, args);

    }

    /**
     * 执行SQL
     * 
     * @param sql
     * @author SHANHY
     */
    public void execSQL(String sql) {
        mDb.execSQL(sql);

    }

    /**
     * 判断某张表是否存在
     * 
     * @param tableName 表名
     * @return
     */
    public boolean isTableExist(String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }

        try {
            Cursor cursor = null;
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "'";
            cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

            cursor.close();
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 判断某张表中是否存在某字段(注，该方法无法判断表是否存在，因此应与isTableExist一起使用)
     * 
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public boolean isColumnExist(String tableName, String columnName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }

        try {
            Cursor cursor = null;
            String sql = "select count(1) as c from sqlite_master where type ='table' and name ='" + tableName.trim() + "' and sql like '%" + columnName.trim() + "%'";
            cursor = mDb.rawQuery(sql, null);
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    result = true;
                }
            }

            cursor.close();
        } catch (Exception e) {
        }
        return result;
    }

}