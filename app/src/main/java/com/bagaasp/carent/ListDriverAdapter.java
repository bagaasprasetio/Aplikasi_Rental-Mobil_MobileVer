package com.bagaasp.carent;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class ListDriverAdapter extends RecyclerView.Adapter<ListDriverAdapter.Holder>{

    private ArrayList<datadriver> dataDriver;
    private Activity activity;
    private int isSelected = 0;
    public SharedPreferences sp;
    String TAG;

    public ListDriverAdapter(Activity activity, ArrayList<datadriver> dataDriver){
        this.dataDriver = dataDriver;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListDriverAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listdriver, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListDriverAdapter.Holder holder, int position) {
        datadriver model = dataDriver.get(position);

        holder.namaDrv.setText(model.getNamaDriver());
        holder.genderDrv.setText(model.getGenderDriver());
        holder.umurDrv.setText(model.getUmurDriver() + " Tahun");

        Glide.with(activity)
                .load( config.IPFotodriver + model.getFotoDriver())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.fotoDrv);

        holder.bind(dataDriver.get(position));

    }

    @Override
    public int getItemCount() {
        return dataDriver.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView namaDrv, genderDrv, umurDrv, kodeDrv;
        ImageView fotoDrv;
        RelativeLayout driverContainer;

        public Holder(@NonNull View itemView) {
            super(itemView);

            namaDrv = (TextView) itemView.findViewById(R.id.driverName);
            genderDrv = (TextView) itemView.findViewById(R.id.driverGender);
            umurDrv = (TextView) itemView.findViewById(R.id.umur_driver);
            kodeDrv = itemView.findViewById(R.id.kode_driver);
            fotoDrv = (ImageView) itemView.findViewById(R.id.foto_driver);
            driverContainer = itemView.findViewById(R.id.driver_container);
        }

        public void bind (final datadriver dtDriver){
            if (isSelected == 0 ){
                driverContainer.setBackground(activity.getResources().getDrawable(R.drawable.edittext));
            } else {
                if (isSelected == getAdapterPosition()){
                    driverContainer.setBackground(activity.getResources().getDrawable(R.drawable.selectedbackground));
                } else {
                    driverContainer.setBackground(activity.getResources().getDrawable(R.drawable.edittext));
                }
            }

            driverContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    driverContainer.setBackground(activity.getResources().getDrawable(R.drawable.selectedbackground));

                    if (isSelected != getAdapterPosition()){
                        notifyItemChanged(isSelected);
                        isSelected = getAdapterPosition();
                    }

                    sp = view.getContext().getSharedPreferences("fetchDriver", Context.MODE_PRIVATE);

                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("idDriver", dtDriver.getKodeDriver());
                    edit.putString("namaDriver", dtDriver.getNamaDriver());
                    edit.commit();

                }
            });
        }
    }
}
