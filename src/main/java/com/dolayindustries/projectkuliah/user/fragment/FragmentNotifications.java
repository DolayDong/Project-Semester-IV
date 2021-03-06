package com.dolayindustries.projectkuliah.user.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.adapter.AdapterRecyclerViewNotifUser;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.model.DataNotifikasiUser;
import com.dolayindustries.projectkuliah.user.HalamanUserActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNotifications extends Fragment {
    private ArrayList<DataNotifikasiUser> dataNotificationsTerkirimUser;
    int jumlahNotitikasiUser = 0;


    public FragmentNotifications() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications_user, container, false);

        RecyclerView recyclerViewTerkirim = view.findViewById(R.id.recycler_view_notif_user);

        addDataNotifTerkirim();

        /*set notif terkirim*/
        RecyclerView.LayoutManager layoutManagerTerkirim = new LinearLayoutManager(this.getContext());
        recyclerViewTerkirim.setLayoutManager(layoutManagerTerkirim);
        RecyclerView.Adapter adapterTerkirim = new AdapterRecyclerViewNotifUser(dataNotificationsTerkirimUser, requireActivity().getApplicationContext());
        adapterTerkirim.notifyDataSetChanged();
        recyclerViewTerkirim.setAdapter(adapterTerkirim);
        /*akhir notif terkirim*/

        if (jumlahNotif() == 0) {
            simpanJumlahNotif(jumlahNotitikasiUser);
        }
        return view;
    }

    private void addDataNotifTerkirim() {
        dataNotificationsTerkirimUser = new ArrayList<>();

        //ambil data dari database untuk dimasukkan ke array list

        DataHelper dataHelper = new DataHelper(getContext());
        SQLiteDatabase database = dataHelper.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery("SELECT * FROM tabelpengajuan WHERE username = '" + ((HalamanUserActivity) requireActivity()).getDataNimDariSharedPreferences() + "' ORDER BY id_pengajuan DESC;", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            for (int jumlah = 0; jumlah < cursor.getCount(); jumlah++) {
                cursor.moveToPosition(jumlah);
                if (cursor.getString(5).equals("dibaca")) {
                    if (cursor.getString(4).equals("belum")) {
                        dataNotificationsTerkirimUser.add(new DataNotifikasiUser(cursor.getInt(0), cursor.getString(8) + " " + cursor.getString(10), cursor.getString(7), "Belum Disetujui", getResources().getDrawable(R.drawable.ic_done_all_blue_24dp)));
                    } else if (cursor.getString(4).equals("setuju")) {
                        // notifikasi disetujui
                        jumlahNotitikasiUser++;
                        dataNotificationsTerkirimUser.add(new DataNotifikasiUser(cursor.getInt(0), cursor.getString(8) + " " + cursor.getString(10), cursor.getString(7), "Disetujui", getResources().getDrawable(R.drawable.ic_done_all_blue_24dp)));
                    } else {
                        dataNotificationsTerkirimUser.add(new DataNotifikasiUser(cursor.getInt(0), cursor.getString(8) + " " + cursor.getString(10), cursor.getString(7), "Tidak Disetujui", getResources().getDrawable(R.drawable.ic_done_all_blue_24dp)));
                    }
                } else {
                    dataNotificationsTerkirimUser.add(new DataNotifikasiUser(cursor.getInt(0), cursor.getString(8) + " " + cursor.getString(10), cursor.getString(7), " - ", getResources().getDrawable(R.drawable.ic_done_all_black_24dp)));
                }
            }
        }
    }

    private void simpanJumlahNotif(int jumlah) {
        SharedPreferences jumlahDisetujui = requireActivity().getSharedPreferences(HalamanUserActivity.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = jumlahDisetujui.edit();
        editor.putInt(HalamanUserActivity.KEY, jumlah);
        editor.apply();
    }

    private int jumlahNotif() {
        SharedPreferences jumlahDisetujui = requireActivity().getSharedPreferences(HalamanUserActivity.PREFERENCES, Context.MODE_PRIVATE);
        return jumlahDisetujui.getInt(HalamanUserActivity.KEY, 0);
    }


}
