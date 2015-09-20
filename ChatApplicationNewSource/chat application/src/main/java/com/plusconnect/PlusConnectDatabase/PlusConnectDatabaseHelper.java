package com.plusconnect.PlusConnectDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ambesh on 08-09-2015.
 */
public class PlusConnectDatabaseHelper extends SQLiteOpenHelper {


    private ChatUserTable chatUserTable;


    public PlusConnectDatabaseHelper(Context context, String name
            , SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        chatUserTable=new ChatUserTable();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        chatUserTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
