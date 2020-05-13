package com.dolayindustries.projectkuliah.user.fragment;

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
    private ArrayList<DataNotifikasiUser> dataNotificationsDisetujuiUser;

    public FragmentNotifications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications_user, container, false);

        RecyclerView recyclerViewTerkirim = view.findViewById(R.id.recycler_view_notif_user);
        RecyclerView recyclerViewDisetujui = view.findViewById(R.id.recycler_view_notif_user_disetujui);

        addDataNotifTerkirim();

        /*set notif terkirim*/
        RecyclerView.LayoutManager layoutManagerTerkirim = new LinearLayoutManager(this.getContext());
        recyclerViewTerkirim.setLayoutManager(layoutManagerTerkirim);
        RecyclerView.Adapter adapterTerkirim = new AdapterRecyclerViewNotifUser(dataNotificationsTerkirimUser);
        adapterTerkirim.notifyDataSetChanged();
        recyclerViewTerkirim.setAdapter(adapterTerkirim);
        /*akhir notif terkirim*/

        /*set notif disetujui*/
        RecyclerView.LayoutManager layoutManagerDisetujui = new LinearLayoutManager(this.getContext());
        recyclerViewDisetujui.setLayoutManager(layoutManagerDisetujui);
        RecyclerView.Adapter adapterDisetujui = new AdapterRecyclerViewNotifUser(dataNotificationsDisetujuiUser);
        adapterDisetujui.notifyDataSetChanged();
        recyclerViewDisetujui.setAdapter(adapterDisetujui);
        /*akhir notif disetujui*/

        return view;
    }

    private void addDataNotifTerkirim() {
        dataNotificationsTerkirimUser = new ArrayList<>();
        dataNotificationsDisetujuiUser = new ArrayList<>();

        //ambil data dari database untuk dimasukkan ke array list

        DataHelper dataHelper = new DataHelper(getContext());
        SQLiteDatabase database = dataHelper.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery("SELECT * FROM tabelpengajuan WHERE username = '" + ((HalamanUserActivity) requireActivity()).getDataNimDariSharedPreferences() + "' ORDER BY tanggalpengajuan ASC;", null);
        cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            for (int jumlah = 0; jumlah < cursor.getCount(); jumlah++) {
                cursor.moveToPosition(jumlah);
                if (cursor.getString(5).equals("dibaca")) {
                    if (cursor.getString(4).equals("belum")) {
                        dataNotificationsTerkirimUser.add(new DataNotifikasiUser(cursor.getString(8) + " " + cursor.getString(10), cursor.getString(7), "Belum Disetujui", getResources().getDrawable(R.drawable.ic_done_all_blue_24dp)));
                    }else if(cursor.getString(4).equals("ya")){
                        dataNotificationsDisetujuiUser.add(new DataNotifikasiUser(cursor.getString(8) + " " + cursor.getString(10), cursor.getString(7), "Disetujui", getResources().getDrawable(R.drawable.ic_done_all_blue_24dp)));
                    } else {
                        dataNotificationsTerkirimUser.add(new DataNotifikasiUser(cursor.getString(8) + " " + cursor.getString(10), cursor.getString(7), "Tidak Disetujui", getResources().getDrawable(R.drawable.ic_done_all_blue_24dp)));
                    }
                } else{
                    dataNotificationsTerkirimUser.add(new DataNotifikasiUser(cursor.getString(8) + " " + cursor.getString(10), cursor.getString(7), " - ", getResources().getDrawable(R.drawable.ic_done_all_black_24dp)));
                }
            }
        }
    }
}
