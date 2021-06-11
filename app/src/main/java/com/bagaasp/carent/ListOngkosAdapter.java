package com.bagaasp.carent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

public class ListOngkosAdapter extends RecyclerView.Adapter<ListOngkosAdapter.Holder> {
    Activity activity;
    ArrayList<dataongkos> dtOngkos;
    public int isSelected = 0;
    SharedPreferences sp;

    public ListOngkosAdapter(Activity activity, ArrayList<dataongkos> dtOngkos) {
        this.activity = activity;
        this.dtOngkos = dtOngkos;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.ongkos, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Locale localeID = new Locale("in", "ID");
        NumberFormat rupiah = NumberFormat.getCurrencyInstance(localeID);

        dataongkos data = dtOngkos.get(position);

        holder.bind(data);

        holder.tujuan.setText(data.getTujuan());
        holder.biaya.setText(rupiah.format(data.getBiayaDriver()));


    }

    @Override
    public int getItemCount() {
        return dtOngkos.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView tujuan, biaya, kodeOngkos;
        LinearLayout listOngkos;


        public Holder(@NonNull View itemView) {
            super(itemView);

            tujuan = itemView.findViewById(R.id.tujuanTv);
            biaya = itemView.findViewById(R.id.biayaTv);
            kodeOngkos = itemView.findViewById(R.id.kode_ongkos);
            listOngkos = itemView.findViewById(R.id.list_ongkos);

        }

        public void bind(final dataongkos dataOngkos){

            if (isSelected == 0){
                listOngkos.setBackground(activity.getResources().getDrawable(R.drawable.edittext));
            } else {
                if ( isSelected == getAdapterPosition()){
                    listOngkos.setBackground(activity.getResources().getDrawable(R.drawable.selectedbackground));
                } else {
                    listOngkos.setBackground(activity.getResources().getDrawable(R.drawable.edittext));
                }
            }

            listOngkos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listOngkos.setBackground(activity.getResources().getDrawable(R.drawable.selectedbackground));

                    if ( isSelected != getAdapterPosition()) {
                        notifyItemChanged(isSelected);
                        isSelected = getAdapterPosition();
                    }

                    sp = view.getContext().getSharedPreferences("fetchOngkos", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();

                    edit.putInt("kdOngkos", dataOngkos.getKodeOngkos());
                    edit.putString("ongkosDrv", String.valueOf(dataOngkos.getBiayaDriver()));
                    edit.putString("tujuan", dataOngkos.getTujuan());
                    edit.commit();

                }
            });

        }
    }

    public dataongkos getSelected(){
        if (isSelected != -1){
            return dtOngkos.get(isSelected);
        }
        return null;
    }
}
