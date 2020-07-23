package com.dolayindustries.projectkuliah.user.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.service.AppService;
import com.dolayindustries.projectkuliah.user.HalamanUserActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPengajuan extends Fragment {
    private String dataJurusan;
    private String dataTempatLahir;
    private String dataNamaKampus;
    private String tanggalPengajuan;
    private String waktuPengajuan;
    private TextView textViewAjukanSurat, textViewBatalAjukanSurat;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private Cursor cursor;
    private Spinner spinnerDataJurusan, spinnerDataNamaKampus, spinnerTempatLahir;
    private EditText editTextNimPengaju, editTextNamaPengaju, editTextTanggalLahirPengaju, editTextAlamatPengaju, editTextNamaOrangTua, editTextAlamatOrangTua, editTextPekerjaanOrangTua;


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
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            /*spinner untuk data tempat lahir*/
            ArrayAdapter<CharSequence> adapterTempatLahir = ArrayAdapter.createFromResource(requireContext(), R.array.kabupaten, android.R.layout.simple_spinner_item);
            adapterSpinnerDataKampus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerTempatLahir.setAdapter(adapterTempatLahir);

            spinnerTempatLahir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    dataTempatLahir = spinnerTempatLahir.getSelectedItem().toString();
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
            alertDialogPengajuanSurat.setCancelable(false);

            textViewAjukanSurat.setOnClickListener(v1 -> {
                if (validasiInput()) {
                    alertDialogPengajuanSurat.dismiss();
                    inputDatabase();
                }


            });

            textViewBatalAjukanSurat.setOnClickListener(v1 -> alertDialogPengajuanSurat.cancel());
            alertDialogPengajuanSurat.show();
        });

        return view;
    }

    private void inisialisasiElement(View view) {
        editTextNamaOrangTua = view.findViewById(R.id.data_nama_ortu_pengaju);
        editTextPekerjaanOrangTua = view.findViewById(R.id.data_pekerjaan_ortu_pengaju);
        editTextAlamatOrangTua = view.findViewById(R.id.data_alamat_ortu_pengajuan);
        textViewAjukanSurat = view.findViewById(R.id.text_view_ajukan_surat);
        textViewBatalAjukanSurat = view.findViewById(R.id.text_view_batal_ajukan_surat);
        editTextAlamatPengaju = view.findViewById(R.id.data_alamat_pengajuan);
        editTextNamaPengaju = view.findViewById(R.id.data_nama_pengajuan);
        editTextNimPengaju = view.findViewById(R.id.data_nim_pengajuan);
        editTextTanggalLahirPengaju = view.findViewById(R.id.data_tanggal_lahir_pengajuan);
        spinnerDataNamaKampus = view.findViewById(R.id.spinner_nama_kampus);
        spinnerDataJurusan = view.findViewById(R.id.spinner_data_jurusan_pengajuan);
        spinnerTempatLahir = view.findViewById(R.id.spinner_tempat_lahir);
    }

    private void inputDatabase() {
        String dataNimPengaju = editTextNimPengaju.getText().toString();
        String dataTanggalLahirPengaju = editTextTanggalLahirPengaju.getText().toString();
        String dataAlamatRumahPengaju = editTextAlamatPengaju.getText().toString();
        String dataNamaOrangTua = editTextNamaOrangTua.getText().toString();
        String dataPekerjaanOrangTua = editTextPekerjaanOrangTua.getText().toString();
        String dataAlamatOrangTua = editTextAlamatOrangTua.getText().toString();
        DataHelper dataHelper = new DataHelper(getContext());
        SQLiteDatabase database = dataHelper.getWritableDatabase();
        database.execSQL("INSERT INTO tabelpengajuan(username, jurusan, statusapprove, dibaca, namakampus, jenispengajuan, tanggalpengajuan, alamatpengaju, tanggallahir, waktupengajuan, tempatlahir, namaorangtua, pekerjaanorangtua, alamatorangtua, tanggalapprove, idadmin) VALUES('" + dataNimPengaju + "', '" + dataJurusan + "', 'tidak', 'terkirim', '" + dataNamaKampus + "', 'Surat Pernyataan', date('now'), '" + dataAlamatRumahPengaju + "', '" + dataTanggalLahirPengaju + "', time('now', 'localtime'), '" + dataTempatLahir + "', '" + dataNamaOrangTua + "', '" + dataPekerjaanOrangTua + "', '" + dataAlamatOrangTua + "', null, null);");

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

    private boolean validasiInput() {
        boolean valid = true;
        if (editTextAlamatPengaju.getText().toString().isEmpty()) {
            editTextAlamatPengaju.setError("harap isi alamat anda");
            editTextAlamatPengaju.requestFocus();
            valid = false;
        } else if (editTextAlamatPengaju.getText().length() < 5) {
            editTextAlamatPengaju.setError("kayanya alamatnya terlalu pendek deh");
            editTextAlamatPengaju.requestFocus();
            valid = false;
        }

        if (editTextTanggalLahirPengaju.getText().toString().isEmpty()) {
            editTextTanggalLahirPengaju.setError("harap isi tanggal lahir anda");
            editTextTanggalLahirPengaju.requestFocus();
            valid = false;
        }
        if (editTextNamaOrangTua.getText().toString().isEmpty()) {
            editTextNamaOrangTua.setError("isi field nama orang tua / wali");
            editTextNamaOrangTua.requestFocus();
            valid = false;
        }
        if (editTextAlamatOrangTua.getText().toString().isEmpty()) {
            editTextAlamatOrangTua.setError("isi field ini");
            editTextAlamatOrangTua.requestFocus();
            valid = false;
        }
        if (editTextPekerjaanOrangTua.getText().toString().isEmpty()) {
            editTextPekerjaanOrangTua.setError("field ini tidak boleh kosong");
            editTextPekerjaanOrangTua.requestFocus();
            valid = false;
        }

        return valid;
    }
}
