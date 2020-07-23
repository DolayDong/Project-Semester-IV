package com.dolayindustries.projectkuliah;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dolayindustries.projectkuliah.database.DataHelper;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;

public class RegistrasiActivity extends AppCompatActivity {
    private EditText editTextNama, editTextNim, editTextEmail, editTextPassword, editTextKonfirmasiPassword;
    private TextView textViewMasuk, textViewNamaFile;
    private Button buttonDaftar, buttonPilihFoto;
    private DataHelper dataHelper;
    private Uri filePath;
    private Cursor cursor;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        inisialisasiElementLayout();
        dataHelper = new DataHelper(this);

        Dexter.withContext(RegistrasiActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                buttonPilihFoto.setOnClickListener(v -> {
                    pilihFoto();
                });
                textViewMasuk.setOnClickListener(v -> pindahHalamanMasuk());
                buttonDaftar.setOnClickListener(v -> {
                    if (validInputDataUser()) {
                        uploadFoto();
                    }
                });

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();
    }

    private void uploadFoto() {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(RegistrasiActivity.this);
            progressDialog.setTitle("Ciee Nungguin ya..");
            progressDialog.show();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profil/").child(editTextNim.getText().toString());
            storageReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                insertData();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(RegistrasiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage((int) progress + "% Sedang diproses, harap tunggu... ");
            });
        } else {
            Toast.makeText(this, "Anda belum memilih foto profil", Toast.LENGTH_SHORT).show();
        }
    }

    private void pilihFoto() {
        Intent intentFoto = new Intent();
        intentFoto.setType("image/*");
        intentFoto.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentFoto, "Pilih Foto Profil"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK & data != null && data.getData() != null) {
            filePath = data.getData();
            FileDetail fileDetail = getFileDetailFromUri(this, filePath);
            textViewNamaFile.setVisibility(View.VISIBLE);
            textViewNamaFile.setText(fileDetail.fileName);
        }
    }

    private void inisialisasiElementLayout() {
        buttonPilihFoto = findViewById(R.id.btn_pilih_photo_profil_reist);
        textViewNamaFile = findViewById(R.id.t_view_nama_file);
        buttonDaftar = findViewById(R.id.tombol_buat_akun_registrasi);
        textViewMasuk = findViewById(R.id.text_view_login_registrasi);
        editTextEmail = findViewById(R.id.e_text_alamat_email_registrasi);
        editTextNama = findViewById(R.id.e_text_nama_pengguna_registrasi);
        editTextNim = findViewById(R.id.e_text_nim_nip_registrasi);
        editTextPassword = findViewById(R.id.e_text_password_registrasi);
        editTextKonfirmasiPassword = findViewById(R.id.e_text_konfirmasi_password_registrasi);
    }

    private void pindahHalamanMasuk() {
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

        if (editTextKonfirmasiPassword.getText().toString().isEmpty()) {
            editTextKonfirmasiPassword.setError("field konfirmasi tidak boleh kosng");
            validData = false;
        } else if (!(editTextKonfirmasiPassword.getText().toString().equals(editTextPassword.getText().toString()))) { //cek apakah konfirmasi sama dengan password
            editTextKonfirmasiPassword.setError("password tidak sama");
            validData = false;
        }

        if (!editTextEmail.getText().toString().contains("@")) {
            editTextEmail.setError("email harus mengandung karakter '@'");
            validData = false;
        }

        if (filePath == null) {
            Toast.makeText(this, "Anda belum memilih foto", Toast.LENGTH_SHORT).show();
            validData = false;
        }
        return validData;
    }

    private void insertData() {

        SQLiteDatabase databaseRead = dataHelper.getReadableDatabase();
        cursor = databaseRead.rawQuery("SELECT * FROM tabelakun WHERE username ='" + editTextNim.getText().toString() + "';", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            AlertDialog.Builder alertDialogSudahAdaPengguna = new AlertDialog.Builder(RegistrasiActivity.this);
            alertDialogSudahAdaPengguna.setTitle("Perhatian!!");
            alertDialogSudahAdaPengguna.setMessage("Akun dengan nim " + editTextNim.getText().toString() + " Sudah terdaftar. Harap Login");
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
            database.execSQL("INSERT INTO tabelakun(username, nama, email, role, password) VALUES('" + editTextNim.getText().toString() + "','" + editTextNama.getText().toString() + "','" + editTextEmail.getText().toString() + "','" + role + "','" + editTextPassword.getText().toString() + "');");

            if (database.isDatabaseIntegrityOk()) {
                Toast.makeText(getApplicationContext(), "Akun dengan nama " + editTextNama.getText().toString() + " Berhasil ditambahkan, Silahkan login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static FileDetail getFileDetailFromUri(final Context context, final Uri uri) {
        FileDetail fileDetail = null;
        if (uri != null) {
            fileDetail = new FileDetail();
            // File Scheme.
            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
                File file = new File(uri.getPath());
                fileDetail.fileName = file.getName();
                fileDetail.fileSize = file.length();
            }
            // Content Scheme.
            else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                Cursor returnCursor =
                        context.getContentResolver().query(uri, null, null, null, null);
                if (returnCursor != null && returnCursor.moveToFirst()) {
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    fileDetail.fileName = returnCursor.getString(nameIndex);
                    fileDetail.fileSize = returnCursor.getLong(sizeIndex);
                    returnCursor.close();
                }
            }
        }
        return fileDetail;
    }

    /**
     * File Detail.
     * <p>
     * 1. Model used to hold file details.
     */
    public static class FileDetail {

        // fileSize.
        public String fileName;

        // fileSize in bytes.
        public long fileSize;

        /**
         * Constructor.
         */
        public FileDetail() {

        }
    }
}
