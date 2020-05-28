package com.dolayindustries.projectkuliah.user.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dolayindustries.projectkuliah.LoginActivity;
import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.service.AppService;
import com.dolayindustries.projectkuliah.user.HalamanUserActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPengajuan extends Fragment {
    private String dataJurusan, dataNamaKampus, dataNimPengaju, dataNamaPengaju, dataTanggalLahirPengaju, dataAlamatRumahPengaju;
    private TextView textViewAjukanSurat, textViewBatalAjukanSurat;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Cursor cursor;
    private Spinner spinnerDataJurusan, spinnerDataNamaKampus;
    private EditText editTextNimPengaju, editTextNamaPengaju, editTextTanggalLahirPengaju, editTextAlamatPengaju;


    public FragmentPengajuan() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengajuan, container, false);

        FloatingActionButton floatingActionButtonBuatPengajuan = view.findViewById(R.id.fab_add_pengajuan);
        floatingActionButtonBuatPengajuan.setOnClickListener(v -> {
            View viewPengajuan = View.inflate(getContext(), R.layout.form_pengajuan_surat, null);
            AlertDialog alertDialogPengajuanSurat = new AlertDialog.Builder(getContext()).create();
            alertDialogPengajuanSurat.setTitle("Pengajuan Surat Pernyataan");

            inisialisasiElement(viewPengajuan);
            /*pengisian data*/
            String dataNim = ((HalamanUserActivity)requireActivity()).getDataNimDariSharedPreferences();

            DataHelper dataHelper = new DataHelper(getContext());
            SQLiteDatabase database = dataHelper.getReadableDatabase();
            cursor = database.rawQuery("SELECT nama FROM tabelakun WHERE username = '" + dataNim + "';", null);
            cursor.moveToFirst();

            editTextNamaPengaju.setText(cursor.getString(0));
            editTextNimPengaju.setText(dataNim);

            /*spinner (dropdown menu) untuk data jurusan*/
            ArrayAdapter<CharSequence> adapterSpinnerJurusan = ArrayAdapter.createFromResource(requireContext(), R.array.dataJurusan, android.R.layout.simple_spinner_item);
            adapterSpinnerJurusan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDataJurusan.setAdapter(adapterSpinnerJurusan);

            spinnerDataJurusan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dataJurusan = spinnerDataJurusan.getSelectedItem().toString();
                    Toast.makeText(getContext(), dataJurusan, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            /*spinner untuk data nama kampus*/
            ArrayAdapter<CharSequence> adapterSpinnerDataKampus = ArrayAdapter.createFromResource(requireContext(), R.array.dataNamaKampus, android.R.layout.simple_spinner_item);
            adapterSpinnerDataKampus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDataNamaKampus.setAdapter(adapterSpinnerDataKampus);

            spinnerDataNamaKampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dataNamaKampus = spinnerDataNamaKampus.getSelectedItem().toString();
                    Toast.makeText(getContext(), dataNamaKampus, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            editTextTanggalLahirPengaju.setOnClickListener(v1 -> {
                calendar = Calendar.getInstance();
                int hari = calendar.get(Calendar.DAY_OF_MONTH);
                int bulan = calendar.get(Calendar.MONTH);
                int tahun = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.YEAR, year);

                    String formatTanggal = "dd MMM yyyy";
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatTanggal);
                    editTextTanggalLahirPengaju.setText(simpleDateFormat.format(calendar.getTime()));
                }, hari, bulan, tahun);

                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() - 946708560000L);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis() - 315569520000L);
                datePickerDialog.show();
            });

            alertDialogPengajuanSurat.setView(viewPengajuan);

            textViewAjukanSurat.setOnClickListener(v1 -> {
                alertDialogPengajuanSurat.dismiss();
                inputDatabase();
                
            });

            textViewBatalAjukanSurat.setOnClickListener(v1 -> alertDialogPengajuanSurat.cancel());
            alertDialogPengajuanSurat.show();
        });
    
        return view;
    }

    private void inisialisasiElement(View view){
        textViewAjukanSurat = view.findViewById(R.id.text_view_ajukan_surat);
        textViewBatalAjukanSurat = view.findViewById(R.id.text_view_batal_ajukan_surat);
        editTextAlamatPengaju = view.findViewById(R.id.data_alamat_pengajuan);
        editTextNamaPengaju = view.findViewById(R.id.data_nama_pengajuan);
        editTextNimPengaju = view.findViewById(R.id.data_nim_pengajuan);
        editTextTanggalLahirPengaju = view.findViewById(R.id.data_tanggal_lahir_pengajuan);
        spinnerDataNamaKampus = view.findViewById(R.id.spinner_nama_kampus);
        spinnerDataJurusan = view.findViewById(R.id.spinner_data_jurusan_pengajuan);
    }

    private void inputDatabase() {
        dataNimPengaju = editTextNimPengaju.getText().toString();
        dataNamaPengaju = editTextNamaPengaju.getText().toString();
        dataTanggalLahirPengaju = editTextTanggalLahirPengaju.getText().toString();
        dataAlamatRumahPengaju = editTextAlamatPengaju.getText().toString();

        DataHelper dataHelper = new DataHelper(getContext());
        SQLiteDatabase database = dataHelper.getWritableDatabase();
        database.execSQL("INSERT INTO tabelpengajuan(username, jurusan, statusapprove, dibaca, namakampus, jenispengajuan, tanggalpengajuan, alamatpengaju, tanggallahir, waktupengajuan) VALUES('" + dataNimPengaju + "', '" + dataJurusan + "', 'tidak', 'terkirim', '" + dataNamaKampus + "', 'Surat Pernyataan', date('now'), '" + dataAlamatRumahPengaju + "', '" + dataTanggalLahirPengaju + "', time('now','localtime'));");

        if (database.isDatabaseIntegrityOk()) {
            SharedPreferences HapusStatusLogin = requireContext().getSharedPreferences("DATA_LOGIN", MODE_PRIVATE);
            SharedPreferences.Editor preferencesEditor = HapusStatusLogin.edit();
            //jika logout diclick, maka data login dihapus
            preferencesEditor.remove("login").apply();

            //dan diganti status dengan tidak login
            preferencesEditor.putString("login", "pindah");
            preferencesEditor.apply();

            Intent intentNotif = new Intent(getContext(), AppService.class);
            requireActivity().startService(intentNotif);
            Toast.makeText(getContext(), "Pengajuan sudah dikirim, harap menunggu untuk disetujui", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Data Tidak Terkirim", Toast.LENGTH_SHORT).show();
        }

    }
}
