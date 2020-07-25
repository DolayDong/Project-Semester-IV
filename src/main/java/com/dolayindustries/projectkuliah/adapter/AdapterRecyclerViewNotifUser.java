package com.dolayindustries.projectkuliah.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.model.DataNotifikasiUser;
import com.dolayindustries.projectkuliah.user.report.ReportActivity;

import java.util.ArrayList;

public class AdapterRecyclerViewNotifUser extends RecyclerView.Adapter<AdapterRecyclerViewNotifUser.MyViewHolder> {
    public AdapterRecyclerViewNotifUser(ArrayList<DataNotifikasiUser> arrayListData, Context context) {
        this.context = context;
        this.arrayListData = arrayListData;
        notifyDataSetChanged();
    }

    private Context context;

    private ArrayList<DataNotifikasiUser> arrayListData;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_design_notifikasi_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.textViewStatusApproveNotif.setText(arrayListData.get(position).getStatusApprove());
        holder.textViewJudulNotif.setText(arrayListData.get(position).getJudulNotif());
        holder.textViewStatusPengiriman.setImageDrawable(arrayListData.get(position).getStatusPengiriman());
        holder.textViewTanggalPengajuan.setText(arrayListData.get(position).getTanggalPengajuan());
        holder.textViewIdPengajuan.setText(String.valueOf(arrayListData.get(position).getIdPengajuan()));


        holder.itemView.setOnClickListener(v -> {
            if (holder.textViewStatusApproveNotif.getText().toString().equalsIgnoreCase("Disetujui")) {
                Intent pindahReport = new Intent(v.getContext(), ReportActivity.class);
                pindahReport.putExtra("id_pengajuan", holder.textViewIdPengajuan.getText().toString());
                v.getContext().startActivity(pindahReport);
                if (holder.getDataStatusDibacaUser(arrayListData.get(position).getIdPengajuan()) == 0) {
                    holder.updateStatusDibacaUser(arrayListData.get(position).getIdPengajuan());

                }
            } else if (holder.textViewStatusApproveNotif.getText().toString().equalsIgnoreCase(" - ")) {
                Toast.makeText(v.getContext(), "Silahkan hubungi admin agar membaca surat pengajuan anda", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "Tidak bisa di tindak lanjuti, karena Pengajuan ini tidak disetujui", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayListData != null) ? arrayListData.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewJudulNotif, textViewStatusApproveNotif, textViewTanggalPengajuan, textViewIdPengajuan;
        private ImageView textViewStatusPengiriman;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIdPengajuan = itemView.findViewById(R.id.t_view_id_pengajuan_user);
            textViewJudulNotif = itemView.findViewById(R.id.judul_notif_user);
            textViewStatusApproveNotif = itemView.findViewById(R.id.status_approve_pengajuan);
            textViewStatusPengiriman = itemView.findViewById(R.id.status_pengiriman_pengajuan);
            textViewTanggalPengajuan = itemView.findViewById(R.id.text_view_tanggal_pengajuan);

        }

        private void updateStatusDibacaUser(int idpengajuan) {
            DataHelper dataHelper = new DataHelper(context);
            SQLiteDatabase database = dataHelper.getWritableDatabase();
            database.execSQL("UPDATE tabelpengajuan SET dibacauser = 1 WHERE id_pengajuan ='" + idpengajuan + "';");
        }

        private int getDataStatusDibacaUser(int idpengajuan) {
            DataHelper dataHelper = new DataHelper(context);
            SQLiteDatabase database = dataHelper.getReadableDatabase();
            @SuppressLint("Recycle")
            Cursor cursor = database.rawQuery("SELECT dibacauser FROM tabelpengajuan WHERE id_pengajuan ='" + idpengajuan + "';", null);
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
    }
}
