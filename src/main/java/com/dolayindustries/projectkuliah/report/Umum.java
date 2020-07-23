package com.dolayindustries.projectkuliah.report;

import android.content.Context;

import com.dolayindustries.projectkuliah.R;

import java.io.File;

public class Umum {
    public static String getAppPath(Context context) {
        File direktori = new File(android.os.Environment.getExternalStorageDirectory()
                + File.separator + context.getResources().getString(R.string.app_name)
                + File.separator);
        if (!direktori.exists())
            direktori.mkdir();
        return direktori.getPath() + File.separator;
    }
}
