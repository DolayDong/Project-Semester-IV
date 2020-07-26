package com.dolayindustries.projectkuliah.user.report;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dolayindustries.projectkuliah.R;
import com.dolayindustries.projectkuliah.database.DataHelper;
import com.dolayindustries.projectkuliah.report.PdfDocumentAdapter;
import com.dolayindustries.projectkuliah.report.Umum;
import com.dolayindustries.projectkuliah.user.fragment.FragmentNotifications;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class ReportActivity extends AppCompatActivity {
    private Button buttonPrintPdf, buttonKembali;
    private String idPengajuan;
    private TextView textViewNama, textViewDisetujuiOleh, textViewNim, textViewAlamat, textViewTtl, textViewJurusan, textViewNamaOrangTua, textViewPekerjaaOrangTua, textViewAlamatOrangTua, textViewTanggalDiSetujui;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        idPengajuan = getIntent().getStringExtra("id_pengajuan");
        inisialisasi();

        Dexter.withContext(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                DataHelper dataHelper = new DataHelper(getApplicationContext());
                SQLiteDatabase database = dataHelper.getReadableDatabase();
                @SuppressLint("Recycle")
                Cursor cursor = database.rawQuery("SELECT tabelakun.nama, tabelpengajuan.id_pengajuan, tabelpengajuan.username, tabelpengajuan.alamatpengaju, tabelpengajuan.tanggallahir, tabelpengajuan.jurusan, tabelpengajuan.tempatlahir, tabelpengajuan.namaorangtua, tabelpengajuan.pekerjaanorangtua, tabelpengajuan.alamatorangtua, tabelpengajuan.tanggalapprove, tabelpengajuan.idadmin FROM tabelakun JOIN tabelpengajuan ON tabelpengajuan.username = tabelakun.username WHERE id_pengajuan ='" + idPengajuan + "';", null);
                cursor.moveToFirst();

                if (cursor.getCount() > 0) {
                    String tempat = cursor.getString(6) + ", " + cursor.getString(4);
                    textViewNama.setText(cursor.getString(0));
                    textViewNim.setText(cursor.getString(2));
                    textViewAlamat.setText(cursor.getString(3));
                    textViewTtl.setText(tempat);
                    textViewJurusan.setText(cursor.getString(5));
                    textViewNamaOrangTua.setText(cursor.getString(7));
                    textViewPekerjaaOrangTua.setText(cursor.getString(8));
                    textViewAlamatOrangTua.setText(cursor.getString(9));
                    textViewTanggalDiSetujui.setText(bulanIndo(cursor.getString(10)));
                    textViewDisetujuiOleh.setText(cursor.getString(11));
                }
                buttonKembali.setOnClickListener(v -> {
                    Intent intentKembali = new Intent(ReportActivity.this, FragmentNotifications.class);
                    startActivity(intentKembali);
                });
                buttonPrintPdf.setOnClickListener(v -> {
                    buatFilePdf(Umum.getAppPath(ReportActivity.this) + "SURAT_PERNYATAAN.pdf");
                });

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();
    }

    private void buatFilePdf(String path) {
        if (new File(path).exists())
            new File(path).delete();
        try {
            Document document = new Document();
            // buat document pdf
            PdfWriter.getInstance(document, new FileOutputStream(path));
            // buka document
            document.open();

            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Dolay Industries");
            document.addCreator("Sapto Aji");

            //Setting font
            float valueUkuranFont = 13.0f;

            //custom font
            BaseFont namaFont = BaseFont.createFont("assets/fonts/times_new_roman.ttf", "UTF-8", BaseFont.EMBEDDED);

            //header
            tambahHeader(document);

            tambahGarisSeparator(document);
            //buat judul document
            Font fontJudul = new Font(namaFont, 18.0f, Font.UNDERLINE, BaseColor.BLACK);

            //method baru 1
            tambahItemBaru(document, fontJudul);

            tambahLineSpasi(document);
            //tambah element
            tambahNomorSurat(document, new Font(Font.FontFamily.TIMES_ROMAN, 9.0f, Font.UNDERLINE, BaseColor.BLACK));

            new Font(namaFont, valueUkuranFont, Font.NORMAL, BaseColor.BLACK);
            PdfPTable pdfPTablettd = new PdfPTable(1);

            //add cell untuk table diatas yang diisi dengan image yang telah di kompres
            PdfPCell pdfPCellTtd = new PdfPCell(buatImage(30, "image/ttd.png"));

            // method 4
            // nama pengaju
//                tambahDataRataKiriTengah(document, "Nama    : ",  fontValueNamaMahasiswa, fontValueNamaMahasiswa);
//                tambahDataRataKiriTengah(document, "Nim     : ", fontValueNamaMahasiswa, fontValueNamaMahasiswa);
//                tambahDataRataKiriTengah(document, "Jurusan : ",  fontValueNamaMahasiswa, fontValueNamaMahasiswa);
//                tambahDataRataKiriTengah(document, "Perihal : ",  fontValueNamaMahasiswa, fontValueNamaMahasiswa);
            String html = "<html><body class=\"body\">" +
                    "    <br><br>" +
                    "    <p>Kepala kampus Politeknik Jakarta kampus Sudirman Tangerang dengan ini menerangkan bahwa: </p><br>" +
                    "    <table>" +
                    "        <tr>" +
                    "            <th><strong>Nama</strong></th>" +
                    "            <td>: " + textViewNama.getText().toString() + "</td>" +
                    "            <td></td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th><strong>Nim</strong></th>" +
                    "            <td>: " + textViewNim.getText().toString() + "</td>" +
                    "            <td></td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th><strong>Tempat, Tanggal Lahir</strong></th>" +
                    "            <td>: " + textViewTtl.getText().toString() + "</td>" +
                    "            <td></td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th><strong>Alamat</strong></th>" +
                    "            <td>: " + textViewAlamat.getText().toString() + "</td>" +
                    "            <td></td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th> &nbsp; </th>" +
                    "            <td> &nbsp; </td>" +
                    "            <td> &nbsp; </td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th><strong>Nama Orang Tua/Wali</strong></th>" +
                    "            <td>: " + textViewNamaOrangTua.getText().toString() + "</td>" +
                    "            <td></td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th><strong>Pekerjaan Orang Tua/Wali</strong></th>" +
                    "            <td>: " + textViewPekerjaaOrangTua.getText().toString() + "</td>" +
                    "            <td></td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <th><strong>Alamat</strong></th>" +
                    "            <td>: " + textViewAlamatOrangTua.getText().toString() + "</td>" +
                    "            <td></td>" +
                    "        </tr>" +
                    "    </table>" +
                    " <br><br>" +
                    "    <p>Adalah benar Mahasiswa Politeknik Jakarta kampus Sudirman-Tangerang dan tercatat masih aktif sebagai Mahasiswa" +
                    "        Jurusan Informatika Komputer Tahun Akademik " + tahunAjaran(textViewNim.getText().toString()) + "</p>\n" +
                    "    <p>Demikian Surat Keterangan ini diberikan dengan sebenar-benarnya kepada yang bersangkutan, untuk dipergunakan" +
                    "        sebagaimana mestinya</p><br><br>" +
                    " <table>" +
                    "        <tr>" +
                    "            <td>&nbsp;</td>" +
                    "            <td>" + textViewTanggalDiSetujui.getText().toString() + ",</td>" +
                    "        </tr>" +
                    "        <tr>" +
                    "            <td>&nbsp;</td>" +
                    "            <th><strong>Kepala Kampus Sudirman-Tangerang</strong></th>" +
                    "        </tr>" +
                    "    </table>" +
                    "</body></html>";

            HTMLWorker htmlWorker = new HTMLWorker(document);

            htmlWorker.parse(new StringReader(html));
            pdfPCellTtd.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pdfPCellTtd.setBorder(Rectangle.NO_BORDER);
            pdfPCellTtd.setPaddingTop(10.0F);
            pdfPCellTtd.setPaddingBottom(10.0F);
            pdfPCellTtd.setPaddingRight(30.0F);
            pdfPTablettd.addCell(pdfPCellTtd);
            document.add(pdfPTablettd);

            textKepalKampus(document, new Font(Font.FontFamily.TIMES_ROMAN, 13.0F, Font.BOLD, BaseColor.BLACK));


            //tutup document
            document.close();

            // jalankan method cetak untuk mengeprint document
            cetakPdf();
        } catch (IOException | DocumentException nf) {
            nf.printStackTrace();
        }

    }


    private void cetakPdf() {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        try {
            // instansiasi class untuk adapter pdf document
            PrintDocumentAdapter adapter = new PdfDocumentAdapter(ReportActivity.this, Umum.getAppPath(ReportActivity.this) + "SURAT_PERNYATAAN.pdf");
            assert printManager != null;
            printManager.print("Dcument", adapter, new PrintAttributes.Builder().build());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void inisialisasi() {
        buttonKembali = findViewById(R.id.btn_kembali);
        buttonPrintPdf = findViewById(R.id.btn_print_pdf);
        textViewAlamat = findViewById(R.id.tv_value_alamat_pengaju_user);
        textViewJurusan = findViewById(R.id.tv_value_jurusan_pengaju_user);
        textViewNama = findViewById(R.id.tv_value_nama_pengaju_user);
        textViewNim = findViewById(R.id.tv_value_nim_pengaju_user);
        textViewTtl = findViewById(R.id.tv_value_ttl_pengaju_user);
        textViewNamaOrangTua = findViewById(R.id.tv_value_nama_orang_tua_user);
        textViewPekerjaaOrangTua = findViewById(R.id.tv_value_pekerjaan_orang_tua_user);
        textViewAlamatOrangTua = findViewById(R.id.tv_value_alamat_orang_tua_user);
        textViewTanggalDiSetujui = findViewById(R.id.tanggal_approve);
        textViewDisetujuiOleh = findViewById(R.id.tv_value_disetujui_oleh);
    }

    private void textKepalKampus(Document document, Font font) throws DocumentException {
        // pelajari chunk lebih lanjut untuk menangani document pdf itext
        Chunk chunkText = new Chunk("Andri lrawan, S.Pd, MM", font);
        PdfPTable pdfPTableNama = new PdfPTable(1);

        // instansiasi class paragraph dari itextpdf
        Paragraph par = new Paragraph(chunkText);
        par.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell pdfPCell = new PdfPCell(new Phrase(par));
        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCell.setPaddingTop(10.0F);
        pdfPCell.setPaddingRight(75.0F);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        pdfPTableNama.addCell(pdfPCell);

        //add paragraph yang sudah diatur propertinya diatas kedalam parameter document
        document.add(pdfPTableNama);
    }

    private void tambahGarisSeparator(Document document) throws DocumentException {
        //method pembuat garis
        LineSeparator garisSeparator = new LineSeparator();

        //set warna
        garisSeparator.setLineColor(new BaseColor(0, 0, 0, 255));

        //set lebar garis
        garisSeparator.setLineWidth(3.0f);

        //method baru 3
        document.add(new Chunk(garisSeparator));
        tambahLineSpasi(document);
    }

    private void tambahLineSpasi(Document document) throws DocumentException {
        //untuk membuat baris baru
        Paragraph p = new Paragraph("");
        p.setPaddingTop(10.0f);
        document.add(p);
    }

    private void tambahNomorSurat(Document document, Font font) throws DocumentException {
        Chunk chunk = new Chunk("Nomor : ", font);
        Paragraph paragraf = new Paragraph(chunk);
        paragraf.setAlignment(Element.ALIGN_CENTER);

        paragraf.add("37673287");

        document.add(paragraf);
    }


    private void tambahItemBaru(Document document, Font font) throws DocumentException {
        Chunk chunk = new Chunk("SURAT KETERANGAN", font);
        Paragraph paragraf = new Paragraph(chunk);
        paragraf.setAlignment(Element.ALIGN_CENTER);

        document.add(paragraf);
    }


    private void tambahHeader(Document document) throws DocumentException, IOException {
        //set font email, warna biru
        Font fontEmail = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.BOLDITALIC, BaseColor.BLUE);

        // buat table untuk menangani image di header, dengan jumlah kolom 1
        PdfPTable pdfPTable = new PdfPTable(1);

        //add cell untuk table diatas yang diisi dengan image yang telah di kompres
        PdfPCell pdfPCell = new PdfPCell(buatImage(7, "image/lp3i.png"));

        //atur alignment secara horizontal(kesamping)
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        //hilangkan border cell
        pdfPCell.setBorder(Rectangle.NO_BORDER);

        //set padding atas sebesar 10, type data float
        pdfPCell.setPaddingTop(10.0f);

        //add cell di table
        pdfPTable.addCell(pdfPCell);

        //buat paragraph judul di header, dengan instansiasi class paragraph dari itext
        Paragraph paragraph = new Paragraph("Politeknik Jakarta \n Jl. Jend Sudirman Kota - Tangerang \n");

        //buat cell untuk judul
        pdfPCell = new PdfPCell(new Phrase(paragraph));

        //setting
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBorder(Rectangle.NO_BORDER);
        pdfPCell.setPaddingTop(10.0f);
        pdfPCell.setPaddingBottom(5.0f);
        pdfPTable.addCell(pdfPCell);

        //add table ke dokumen pdf. document berasal dari parameter method
        document.add(pdfPTable);

        //buat table again. untuk handle data contact di header. dengan jumlah kolom 4
        PdfPTable contact = new PdfPTable(4);

        //attribut lebar header dengan type data array float. bisa lihat di dokumentasi di https://itextpdf.com/en/resources
        float[] width = {90, 160, 40, 150};

        // masukkan atribut kedalam table contact
        contact.setTotalWidth(width);

        // kunci lebar, jika true maka table akan sesuai dengan lebar yang telahditentukan diatas.
        contact.setLockedWidth(false);

        //set lebar table 100.0f. type data float. agar lebar sesuai dengan halaman.
        contact.setWidthPercentage(100.0f);

        // buat cell pertama untuk text Email
        PdfPCell pdfPCellContact = new PdfPCell(new Phrase("Email : "));

        //property
        pdfPCellContact.setBorder(Rectangle.NO_BORDER);
        pdfPCellContact.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellContact.setPaddingRight(3.0f);
        pdfPCellContact.setPaddingBottom(15.0f);

        // add chunk untuk cell kedua. menggunakan chunk agar kita bisa set font yang telah dideklarasikan di awal method
        Chunk chunk = new Chunk("akademik.sedirman@gmail.com", fontEmail);

        //masukkan chunk ke paragraph
        Paragraph p = new Paragraph(chunk);

        //set isi cell kedua
        PdfPCell pdfPCellValueContact = new PdfPCell(p);

        //property
        pdfPCellValueContact.setBorder(Rectangle.NO_BORDER);
        pdfPCellValueContact.setPaddingLeft(3.0f);
        pdfPCellValueContact.setPaddingBottom(15.0f);

        //add cell ketiga
        PdfPCell pdfPCellSite = new PdfPCell(new Phrase("Site : "));

        //property
        pdfPCellSite.setBorder(Rectangle.NO_BORDER);
        pdfPCellSite.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pdfPCellSite.setPaddingRight(3.0f);
        pdfPCellSite.setPaddingBottom(15.0f);

        //instansiasi chunk yang berisi text, dengan font yang telah dideklarasikan di awal method
        chunk = new Chunk("www.plj.ac.id", fontEmail);

        //add chunk ke paragraph. dengan instansiasi class paragraph dari itextpdf
        p = new Paragraph(chunk);

        //add cell keempat menggunakan chunk again agar bisa merubah warna
        PdfPCell pdfPCellValueSite = new PdfPCell(p);

        //property
        pdfPCellValueSite.setBorder(Rectangle.NO_BORDER);
        pdfPCellValueSite.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfPCellValueSite.setPaddingLeft(3.0f);
        pdfPCellValueSite.setPaddingBottom(15.0f);

        //add table contact dengan masing2 cell yang telah dideklarasikan di atas
        contact.addCell(pdfPCellContact);
        contact.addCell(pdfPCellValueContact);
        contact.addCell(pdfPCellSite);
        contact.addCell(pdfPCellValueSite);

        //add table kedua ke document
        document.add(contact);
    }

    private String bulanIndo(String waktu) {
        String[] format = waktu.split("-");
        String bulan = null;
        switch (format[1]) {
            case "01":
                bulan = "Januari";
                break;
            case "02":
                bulan = "Februari";
                break;
            case "03":
                bulan = "Maret";
                break;
            case "04":
                bulan = "April";
                break;
            case "05":
                bulan = "Mei";
                break;
            case "06":
                bulan = "Juni";
                break;
            case "07":
                bulan = "Juli";
                break;
            case "08":
                bulan = "Agustus";
                break;
            case "09":
                bulan = "September";
                break;
            case "10":
                bulan = "Oktober";
                break;
            case "11":
                bulan = "November";
                break;
            case "12":
                bulan = "Desember";
                break;
        }
        return format[2] + " " + bulan + " " + format[0];
    }

    private String tahunAjaran(String nim) {
        String awalnim = nim.substring(0, 2);
        int tahunawal = Integer.parseInt(awalnim);
        int tahunakhir = tahunawal + 3;
        String format = null;
        if (tahunawal > 0) {
            format = "20" + tahunawal + "/" + "20" + tahunakhir;
        }
        return format;
    }

    private Image buatImage(int ukuran, String pathGambar) throws IOException, BadElementException {
        //ambil logo menggunakakan inputstream
        InputStream inputStream = getAssets().open(pathGambar);

        //tangani logo menggunakan bitmap
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        //jadikan byte untuk di output
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //ambil data byte lalu compress lagi menggunakan format png
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);


        // instansiasi image dari data byte yang sudah di kompress menggunakan class dari itextpdf
        Image image = Image.getInstance(byteArrayOutputStream.toByteArray());

        //set ukuran image logo
        image.scalePercent(ukuran);
        return image;
    }
}