package com.dolayindustries.projectkuliah.admin.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.adapter.AdapterRecyclerViewNotifikasiAdmin;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.model.DataNotifikasiAdmin;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotifications extends Fragment {

    public FragmentNotifications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications_admin, container, false);

        RecyclerView recyclerViewNotifikasiAdmin = view.findViewById(R.id.recycler_view_notif_admin);
        ArrayList<DataNotifikasiAdmin> dataNotifikasiAdmins = new ArrayList<>();

        TextView textViewNoNotif = view.findViewById(R.id.no_notif);
        if (addDataNotifAdmin().getCount() > 0) {
            textViewNoNotif.setVisibility(View.GONE);

            dataNotifikasiAdmins.add(new DataNotifikasiAdmin(getResources().getDrawable(R.drawable.ic_done_all_black_24dp), addDataNotifAdmin().getString(8) + " " + addDataNotifAdmin().getString(10)));
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerViewNotifikasiAdmin.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new AdapterRecyclerViewNotifikasiAdmin(dataNotifikasiAdmins);
        recyclerViewNotifikasiAdmin.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        return view;
    }

    private Cursor addDataNotifAdmin() {
        DataHelper dataHelper = new DataHelper(getContext());
        SQLiteDatabase database = dataHelper.getReadableDatabase();

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery("SELECT * FROM tabelpengajuan WHERE dibaca = 'terkirim';", null);
        cursor.moveToFirst();

        return cursor;
    }
}
