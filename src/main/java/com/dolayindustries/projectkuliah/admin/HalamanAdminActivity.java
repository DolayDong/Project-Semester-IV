package com.dolayindustries.projectkuliah.admin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dolayindustries.projectkuliah.LoginActivity;
import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.adapter.MyAdapterViewPager;
import com.dolayindustries.projectkuliah.admin.fragment.FragmentAccount;
import com.dolayindustries.projectkuliah.admin.fragment.FragmentHome;
import com.dolayindustries.projectkuliah.admin.fragment.FragmentNotifications;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class HalamanAdminActivity extends AppCompatActivity {

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_admin);


        viewPager = findViewById(R.id.view_pager_admin);
        setupViewPager(viewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu_admin);
        bottomNavigationView.getOrCreateBadge(R.id.menu_notifikasi_admin);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_dashboard:
                    viewPager.setCurrentItem(0, true);
                    break;
                case R.id.menu_notifikasi_admin:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.menu_account_admin:
                    viewPager.setCurrentItem(2, true);
                    break;
            }
            return true;
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        dataPengajuanDariDatabase(bottomNavigationView);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA_LOGIN", Context.MODE_PRIVATE);
        String dataLogin = sharedPreferences.getString("login", null);
        String dataRole = sharedPreferences.getString("role", null);

        assert dataRole != null;
        if (dataLogin != null && !dataRole.equalsIgnoreCase("admin")) {
            Intent intentLogin = new Intent(this, LoginActivity.class);
            startActivity(intentLogin);
        }

    }

    private void setupViewPager(ViewPager viewPagerParams){
        MyAdapterViewPager myAdapter = new MyAdapterViewPager(getSupportFragmentManager());
        myAdapter.addFragment(new FragmentHome());
        myAdapter.addFragment(new FragmentNotifications());
        myAdapter.addFragment(new FragmentAccount());

        viewPagerParams.setAdapter(myAdapter);
    }

    public void logout(){
        //menu app bar logout
        SharedPreferences HapusStatusLogin = getApplicationContext().getSharedPreferences("DATA_LOGIN", MODE_PRIVATE);
        SharedPreferences.Editor preferencesEditor = HapusStatusLogin.edit();
        //jika logout diclick, maka data login dihapus
        preferencesEditor.remove("login").apply();
        preferencesEditor.remove("username").apply();
        preferencesEditor.remove("role").apply();

        //dan diganti status dengan tidak login
        preferencesEditor.putString("login", "tidak");
        preferencesEditor.apply();

        //lempar ke halaman login
        Intent logOut = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logOut);

        //tutup activity ini
        finish();
    }

    public String getDataNimDariSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("DATA_LOGIN", MODE_PRIVATE);

        return sharedPreferences.getString("username", null);
    }

    private void dataPengajuanDariDatabase(BottomNavigationView bottomNavigationView) throws SQLException {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        SQLiteDatabase database = dataHelper.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery("SELECT * FROM tabelpengajuan WHERE dibaca = 'terkirim';", null);
        bottomNavigationView.getOrCreateBadge(R.id.menu_notifikasi_admin);
        BadgeDrawable badgeDrawableNotifikasi = bottomNavigationView.getBadge(R.id.menu_notifikasi_admin);
        int jumlahNotifikasi = cursor.getCount();
        assert badgeDrawableNotifikasi != null;
        if (jumlahNotifikasi != 0) {
            badgeDrawableNotifikasi.setNumber(cursor.getCount());
        } else {
            badgeDrawableNotifikasi.setVisible(false);
        }
    }

    public static void setGambar(final StorageReference storageReference, ImageView imageView, Context contextProgress) {
        try {
            ProgressDialog progressDialog = new ProgressDialog(contextProgress);
            progressDialog.setTitle("Sedang mengambil data gambar...");
            progressDialog.show();

            File file = File.createTempFile("image", "jpg");
            storageReference.getFile(file).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(contextProgress, e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage((int) progress + "% sedang mengambil data gambar. harap tunggu");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
