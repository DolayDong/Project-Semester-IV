package com.dolayindustries.projectkuliah.report;

import android.content.Context;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;


public class PdfDocumentAdapter extends PrintDocumentAdapter {
    //lokasi file tujuan di adnroid
    private String path;

    //constructor untuk dipanggil saat akan membuat document pdf
    public PdfDocumentAdapter(Context context, String path) {
        this.path = path;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        //jika dicancel/tidak diprint document pdfnya
        if (cancellationSignal.isCanceled())
            //maka beritahu layoutnya
            callback.onLayoutCancelled();
        else {
            //jika tidak di cancel/berarti document disave. maka buat document dengan property seperti dibawah
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("Pengajuan Surat");
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN).build();
            callback.onLayoutFinished(builder.build(), !newAttributes.equals(oldAttributes));
        }
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        //input stream untuk mengambil document contoh yang telah dibuat di main activity pada saat aplikasi diinstall
        InputStream inputStream = null;

        //output untuk menghasilkan output document saat dibuat
        OutputStream outputStream = null;
        try {
            //class file dari java yang berisi path yang telah di deklarasikan di awal class. yaitu String lokasi file
            File file = new File(path);

            //masukan lokasi kedalam input
            inputStream = new FileInputStream(file);

            //keluarkan di parameter destination
            outputStream = new FileOutputStream(destination.getFileDescriptor());

            // menangani buffer
            byte[] buffer = new byte[163840];
            int size;

            //selama inputstream membaca buffer(jika ada) dan tidak dicancel/berarti disimpan
            while ((size = inputStream.read(buffer)) >= 0 && !cancellationSignal.isCanceled()) {
                //keluarkan output
                outputStream.write(buffer, 0, size);
            }

            //jika di cancel
            if (cancellationSignal.isCanceled())
                // gajadi di print
                callback.onWriteCancelled();
            else {
                // keluarkan semua halaman
                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }
        } catch (Exception e) {
            callback.onWriteFailed(e.getMessage());
            Log.e("Errorrrrr PdfAdapter", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        } finally {
            try {
                //jika tidak kosong, tutup inputan
                assert inputStream != null;
                inputStream.close();

                //dan tutup outputan
                assert outputStream != null;
                outputStream.close();
            } catch (IOException e) {
                Log.e("Errorrr Finally", "" + e.getMessage());
            }
        }
    }
}
