package com.dolayindustries.projectkuliah.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dolayindustries.projectkuliah.LoginActivity;
import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.adapter.MyAdapterViewPager;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.user.fragment.FragmentAccount;
import com.dolayindustries.projectkuliah.user.fragment.FragmentHome;
import com.dolayindustries.projectkuliah.user.fragment.FragmentNotifications;
import com.dolayindustries.projectkuliah.user.fragment.FragmentPengajuan;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HalamanUserActivity extends AppCompatActivity {
    DataHelper dataHelper;
    private ViewPager viewPager;
    public static final String PREFERENCES = "ITEM_DIKLIK";
    public static final String KEY = "id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_user);

        viewPager = findViewById(R.id.view_pager_user);
        setViewPage(viewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu_user);
        bottomNavigationView.getOrCreateBadge(R.id.menu_notifikasi_user);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.menu_home_user:
                    viewPager.setCurrentItem(0, true);
                    break;
                case R.id.menu_ajukan_user:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.menu_notifikasi_user:
                    viewPager.setCurrentItem(2, true);
                    break;
                case R.id.menu_account_user:
                    viewPager.setCurrentItem(3, true);
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
        addJumlahNotifDisetujui(bottomNavigationView);
    }

    private void setViewPage(ViewPager viewPageParam){
        MyAdapterViewPager myAdapter = new MyAdapterViewPager(getSupportFragmentManager());
        myAdapter.addFragment(new FragmentHome());
        myAdapter.addFragment(new FragmentPengajuan());
        myAdapter.addFragment(new FragmentNotifications());
        myAdapter.addFragment(new FragmentAccount());

        viewPageParam.setAdapter(myAdapter);
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

    public void addJumlahNotifDisetujui(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.getOrCreateBadge(R.id.menu_notifikasi_user);
        BadgeDrawable badgeDrawableNotifUser = bottomNavigationView.getBadge(R.id.menu_notifikasi_user);
        dataHelper = new DataHelper(getApplicationContext());
        SQLiteDatabase database = dataHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM tabelpengajuan WHERE dibacauser = 0;", null);
        cursor.moveToFirst();
        assert badgeDrawableNotifUser != null;
        if (cursor.getCount() > 0) {
            badgeDrawableNotifUser.setNumber(cursor.getCount());
        } else {
            badgeDrawableNotifUser.setVisible(false);
        }
    }


}
