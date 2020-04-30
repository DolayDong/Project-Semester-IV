package com.dolayindustries.projectkuliah.user.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dolayindustries.projectkuliah.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPengajuan extends Fragment {

    public FragmentPengajuan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengajuan, container, false);

        FloatingActionButton floatingActionButtonBuatPengajuan = view.findViewById(R.id.fab_add_pengajuan);
        floatingActionButtonBuatPengajuan.setOnClickListener(v -> Toast.makeText(getContext(), "Lanjut Tampil Form Pengajuan", Toast.LENGTH_SHORT).show());
    
    
        return view;
    }
}
