package com.dolayindustries.projectkuliah.admin.fragment;

import android.os.Bundle;

import com.dolayindustries.projectkuliah.admin.HalamanAdminActivity;
import com.dolayindustries.projectkuliah.user.HalamanUserActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dolayindustries.projectkuliah.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAccount extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAccount.
     */
    // TODO: Rename and change types and number of parameters
    private static FragmentAccount newInstance(String param1, String param2) {
        FragmentAccount fragment = new FragmentAccount();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_admin, container, false);
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
            ((HalamanAdminActivity)requireActivity()).logout();
        }
        return super.onOptionsItemSelected(item);
    }
}
