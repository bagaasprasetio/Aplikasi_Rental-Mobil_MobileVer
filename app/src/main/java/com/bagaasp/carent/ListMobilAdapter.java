package com.bagaasp.carent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListMobilAdapter extends RecyclerView.Adapter<ListMobilAdapter.Holder>{

    private ArrayList<datamobil> dataMobil;
    private Activity activity;
    private int isSelected = 0;
    public SharedPreferences sp;

    public ListMobilAdapter(ArrayList<datamobil> dataMobil, Activity activity) {
        this.dataMobil = dataMobil;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListMobilAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listcar, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListMobilAdapter.Holder holder, int position) {

        Locale localeID = new Locale("in", "ID");
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(localeID);

        datamobil model = dataMobil.get(position);

        holder.namaMbl.setText(model.getNamaMobil());
        holder.sewaMbl.setText(rupiah.format(model.getHargaSewa()));
        holder.tipeMesinMbl.setText(model.getTipeMesin());
        holder.jmlKursiMbl.setText(model.getJumlahKursi() + " Kursi");

        holder.bind(model);

        Glide.with(activity)
                .load(config.IPGambar + dataMobil.get(position).getGambarMobil())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.gambar);



    }

    @Override
    public int getItemCount() {
        return dataMobil.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView gambar;
        TextView namaMbl, sewaMbl, tipeMesinMbl, jmlKursiMbl, kodeMobil;
        RelativeLayout carContainer;

        public Holder(@NonNull View itemView) {
            super(itemView);

            carContainer = itemView.findViewById(R.id.list_mobil);
            namaMbl = itemView.findViewById(R.id.text_namamobil);
            sewaMbl = itemView.findViewById(R.id.text_biayaSewa);
            kodeMobil = itemView.findViewById(R.id.kode_mobil);
            tipeMesinMbl = itemView.findViewById(R.id.text_tipemesin);
            jmlKursiMbl = itemView.findViewById(R.id.text_jumlahkursi);
            gambar = itemView.findViewById(R.id.gambar_mobil);

        }

        public void bind ( final datamobil dataMobil){

            if ( isSelected == 0 ){
                carContainer.setBackground(activity.getResources().getDrawable(R.drawable.edittext));
            } else {
                if ( isSelected == getAdapterPosition()){
                    carContainer.setBackground(activity.getResources().getDrawable(R.drawable.selectedbackground));
                } else {
                    carContainer.setBackground(activity.getResources().getDrawable(R.drawable.edittext));
                }
            }

            carContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    carContainer.setBackground(activity.getResources().getDrawable(R.drawable.selectedbackground));

                    if ( isSelected != getAdapterPosition()){
                        notifyItemChanged(isSelected);
                        isSelected = getAdapterPosition();
                    }

                    sp = view.getContext().getSharedPreferences("fetchMobil", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putInt("idMobil", dataMobil.getKodeMobil());
                    editor.putString("hargaSewa", String.valueOf(dataMobil.getHargaSewa()));
                    editor.putString("namaMobil", dataMobil.getNamaMobil());
                    editor.commit();

                }
            });
        }
    }

    public datamobil getSelected(){
        if ( isSelected != -1 ){
            return dataMobil.get(isSelected);
        }
        return null;
    }
}
