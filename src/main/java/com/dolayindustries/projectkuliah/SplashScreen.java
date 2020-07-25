package com.dolayindustries.projectkuliah;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class SplashScreen extends AppCompatActivity {
    private ImageView imageViewLogo;
    private TextView textViewSambutan;
    private static int waktuSplash = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageViewLogo = findViewById(R.id.image_view_splashscreen);
        textViewSambutan = findViewById(R.id.kata_sambutan_splashscreen);

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        new Handler().postDelayed(() -> {
                            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                            finish();
                        }, waktuSplash);
                        Animation animasiSplashILogo = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animasi_spashscreen_gambar);
                        imageViewLogo.startAnimation(animasiSplashILogo);

                        Animation animasiSplashText = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.animasi_splashscreen_text_sambutan);
                        textViewSambutan.startAnimation(animasiSplashText);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        Toast.makeText(SplashScreen.this, "Aplikasi tidak diberi ijin mengakses data karu memori", Toast.LENGTH_SHORT).show();
                        Intent intentUbahPersetujuan = new Intent();
                        intentUbahPersetujuan.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intentUbahPersetujuan.setData(uri);
                        startActivity(intentUbahPersetujuan);
                    }
                }).check();
    }
}