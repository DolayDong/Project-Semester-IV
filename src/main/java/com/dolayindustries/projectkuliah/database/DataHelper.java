package com.dolayindustries.projectkuliah.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataHelper extends SQLiteOpenHelper {
    private static final String NAMA_DATABASE = "projectgoakademic.db";
    private static final int VERSI_DATABASE = 1;
    public DataHelper(Context context) {
        super(context, NAMA_DATABASE, null, VERSI_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableakun = "CREATE TABLE tabelakun(username INTEGER PRIMARY KEY, nama TEXT, email TEXT, role INTEGER, password TEXT);";
        String tablepengajuan = "CREATE TABLE tabelpengajuan(id_pengajuan INTEGER PRIMARY KEY AUTOINCREMENT, username INTEGER, jurusan TEXT, tanggallahir TEXT, statusapprove TEXT, dibaca TEXT, namakampus TEXT, jenispengajuan TEXT, tanggalpengajuan DATETIME, alamatpengaju TEXT, waktupengajuan TIME);";
        db.execSQL(tableakun);
        db.execSQL(tablepengajuan);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
