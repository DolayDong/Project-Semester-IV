package com.dolayindustries.projectkuliah.model;

import android.graphics.drawable.Drawable;

public class DataNotifikasiAdmin {
    public Drawable getStatusNotif() {
        return statusNotif;
    }

    public DataNotifikasiAdmin(Drawable statusNotif, String tanggalDiajukan) {
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
