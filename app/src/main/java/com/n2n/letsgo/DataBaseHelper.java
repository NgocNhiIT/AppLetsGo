package com.n2n.letsgo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {


    private Context dbcontext;
    private SQLiteDatabase db;
    private static final String dbPath = "/data/data/com.n2n.letsgo/databases/data";
    private static final String dbName = "data.db";
    private static final int dbVersion = 1;

    public DataBaseHelper(@Nullable Context context) {
        super(context, dbName, null, dbVersion  );
        this.dbcontext = context;

    }

    public void copyDB2SDCard() throws SQLiteException {
        boolean check;
        try {
            File file = new File(dbPath);
            check = file.exists();
            if (check) {
                //file.delete();
                this.close();
            } else if (!check) {
                this.getReadableDatabase();
                //
                InputStream myInput = dbcontext.getAssets().open(dbName);
                String outFileName = dbPath;
                OutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        } catch (Exception ex) {
            throw new Error("Lỗi! Không thể copy");
        }
    }

    public void OpenDB() {
        db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }


    public Cursor getCursor(String strSQL) {
        OpenDB();
        Cursor c = db.rawQuery(strSQL, null);
        return c;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
