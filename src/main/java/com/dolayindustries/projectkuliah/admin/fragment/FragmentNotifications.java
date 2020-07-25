package com.dolayindustries.projectkuliah.admin.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.adapter.AdapterRecyclerViewNotifikasiAdmin;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.model.DataNotifikasiAdmin;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotifications extends Fragment {
    private ArrayList<DataNotifikasiAdmin> dataNotifikasiAdmins = new ArrayList<>();


    public FragmentNotifications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications_admin, container, false);

        RecyclerView recyclerViewNotifikasiAdmin = view.findViewById(R.id.recycler_view_notif_admin);

        TextView textViewNoNotif = view.findViewById(R.id.no_notif);
        if (addDataNotifAdmin().getCount() > 0) {
            textViewNoNotif.setVisibility(View.GONE);
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
        Cursor cursor = database.rawQuery("SELECT * FROM tabelpengajuan WHERE dibaca = 'terkirim' ORDER BY id_pengajuan DESC;", null);
        while (cursor.moveToNext()) {
            dataNotifikasiAdmins.add(new DataNotifikasiAdmin(cursor.getInt(0), getResources().getDrawable(R.drawable.ic_done_all_black_24dp), cursor.getString(8) + " " + cursor.getString(10)));
        }
        return cursor;
    }
}
