package com.dolayindustries.projectkuliah.model;

import android.graphics.drawable.Drawable;

public class DataNotifikasiUser {

    public String getTanggalPengajuan() {
        return tanggalPengajuan;
    }

    private String tanggalPengajuan;

    private String judulNotif;

    public String getJudulNotif() {
        return judulNotif;
    }

    public void setJudulNotif(String judulNotif) {
        this.judulNotif = judulNotif;
    }

    public String getStatusApprove() {
        return statusApprove;
    }

    public void setStatusApprove(String statusApprove) {
        this.statusApprove = statusApprove;
    }

    public Drawable getStatusPengiriman() {
        return statusPengiriman;
    }



    public DataNotifikasiUser(String tanggalPengajuan, String judulNotif, String statusApprove, Drawable statusPengiriman) {
        this.tanggalPengajuan = tanggalPengajuan;
        this.judulNotif = judulNotif;
        this.statusApprove = statusApprove;
        this.statusPengiriman = statusPengiriman;
    }

    private String statusApprove;

    private Drawable statusPengiriman;
}
