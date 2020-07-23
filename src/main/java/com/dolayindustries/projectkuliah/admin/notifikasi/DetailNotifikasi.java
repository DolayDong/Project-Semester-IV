package com.dolayindustries.projectkuliah.admin.notifikasi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.admin.fragment.FragmentNotifications;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.dolayindustries.projectkuliah.admin.HalamanAdminActivity.setGambar;

public class DetailNotifikasi extends AppCompatActivity {
    private TextView textViewNamaPengaju, textViewNimPengaju, textViewAlamatPengaju, textViewTtlPengaju, textViewJurusanPengaju, textViewNamaOrangTua, textViewAlamatOrangTua, textViewPekerjaanOrangTua;
    private ImageView imageViewPengaju;
    private Button buttonDisetujui, buttonAbaikan;
    private String idPengaju, idadmin, namaadmin;

    @SuppressLint("Recycle")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notifikasi);
        inisialisasi();
        idadmin = getDataNimDariSharedPreferences();

        idPengaju = getIntent().getStringExtra("id_pengaju");
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        SQLiteDatabase database = dataHelper.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery("SELECT nama FROM tabelakun WHERE username = '" + idadmin + "';", null);
        cursor.moveToFirst();
        namaadmin = cursor.getString(0);
        cursor = database.rawQuery("SELECT tabelakun.nama, tabelpengajuan.id_pengajuan, tabelpengajuan.username, tabelpengajuan.alamatpengaju, tabelpengajuan.tanggallahir, tabelpengajuan.jurusan, tabelpengajuan.tempatlahir, tabelpengajuan.namaorangtua, tabelpengajuan.alamatorangtua, tabelpengajuan.pekerjaanorangtua FROM tabelakun JOIN tabelpengajuan ON tabelpengajuan.username = tabelakun.username WHERE id_pengajuan ='" + idPengaju + "';", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            String tempat = cursor.getString(6) + ", " + cursor.getString(4);
            textViewNamaPengaju.setText(cursor.getString(0));
            textViewNimPengaju.setText(cursor.getString(2));
            textViewAlamatPengaju.setText(cursor.getString(3));
            textViewTtlPengaju.setText(tempat);
            textViewJurusanPengaju.setText(cursor.getString(5));
            textViewNamaOrangTua.setText(cursor.getString(7));
            textViewAlamatOrangTua.setText(cursor.getString(8));
            textViewPekerjaanOrangTua.setText(cursor.getString(9));
        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profil").child(textViewNimPengaju.getText().toString());
        setGambar(storageReference, imageViewPengaju, this);

        buttonAbaikan.setOnClickListener(v -> updateStatusDibacaDatabase());

        buttonDisetujui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatusDibacaDanApprove();
            }
        });

    }

    private void updateStatusDibacaDanApprove() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        SQLiteDatabase database = dataHelper.getWritableDatabase();
        Log.i("DATA ID ADMIN", idadmin);
        database.execSQL("UPDATE tabelpengajuan SET dibaca = 'dibaca', statusapprove = 'setuju', tanggalapprove = date('now'), idadmin = '" + namaadmin + "' WHERE id_pengajuan = '" + idPengaju + "';");
        if (database.isDatabaseIntegrityOk()) {
            startActivity(new Intent(DetailNotifikasi.this, FragmentNotifications.class));
        }
    }


    private void updateStatusDibacaDatabase() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        SQLiteDatabase database = dataHelper.getWritableDatabase();

        database.execSQL("UPDATE tabelpengajuan SET dibaca = 'dibaca' WHERE id_pengajuan = '" + idPengaju + "';");
        if (database.isDatabaseIntegrityOk()) {
            startActivity(new Intent(DetailNotifikasi.this, FragmentNotifications.class));
        }

    }

    private void inisialisasi() {
        buttonAbaikan = findViewById(R.id.btn_abaikan);
        buttonDisetujui = findViewById(R.id.btn_setujui);
        imageViewPengaju = findViewById(R.id.image_view_pengaju);
        textViewAlamatPengaju = findViewById(R.id.tv_value_alamat_pengaju);
        textViewJurusanPengaju = findViewById(R.id.tv_value_jurusan_pengaju);
        textViewNamaPengaju = findViewById(R.id.tv_value_nama_pengaju);
        textViewNimPengaju = findViewById(R.id.tv_value_nim_pengaju);
        textViewTtlPengaju = findViewById(R.id.tv_value_ttl_pengaju);
        textViewAlamatOrangTua = findViewById(R.id.tv_value_alamat_orang_tua);
        textViewNamaOrangTua = findViewById(R.id.tv_value_nama_orang_tua);
        textViewPekerjaanOrangTua = findViewById(R.id.tv_value_pekerjaan_orang_tua);


    }

    public String getDataNimDariSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA_LOGIN", MODE_PRIVATE);

        return sharedPreferences.getString("username", null);
    }
}
