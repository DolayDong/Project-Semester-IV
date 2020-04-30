package com.dolayindustries.projectkuliah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RegistrasiActivity extends AppCompatActivity {
    private TextView textViewMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        inisialisasiElementLayout();

        textViewMasuk.setOnClickListener(v -> pindahHalamanMasuk());
    }

    private void inisialisasiElementLayout(){
        textViewMasuk = findViewById(R.id.text_view_login_registrasi);
    }

    private void pindahHalamanMasuk(){
        Intent pindahKeMasuk = new Intent(RegistrasiActivity.this, LoginActivity.class);
        startActivity(pindahKeMasuk);
    }
}
