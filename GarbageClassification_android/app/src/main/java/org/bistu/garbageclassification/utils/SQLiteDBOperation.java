package org.bistu.garbageclassification.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索记录
 * 建表，未做记录的增删改查
 */
public class SQLiteDBOperation {

    private String tableName;
    private SQLiteDBHelper sqLiteDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteDBOperation(Context context, String tableName) {
        sqLiteDBHelper = new SQLiteDBHelper(context);
        this.tableName = tableName;
    }

    //判断是否搜索过
    public boolean isHasRecord(String gname) {
        boolean isHasRecord = false;
        sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, null, "garbage_name=\""+gname+"\"", null, null, null, null);
        while (cursor.moveToNext()) {
            if (gname.equals(cursor.getString(cursor.getColumnIndexOrThrow("garbage_name")))) {
                isHasRecord = true;
            }
        }
        //关闭数据库
        sqLiteDatabase.close();
        return isHasRecord;
    }

    //增加新的记录，修改已有记录时间
    public boolean insertRecord(String gname) {
        if (!isHasRecord(gname)) { //未搜索过
            sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("garbage_name", gname);
            //添加
            sqLiteDatabase.insert(tableName, null, values);
            sqLiteDatabase.close();
            return true;
        } else { //搜索过
            sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();
            sqLiteDatabase.execSQL("REPLACE INTO " + SQLiteDBHelper.TABLE_NAME_SH + " (" +
                     SQLiteDBHelper.COLUMN_NAME_GNAME + ") VALUES (\""+gname+"\")");  //删旧增新

            sqLiteDatabase.close();
            return false;
        }
    }

    //删除所有记录
    public void deleteAllRecord() {
        sqLiteDatabase = sqLiteDBHelper.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+tableName);

        sqLiteDatabase.close();
    }

    //获取最近搜索记录10条
    public List<String> getTop10() {
        List<String> tops = new ArrayList<>();
        sqLiteDatabase = sqLiteDBHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, null, null, null, "date DESC limit 0,10");
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("garbage_name"));
            tops.add(name);
        }
        //关闭数据库
        sqLiteDatabase.close();

        return tops;
    }

}
