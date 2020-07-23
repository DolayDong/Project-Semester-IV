package com.dolayindustries.projectkuliah.model;

import android.graphics.drawable.Drawable;

public class DataNotifikasiUser {
    public int getIdPengajuan() {
        return idPengajuan;
    }

    private int idPengajuan;

    public String getTanggalPengajuan() {
        return tanggalPengajuan;
    }

    private String tanggalPengajuan;

    private String judulNotif;

    public String getJudulNotif() {
        return judulNotif;
    }

    public String getStatusApprove() {
        return statusApprove;
    }

    public Drawable getStatusPengiriman() {
        return statusPengiriman;
    }


    public DataNotifikasiUser(int idPengajuan, String tanggalPengajuan, String judulNotif, String statusApprove, Drawable statusPengiriman) {
        this.idPengajuan = idPengajuan;
        this.tanggalPengajuan = tanggalPengajuan;
        this.judulNotif = judulNotif;
        this.statusApprove = statusApprove;
        this.statusPengiriman = statusPengiriman;
    }

    private String statusApprove;

    private Drawable statusPengiriman;
}
