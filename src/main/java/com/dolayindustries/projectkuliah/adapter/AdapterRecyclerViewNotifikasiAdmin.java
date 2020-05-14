package com.dolayindustries.projectkuliah.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.admin.notifikasi.DetailNotifikasi;
import com.dolayindustries.projectkuliah.model.DataNotifikasiAdmin;

import java.util.ArrayList;

public class AdapterRecyclerViewNotifikasiAdmin extends RecyclerView.Adapter<AdapterRecyclerViewNotifikasiAdmin.MyViewHolder> {

    public AdapterRecyclerViewNotifikasiAdmin(ArrayList<DataNotifikasiAdmin> dataNotificationAdmins) {
        this.dataNotificationAdmins = dataNotificationAdmins;
    }

    private ArrayList<DataNotifikasiAdmin> dataNotificationAdmins;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_design_notifikasi_admin, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.textViewStatusAdmin.setImageDrawable(dataNotificationAdmins.get(position).getStatusNotif());
        holder.textViewTanggalDikirim.setText(dataNotificationAdmins.get(position).getTanggalDiajukan());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), String.valueOf(dataNotificationAdmins.get(position).getStatusNotif()), Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return (dataNotificationAdmins != null) ? dataNotificationAdmins.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNotifAdmin, textViewTinjauAdmin, textViewTanggalDikirim;
        private ImageView textViewStatusAdmin;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNotifAdmin = itemView.findViewById(R.id.text_view_notif_admin);
            textViewTinjauAdmin = itemView.findViewById(R.id.text_view_tinjau_admin);
            textViewTanggalDikirim = itemView.findViewById(R.id.text_view_tanggal_pengajuan_admin);
            textViewStatusAdmin = itemView.findViewById(R.id.status_peninjauan_admin);
        }
    }


}
