package com.dolayindustries.projectkuliah.user.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dolayindustries.projectkuliah.LoginActivity;
import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.user.HalamanUserActivity;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccount extends Fragment{
    private TextView buttonPerbarui, buttonBatalPerbarui;
    private EditText editTextUpdateUsername, editTextUpdateNama, editTextUpdatePasswprdLama, editTextUpdatePasswordBaru, editTextUpdateEmail;
    private TextView textViewUsername, textViewNama, textViewPassword, textViewEmail;
    private CardView cardViewDataAkun;
    private Cursor cursor;
    private String passwordDariDatabase;

    public FragmentAccount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_user, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_eject_black_24dp));

        inisialisasiElmenet(view);
        /* awal ambil data akun dari database */
        String dataUsername = ((HalamanUserActivity) requireActivity()).getDataNimDariSharedPreferences();
        DataHelper dataHelper = new DataHelper(getContext());
        SQLiteDatabase database = dataHelper.getReadableDatabase();
        cursor = database.rawQuery("SELECT * FROM tabelakun WHERE username ='" + dataUsername + "'", null);
        cursor.moveToFirst();

            for (int jumlah = 0; jumlah < cursor.getCount(); jumlah++){
                cursor.moveToPosition(jumlah);
                textViewUsername.setText(cursor.getString(0));
                textViewNama.setText(cursor.getString(1));
                textViewEmail.setText(cursor.getString(2));
                textViewPassword.setText(cursor.getString(4));
            }
            /* akhir ambil data akun*/

        passwordDariDatabase = cursor.getString(4);

        cardViewDataAkun.setOnClickListener(v ->{
            CharSequence[] pilihan = {"Update Data", "Hapus Akun"};
            AlertDialog.Builder alertPilihan = new AlertDialog.Builder(requireContext());
            alertPilihan.setTitle("Pilihan");

            alertPilihan.setItems(pilihan, (dialog, which) -> {
                switch (which){
                    case 0:
                        View viewUpdate = View.inflate(getContext(), R.layout.form_update_data_akun, null);
                        
                        AlertDialog alertUpdate = new AlertDialog.Builder(requireContext()).create();
                        alertUpdate.setTitle("Update Data Profil");
                        inisialisasiElmenet(viewUpdate);

                        cursor.moveToFirst();
                        if (cursor.getCount() > 0){
                            cursor.moveToPosition(0);
                            editTextUpdateUsername.setText(cursor.getString(0));
                            editTextUpdateNama.setText(cursor.getString(1));
                            editTextUpdateEmail.setText(cursor.getString(2));
                        }
                        alertUpdate.setView(viewUpdate);

                        buttonPerbarui.setOnClickListener(v1 -> {
                            if (validDataUpdate()){
                                SQLiteDatabase ubahData = dataHelper.getWritableDatabase();
                                ubahData.execSQL("UPDATE tabelakun SET email = '" + editTextUpdateEmail.getText().toString() + "', password = '" + editTextUpdatePasswordBaru.getText().toString() + "' WHERE username = '" + dataUsername + "';");

                                Toast.makeText(getContext(), "Berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                alertUpdate.dismiss();
                            } else {
                                Toast.makeText(getContext(), "Gagal diperbarui", Toast.LENGTH_SHORT).show();
                          }
                        });
                        buttonBatalPerbarui.setOnClickListener(v1 -> {
                            alertUpdate.cancel();
                        });
                        alertUpdate.show();
                        break;
                    case 1:
                        AlertDialog.Builder alertHapusAkun = new AlertDialog.Builder(requireContext());
                        alertHapusAkun.setTitle("Perhatian!");
                        alertHapusAkun.setMessage("Setelah anda menghapus akun, sistem akan menghapus semua data anda. perlu di ketahui bahwa tindakan ini tidak bisa di batalkan. anda harus registrasi ulang jika ingin memiliki akun lagi. lanjutkan hapus akun?");

                        alertHapusAkun.setPositiveButton("Lanjutkan", (dialog1, which1) -> {
                            SQLiteDatabase hapusData = dataHelper.getWritableDatabase();
                            hapusData.execSQL("DELETE FROM tabelakun WHERE username = '" + dataUsername + "'");

                            //ambil data dari shared preferences login untuk di hapus
                            SharedPreferences sharedPreferencesHapusData = requireContext().getSharedPreferences("DATA_LOGIN", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorSharedPreferences = sharedPreferencesHapusData.edit();
                            editorSharedPreferences.clear().apply();

                            //setelah bersih lempar ke halaman Login
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);

                            //tutup activity ini
                            requireActivity().finish();
                            Toast.makeText(getContext(), "Akun Dihapus", Toast.LENGTH_SHORT).show();
                        }).setNegativeButton("Batal", (dialog1, which1) -> {
                            Toast.makeText(getContext(), "Batal Hapus", Toast.LENGTH_SHORT).show();
                        });
                        alertHapusAkun.create().show();
                        break;
                }
            });
            alertPilihan.create().show();
        });


        
        
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_action_bar_account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_account_logout){
            ((HalamanUserActivity) requireActivity()).logout();
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void inisialisasiElmenet(View view){
        buttonBatalPerbarui = view.findViewById(R.id.button_batal_perbarui_data);
        buttonPerbarui = view.findViewById(R.id.button_perbarui_data);
        textViewEmail = view.findViewById(R.id.text_view_list_email);
        textViewNama = view.findViewById(R.id.text_view_list_nama);
        textViewUsername = view.findViewById(R.id.text_view_list_nim);
        textViewPassword = view.findViewById(R.id.text_view_list_password_akun);
        cardViewDataAkun = view.findViewById(R.id.card_view_data_account_user);
        editTextUpdateEmail = view.findViewById(R.id.e_text_update_email);
        editTextUpdateNama = view.findViewById(R.id.e_text_update_nama);
        editTextUpdateUsername = view.findViewById(R.id.e_text_update_nim);
        editTextUpdatePasswordBaru = view.findViewById(R.id.e_text_konfirmasi_update_password_baru);
        editTextUpdatePasswprdLama = view.findViewById(R.id.e_text_update_sandi_lama);
    }

    private boolean validDataUpdate(){
        boolean valid = true;
        if (editTextUpdateEmail.getText().toString().isEmpty()){
            editTextUpdateEmail.setError("field tidak boleh kosong");
            valid = false;
        } else if(!editTextUpdateEmail.getText().toString().contains("@")){
            editTextUpdateEmail.setError("field harus mengandung karakter email");
            valid = false;
        }

        if (editTextUpdatePasswprdLama.getText().toString().isEmpty()){
            editTextUpdatePasswprdLama.setError("field tidak boleh kosong");
            valid = false;
        } else if (!editTextUpdatePasswprdLama.getText().toString().equals(passwordDariDatabase) && !editTextUpdatePasswprdLama.getText().toString().isEmpty()){
            editTextUpdatePasswprdLama.setError("password lama tidak sesuai");
            valid = false;
        }

        if (editTextUpdatePasswordBaru.getText().length() < 8 && !editTextUpdatePasswordBaru.getText().toString().isEmpty()){
            editTextUpdatePasswordBaru.setError("password harus lebih dari 8 karakter");
            valid = false;
        } else if(editTextUpdatePasswordBaru.getText().toString().isEmpty()){
            editTextUpdatePasswordBaru.setError("field tidak boleh kosong");
            valid = false;
        } else if(!editTextUpdatePasswordBaru.getText().toString().isEmpty() && !editTextUpdatePasswprdLama.getText().toString().equals(passwordDariDatabase)){
            valid = false;
        }

        return valid;
    }

}
