package org.bistu.garbageclassification.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "gcdb";
    private final static int DB_VERSION = 1;

    public static final String TABLE_NAME_SH = "search_history";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_GNAME = "garbage_name";

    //建表，删表
    private static String SQL_CREATE_DB = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SH + " ("
            + COLUMN_NAME_GNAME + " TEXT PRIMARY KEY,"
            + COLUMN_NAME_DATE + " DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)";

    private static String SQL_DELETE_DB = "DROP TABLE IF EXISTS " + TABLE_NAME_SH;

    public SQLiteDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
