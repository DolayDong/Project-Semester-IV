package com.dolayindustries.projectkuliah;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dolayindustries.projectkuliah.database.DataHelper;

public class RegistrasiActivity extends AppCompatActivity {
    private EditText editTextNama, editTextNim, editTextEmail, editTextPassword, editTextKonfirmasiPassword;
    private TextView textViewMasuk;
    private Button buttonDaftar;
    private DataHelper dataHelper;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        inisialisasiElementLayout();
        dataHelper = new DataHelper(this);

        textViewMasuk.setOnClickListener(v -> pindahHalamanMasuk());
        buttonDaftar.setOnClickListener(v -> {
                if (validInputDataUser()){
                    SQLiteDatabase databaseRead = dataHelper.getReadableDatabase();
                    cursor = databaseRead.rawQuery("SELECT * FROM tabelakun WHERE username ='" + editTextNim.getText().toString() + "';", null);
                    cursor.moveToFirst();
                    if (cursor.getCount() > 0){
                        AlertDialog.Builder alertDialogSudahAdaPengguna = new AlertDialog.Builder(RegistrasiActivity.this);
                        alertDialogSudahAdaPengguna.setTitle("Perhatian!!");
                        alertDialogSudahAdaPengguna.setMessage("Akun dengan nim " + editTextNim.getText().toString()+ " Sudah terdaftar. Harap Login");
                        alertDialogSudahAdaPengguna.setPositiveButton("Ok", (dialog, which) -> dialog.cancel());
                        alertDialogSudahAdaPengguna.show().create();
                    } else {
                        int role;
                        if (editTextNim.getText().toString().contains("180552")) {
                            role = 1;
                        } else {
                            role = 2;
                        }
                        SQLiteDatabase database = dataHelper.getWritableDatabase();
                        database.execSQL("INSERT INTO tabelakun(username, nama, email, role, password) VALUES('"+editTextNim.getText().toString()+"','"+editTextNama.getText().toString()+"','"+editTextEmail.getText().toString()+"','"+ role +"','"+editTextPassword.getText().toString() +"');");

                        if (database.isDatabaseIntegrityOk()){
                            Toast.makeText(getApplicationContext(),"Akun dengan nama " + editTextNama.getText().toString() + " Berhasil ditambahkan, Silahkan login", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        });
    }

    private void inisialisasiElementLayout(){
        buttonDaftar = findViewById(R.id.tombol_buat_akun_registrasi);
        textViewMasuk = findViewById(R.id.text_view_login_registrasi);
        editTextEmail = findViewById(R.id.e_text_alamat_email_registrasi);
        editTextNama = findViewById(R.id.e_text_nama_pengguna_registrasi);
        editTextNim = findViewById(R.id.e_text_nim_nip_registrasi);
        editTextPassword = findViewById(R.id.e_text_password_registrasi);
        editTextKonfirmasiPassword = findViewById(R.id.e_text_konfirmasi_password_registrasi);
    }

    private void pindahHalamanMasuk(){
        Intent pindahKeMasuk = new Intent(RegistrasiActivity.this, LoginActivity.class);
        startActivity(pindahKeMasuk);
        this.finish();
    }

    private boolean validInputDataUser(){
        boolean validData = true;
        if (editTextNim.getText().toString().isEmpty()){
            editTextNim.setError("field nim tidak boleh kosong");
            validData = false;
        } else if(!editTextNim.getText().toString().contains("180442") && !editTextNim.getText().toString().contains("180552")){ //cek nim jika tidak mengandung angka 1804421000
            editTextNim.setError("username tidak valid. username harus mengandung karakter nim anda atau nip");
            validData = false;
        }

        if (editTextNama.getText().toString().isEmpty()){
            editTextNama.setError("field nama depan tidak boleh kosong");
            validData = false;
        }

        if (editTextPassword.getText().toString().isEmpty()){
            editTextPassword.setError("field password tidak boleh kosong");
            validData = false;
        } else if(editTextPassword.getText().toString().length() < 8){ //cek jumlah karakter password
            editTextPassword.setError("minimal 8 character ");
            validData = false;
        }

        if (editTextKonfirmasiPassword.getText().toString().isEmpty()){
            editTextKonfirmasiPassword.setError("field konfirmasi tidak boleh kosng");
            validData = false;
        } else if(!(editTextKonfirmasiPassword.getText().toString().equals(editTextPassword.getText().toString()))){ //cek apakah konfirmasi sama dengan password
            editTextKonfirmasiPassword.setError("password tidak sama");
            validData = false;
        }

        if (!editTextEmail.getText().toString().contains("@")){
            editTextEmail.setError("email harus mengandung karakter '@'");
            validData = false;
        }
        return validData;
    }

}
