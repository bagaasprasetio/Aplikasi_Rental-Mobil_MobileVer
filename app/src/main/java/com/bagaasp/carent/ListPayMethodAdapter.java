package com.bagaasp.carent;

import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;

public class ListPayMethodAdapter extends RecyclerView.Adapter<ListPayMethodAdapter.Holder> {

    ArrayList<datapaymethod> dataPay;
    Activity activity;
    int isSelected = 0;
    SharedPreferences sp;

    public ListPayMethodAdapter(ArrayList<datapaymethod> dataPay, Activity activity) {
        this.dataPay = dataPay;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListPayMethodAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listpaymethod, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListPayMethodAdapter.Holder holder, int position) {

        datapaymethod dataP = dataPay.get(position);

        holder.payMethodName.setText(dataP.getNamaMethod());

        Glide.with(activity)
                .load(config.IPLogoPay + dataPay.get(position).getLogoMethod())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.logoPay);

        holder.bind(dataPay.get(position));

    }

    @Override
    public int getItemCount() {
        return dataPay.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView logoPay;
        TextView payMethodName, kodeMethod;
        RelativeLayout payContainer;

        public Holder(@NonNull View itemView) {
            super(itemView);

            logoPay = itemView.findViewById(R.id.logo_method);
            payMethodName = itemView.findViewById(R.id.text_method);
            kodeMethod = itemView.findViewById(R.id.kode_method);
            payContainer = itemView.findViewById(R.id.paymethod_container);
        }

        public void bind (final datapaymethod dtPay){

            if ( isSelected == 0 ){
                payContainer.setBackground(activity.getResources().getDrawable(R.drawable.edittext));
            } else {
                if ( isSelected == getAdapterPosition()){
                    payContainer.setBackground(activity.getResources().getDrawable(R.drawable.selectedbackground));
                } else {
                    payContainer.setBackground(activity.getResources().getDrawable(R.drawable.edittext));
                }
            }

            payContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payContainer.setBackground(activity.getResources().getDrawable(R.drawable.selectedbackground));

                    if ( isSelected != getAdapterPosition()){
                        notifyItemChanged(isSelected);
                        isSelected = getAdapterPosition();
                    }

                    sp = view.getContext().getSharedPreferences("fetchPay", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();

                    edit.putInt("idMethod", dtPay.getKodeMethod());
                    edit.commit();

                }
            });
        }
    }
}
