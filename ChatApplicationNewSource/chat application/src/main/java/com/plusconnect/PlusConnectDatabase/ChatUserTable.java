package com.plusconnect.PlusConnectDatabase;

import android.database.sqlite.SQLiteDatabase;

import com.plusconnect.Beans.ChatUserBean;

/**
 * Created by ambesh on 08-09-2015.
 */
public class ChatUserTable {
    private String CHAT_USER_TABLE="chat_user_table";



    public void  onCreate(SQLiteDatabase sqLiteDatabase){

     String tableCreationQuery=   "create table IF NOT EXISTS "
                + CHAT_USER_TABLE + " (" + ChatUserBean.SENTBY + "  varchar " + " , "+ ChatUserBean.SENT_TO + " varchar" +" , "+ ChatUserBean.EXTENTION
                + " varchar"
                +" , "+ ChatUserBean.FILENAME + " varchar" +" , "+ ChatUserBean.GROUPNAMEIFAPPLICABLE + " varchar"+" , "+ChatUserBean.FILENAME + " varchar"+
             ChatUserBean.ISERROR + " varchar"+ChatUserBean.ISMEGGAGESENTTOUSERFROMSERVERACKNL
             + " varchar"+ChatUserBean.ISMESSAGESENTTOSERVERFROMUSERACKNL + " varchar"+ChatUserBean.MESSAGE + " varchar"
             +ChatUserBean.MESSAGE_DATA + " varchar"+ChatUserBean.MESSAGEID + " varchar"+ChatUserBean.MESSAGESENTOTYPE + " varchar"
             +ChatUserBean.MESSAGETYPE + " varchar"+ChatUserBean.MSGREADACKNLBYCLIENT + " varchar"+ChatUserBean.MSGREADACKNLBYSERVERTOCLIENT + " varchar"+
             ChatUserBean.TIMEINMILLISECS + " varchar"+")";


        sqLiteDatabase.execSQL(tableCreationQuery);



    }





}
