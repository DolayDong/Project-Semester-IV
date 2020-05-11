package com.dolayindustries.projectkuliah.user.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.user.HalamanUserActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccount extends Fragment {

    public FragmentAccount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_user, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_eject_black_24dp));
        
        
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action_bar_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_account_logout){
            ((HalamanUserActivity) requireActivity()).logout();
        }
        
        return super.onOptionsItemSelected(item);
    }
}
