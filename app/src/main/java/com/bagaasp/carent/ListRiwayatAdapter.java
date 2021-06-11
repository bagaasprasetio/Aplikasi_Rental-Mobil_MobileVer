package com.bagaasp.carent;

import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListRiwayatAdapter extends RecyclerView.Adapter<ListRiwayatAdapter.Holder> {

    private ArrayList<datariwayat> dataRiwayat;
    private Activity activity;
    String getTanggal, day, showHari, month, showBulan, tanggal, tahun;

    String hari[] = {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
    String bulan[] = {"", "Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

    public ListRiwayatAdapter(ArrayList<datariwayat> dataRiwayat, Activity activity){
        this.dataRiwayat = dataRiwayat;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListRiwayatAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_riwayat, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Locale localeID = new Locale("in", "ID");
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(localeID);

        datariwayat model = dataRiwayat.get(position);

        getTanggal = model.getTanggalSewa();

        day = getTanggal.substring(0, 3);

        if (day.equals("Sun")){
            showHari = hari[1];
        } else if (day.equals("Mon")){
            showHari = hari[2];
        } else if (day.equals("Tue")){
            showHari = hari[3];
        } else if (day.equals("Wed")){
            showHari = hari[4];
        } else if (day.equals("Thu")){
            showHari = hari[5];
        } else if (day.equals("Fri")){
            showHari = hari[6];
        } else if (day.equals("Sat")){
            showHari = hari[7];
        } else {
            showHari = "NULL";
        }



        month = getTanggal.substring(8, 11);

        if (month.equals("Jan")){
            showBulan = bulan[1];
        } else if (month.equals("Feb")){
            showBulan = bulan[2];
        } else if (month.equals("Mar")){
            showBulan = bulan[3];
        } else if (month.equals("Apr")){
            showBulan = bulan[4];
        } else if (month.equals("May")){
            showBulan = bulan[5];
        } else if (month.equals("Jun")){
            showBulan = bulan[6];
        } else if (month.equals("Jul")){
            showBulan = bulan[7];
        } else if (month.equals("Agt")){
            showBulan = bulan[8];
        } else if (month.equals("Sep")){
            showBulan = bulan[9];
        } else if (month.equals("Oct")){
            showBulan = bulan[10];
        } else if (month.equals("Nov")){
            showBulan = bulan[11];
        } else if (month.equals("Dec")){
            showBulan = bulan[12];
        } else {
            showBulan = "NULL";
        }


        tanggal = getTanggal.substring(5, 7);
        tahun = getTanggal.substring(12, 16);


        holder.hariTanggal.setText("Hari " + showHari + ", "+ tanggal + " " + showBulan + " " + tahun + ".");
        holder.jamSewa.setText(" Jam " + model.getWaktuPenyewan());
        holder.namaMobil.setText(model.getNamaMobil());
        holder.namaDriver.setText(model.getNamaDriver());
        holder.hargaSewa.setText(rupiah.format(model.getBiayaMobil()));
        holder.kodePenyewaan.setText(model.getKodePenyewaan());
        holder.statusSewa.setText(model.getStatusPenyewaan());
        holder.tglAmbil.setText(model.getTglAmbil());
        holder.tglBalik.setText(model.getTglBalik());
        holder.lamaSewa.setText(model.getLamaSewa() + " Hari");
        holder.biayaDriver.setText(rupiah.format(model.getBiayaDriver()));
        holder.biayaKerusakan.setText(rupiah.format(model.getBiayaKerusakan()));
        holder.totalPembayaran.setText(rupiah.format(model.getTotalPembayaran()));
        holder.metodeBayar.setText(model.getMetodeBayar());

        Glide.with(activity)
                .load(config.IPGambar + model.getGambarMobil())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.gambarMobil);


        if (model.getStatusPenyewaan().equals("Aktif") ){
            holder.statusSewa.setTextColor(activity.getResources().getColor(R.color.merah));
        } else if (model.getStatusPenyewaan().equals("Selesai")){
            holder.statusSewa.setTextColor(activity.getResources().getColor(R.color.ijo));
        } else {
            holder.statusSewa.setTextColor(activity.getResources().getColor(R.color.kuning));
        }


    }

    @Override
    public int getItemCount() {
        return dataRiwayat.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView gambarMobil;
        TextView hariTanggal, jamSewa, namaMobil, namaDriver, hargaSewa, kodePenyewaan,
                statusSewa, lamaSewa, biayaDriver, totalPembayaran, biayaKerusakan, metodeBayar, tglAmbil, tglBalik;

        public Holder(@NonNull View itemView) {
            super(itemView);

            hariTanggal = itemView.findViewById(R.id.hari_sewa);
            jamSewa = itemView.findViewById(R.id.jam_sewa);
            namaMobil = itemView.findViewById(R.id.nama_mobil);
            namaDriver = itemView.findViewById(R.id.nama_driver);
            hargaSewa = itemView.findViewById(R.id.biaya_sewa);
            kodePenyewaan = itemView.findViewById(R.id.kode_penyewaan);
            tglAmbil = itemView.findViewById(R.id.tgl_pengambilan);
            tglBalik = itemView.findViewById(R.id.tgl_pengembalian);
            statusSewa = itemView.findViewById(R.id.status_sewa);
            lamaSewa = itemView.findViewById(R.id.lamanya);
            biayaDriver = itemView.findViewById(R.id.biaya_driver);
            totalPembayaran = itemView.findViewById(R.id.total_bayar);
            gambarMobil = itemView.findViewById(R.id.gambar_mobil);
            biayaKerusakan = itemView.findViewById(R.id.biaya_rusak);
            metodeBayar = itemView.findViewById(R.id.metode_bayar);
        }
    }
}
