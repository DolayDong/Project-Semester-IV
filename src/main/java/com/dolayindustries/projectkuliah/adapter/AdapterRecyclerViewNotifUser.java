package com.dolayindustries.projectkuliah.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.model.DataNotifikasiUser;

import java.util.ArrayList;

public class AdapterRecyclerViewNotifUser extends RecyclerView.Adapter<AdapterRecyclerViewNotifUser.MyViewHolder> {

    public AdapterRecyclerViewNotifUser(ArrayList<DataNotifikasiUser> arrayListData) {
        this.arrayListData = arrayListData;
        notifyDataSetChanged();
    }

    private ArrayList<DataNotifikasiUser> arrayListData;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_design_notifikasi_user, parent, false);


        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.textViewStatusApproveNotif.setText(arrayListData.get(position).getStatusApprove());
        holder.textViewJudulNotif.setText(arrayListData.get(position).getJudulNotif());
        holder.textViewStatusPengiriman.setImageDrawable(arrayListData.get(position).getStatusPengiriman());
        holder.textViewTanggalPengajuan.setText(arrayListData.get(position).getTanggalPengajuan());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayListData != null) ? arrayListData.size() : 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewJudulNotif, textViewStatusApproveNotif, textViewTanggalPengajuan;
        private ImageView textViewStatusPengiriman;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewJudulNotif = itemView.findViewById(R.id.judul_notif_user);
            textViewStatusApproveNotif = itemView.findViewById(R.id.status_approve_pengajuan);
            textViewStatusPengiriman = itemView.findViewById(R.id.status_pengiriman_pengajuan);
            textViewTanggalPengajuan = itemView.findViewById(R.id.text_view_tanggal_pengajuan);
        }
    }
}
