package com.dolayindustries.projectkuliah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private Button buttonLogin;
    private ProgressBar loadingLogin;
    private TextView textViewBuatAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        inisialisasiElement();
        
        buttonLogin.setOnClickListener(v -> loadingLogin.setVisibility(View.VISIBLE));

        textViewBuatAkun.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textViewBuatAkun.setOnClickListener(v -> pindahHalamanRegistrasi());
    }
    
    private void inisialisasiElement(){
        buttonLogin = findViewById(R.id.tombol_login);
        loadingLogin = findViewById(R.id.loading_login);
        textViewBuatAkun = findViewById(R.id.t_view_buat_akun);
    }

    private void pindahHalamanRegistrasi(){
        Intent pindahKeRegistrasi = new Intent(LoginActivity.this, RegistrasiActivity.class);
        startActivity(pindahKeRegistrasi);
    }
}
