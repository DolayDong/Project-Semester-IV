package com.dolayindustries.projectkuliah.model;

import android.graphics.drawable.Drawable;

public class DataNotifikasiAdmin {
    public int getIdPengaju() {
        return idPengaju;
    }

    private int idPengaju;

    public Drawable getStatusNotif() {
        return statusNotif;
    }

    public DataNotifikasiAdmin(int idPengaju, Drawable statusNotif, String tanggalDiajukan) {
        this.idPengaju = idPengaju;
        this.statusNotif = statusNotif;
        this.tanggalDiajukan = tanggalDiajukan;
    }

    public String getTanggalDiajukan() {
        return tanggalDiajukan;
    }

    public void setStatusNotif(Drawable statusNotif) {
        this.statusNotif = statusNotif;
    }

    private Drawable statusNotif;
    private String tanggalDiajukan;
}
