package com.dolayindustries.projectkuliah.user.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dolayindustries.projectkuliah.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccount extends Fragment {

    public FragmentAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_user, container, false);
    }
}
