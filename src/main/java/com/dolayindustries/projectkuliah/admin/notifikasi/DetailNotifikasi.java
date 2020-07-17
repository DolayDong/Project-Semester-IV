package com.dolayindustries.projectkuliah.admin.notifikasi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.ScientificNumberFormatter;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.database.DataHelper;

public class DetailNotifikasi extends AppCompatActivity {
    private TextView textViewNamaPengaju, textViewNimPengaju, textViewAlamatPengaju, textViewTtlPengaju, textViewJurusanPengaju;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notifikasi);
        inisialisasi();

        String idPengaju = getIntent().getStringExtra("id_pengaju");
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        SQLiteDatabase database = dataHelper.getReadableDatabase();
        cursor = database.rawQuery("SELECT tabelakun.nama, tabelpengajuan.id_pengajuan, tabelpengajuan.username, tabelpengajuan.alamatpengaju, tabelpengajuan.tanggallahir, tabelpengajuan.jurusan, tabelpengajuan.tempatlahir FROM tabelakun JOIN tabelpengajuan ON tabelpengajuan.username = tabelakun.username WHERE id_pengajuan ='" + idPengaju + "';", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            String tempat = cursor.getString(6) + ", " + cursor.getString(4);
            textViewNamaPengaju.setText(cursor.getString(0));
            textViewNimPengaju.setText(cursor.getString(2));
            textViewAlamatPengaju.setText(cursor.getString(3));
            textViewTtlPengaju.setText(tempat);
            textViewJurusanPengaju.setText(cursor.getString(5));
        }

    }

    private void inisialisasi() {
        textViewAlamatPengaju = findViewById(R.id.tv_value_alamat_pengaju);
        textViewJurusanPengaju = findViewById(R.id.tv_value_jurusan_pengaju);
        textViewNamaPengaju = findViewById(R.id.tv_value_nama_pengaju);
        textViewNimPengaju = findViewById(R.id.tv_value_nim_pengaju);
        textViewTtlPengaju = findViewById(R.id.tv_value_ttl_pengaju);


    }
}
