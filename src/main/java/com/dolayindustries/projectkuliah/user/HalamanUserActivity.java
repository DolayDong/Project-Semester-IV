package com.dolayindustries.projectkuliah.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.adapter.MyAdapterViewPager;
import com.dolayindustries.projectkuliah.user.fragment.FragmentAccount;
import com.dolayindustries.projectkuliah.user.fragment.FragmentHome;
import com.dolayindustries.projectkuliah.user.fragment.FragmentNotifications;
import com.dolayindustries.projectkuliah.user.fragment.FragmentPengajuan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HalamanUserActivity extends AppCompatActivity {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_user);

        viewPager = findViewById(R.id.view_pager_user);
        setViewPage(viewPager);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_menu_user);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.menu_home_user:
                    viewPager.setCurrentItem(0,true);
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
    }

    private void setViewPage(ViewPager viewPageParam){
        MyAdapterViewPager myAdapter = new MyAdapterViewPager(getSupportFragmentManager());
        myAdapter.addFragment(new FragmentHome());
        myAdapter.addFragment(new FragmentPengajuan());
        myAdapter.addFragment(new FragmentNotifications());
        myAdapter.addFragment(new FragmentAccount());

        viewPageParam.setAdapter(myAdapter);
    }
}
