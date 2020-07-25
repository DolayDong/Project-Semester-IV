package com.dolayindustries.projectkuliah;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dolayindustries.projectkuliah.admin.HalamanAdminActivity;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.service.AppService;
import com.dolayindustries.projectkuliah.user.HalamanUserActivity;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;
    private ProgressBar loadingLogin;
    private TextView textViewBuatAkun;
    private EditText editTextNamaPengguna, editTextPasswordPengguna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inisialisasiElement();
        buttonLogin.setOnClickListener(v -> namaDanPasswordValid());

        textViewBuatAkun.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        textViewBuatAkun.setOnClickListener(v -> pindahHalamanRegistrasi());

        /*cek login */
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA_LOGIN", Context.MODE_PRIVATE);
        String dataLogin = sharedPreferences.getString("login", null);
        String dataRole = sharedPreferences.getString("role", null);
        String dataUsrname = sharedPreferences.getString("username", null);

        if (dataLogin != null && dataLogin.equalsIgnoreCase("pindah")) {
            SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
            //jika logout diclick, maka data login dihapus
            preferencesEditor.remove("username").apply();
            preferencesEditor.remove("role").apply();

            Toast.makeText(LoginActivity.this, dataLogin, Toast.LENGTH_SHORT).show();
        }

                        //cek apakah sebelumnya user sudah login. jika ya
                        if (dataLogin != null && dataLogin.equalsIgnoreCase("ya")) {
                            assert dataRole != null;
                            if (dataRole.equalsIgnoreCase("user")) {
                                //langsung lempar ke activity menu user
                                Intent intentActivityMenuUser = new Intent(getApplicationContext(), HalamanUserActivity.class);
                                startActivity(intentActivityMenuUser);
                                LoginActivity.this.finish();
                            } else {
                                //activity menu admin
                                Intent intentActivityMenuAdmin = new Intent(getApplicationContext(), HalamanAdminActivity.class);
                                startActivity(intentActivityMenuAdmin);
                                LoginActivity.this.finish();
                            }

                        }
                        /*akhir cek login*/



    }

    private void inisialisasiElement(){
        editTextNamaPengguna = findViewById(R.id.e_text_username_login);
        editTextPasswordPengguna = findViewById(R.id.e_text_password_login);
        buttonLogin = findViewById(R.id.tombol_login);
        loadingLogin = findViewById(R.id.loading_login);
        textViewBuatAkun = findViewById(R.id.t_view_buat_akun);
    }

    private void pindahHalamanRegistrasi() {
        Intent pindahKeRegistrasi = new Intent(LoginActivity.this, RegistrasiActivity.class);
        startActivity(pindahKeRegistrasi);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(getApplicationContext(), AppService.class));
    }

    private void namaDanPasswordValid() {
        DataHelper dataHelper = new DataHelper(this);
        SQLiteDatabase database = dataHelper.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery("SELECT * FROM tabelakun WHERE username ='" + editTextNamaPengguna.getText().toString() + "';", null);
        cursor.moveToFirst();

        if (dataValid()) {
            loadingLogin.setVisibility(View.VISIBLE);
            if (cursor.getCount() > 0) {
                if (editTextPasswordPengguna.getText().toString().equals(String.valueOf(cursor.getString(4)))) {
                    if (cursor.getInt(3) == 1) {
                        pindahHalamanAdmin();
                    } else {
                        pindahHalamanUser();
                    }
                    this.finish();
                } else {
                    loadingLogin.setVisibility(View.GONE);
                    Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show();
                }
            } else {
                loadingLogin.setVisibility(View.GONE);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                alertDialog.setTitle("Perhatian");
                alertDialog.setMessage("Maaf, username belum terdaftar, silahkan buat akun terlebih dahulu");
                alertDialog.setPositiveButton("Registrasi", (dialog, which) -> pindahHalamanRegistrasi());
                alertDialog.setNegativeButton("Nanti", (dialog, which) -> dialog.cancel());
                alertDialog.show().create();
            }
        }

    }

    private boolean dataValid(){
        boolean valid = true;
        if (editTextPasswordPengguna.getText().toString().isEmpty()) {
            editTextPasswordPengguna.setError("field password tidak boleh kosong");
            valid = false;
        }

        if (editTextNamaPengguna.getText().toString().isEmpty()) {
            editTextNamaPengguna.setError("field username tidak boleh kosong");
            valid = false;
        } else if (!editTextNamaPengguna.getText().toString().contains("180442") && !editTextNamaPengguna.getText().toString().contains("180552")) {
            editTextNamaPengguna.setError("Username tidak Valid, Harus mengandung karakter nim / nip");
            valid = false;
        }
        return valid;
    }

    private void pindahHalamanUser() {
        //simpan data login di shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("DATA_LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("login", "ya");
        sharedPreferencesEditor.putString("username", editTextNamaPengguna.getText().toString());
        sharedPreferencesEditor.putString("role", "user");
        sharedPreferencesEditor.apply();

        // pindah ke Activity Menu User
        Intent intent = new Intent(getApplicationContext(), HalamanUserActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void pindahHalamanAdmin() {
        //simpan data login di shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("DATA_LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("login", "ya");
        sharedPreferencesEditor.putString("username", editTextNamaPengguna.getText().toString());
        sharedPreferencesEditor.putString("role", "admin");
        sharedPreferencesEditor.apply();

        //pindah ke halaman menu admin
        Intent intent = new Intent(getApplicationContext(), HalamanAdminActivity.class);
        startActivity(intent);
        finish();
    }
}
