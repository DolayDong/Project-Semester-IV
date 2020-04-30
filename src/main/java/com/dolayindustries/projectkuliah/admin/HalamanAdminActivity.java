package com.dolayindustries.projectkuliah.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.adapter.MyAdapterViewPager;
import com.dolayindustries.projectkuliah.admin.fragment.FragmentHome;
import com.dolayindustries.projectkuliah.admin.fragment.FragmentNotifications;
import com.dolayindustries.projectkuliah.admin.fragment.FragmentAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HalamanAdminActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_admin);

        viewPager = findViewById(R.id.view_pager_admin);
        setupViewPager(viewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu_admin);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
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
            }
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
    }

    private void setupViewPager(ViewPager viewPagerParams){
        MyAdapterViewPager myAdapter = new MyAdapterViewPager(getSupportFragmentManager());
        myAdapter.addFragment(new FragmentHome());
        myAdapter.addFragment(new FragmentNotifications());
        myAdapter.addFragment(new FragmentAccount());

        viewPagerParams.setAdapter(myAdapter);
    }
}
