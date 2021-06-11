package com.bagaasp.carent;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

public class ListBannerAdapter extends RecyclerView.Adapter<ListBannerAdapter.Holder> {
    Activity activity;
    ArrayList<databanner> dtBanner;

    public ListBannerAdapter(Activity activity, ArrayList<databanner> dtBanner) {
        this.activity = activity;
        this.dtBanner = dtBanner;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.banner, parent, false);
        Holder holder = new Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        databanner data = dtBanner.get(position);

        holder.bannerName.setText(data.getBannerName());
        holder.bannerDesc.setText(data.getBannerDesc());

        Glide.with(activity)
                .load(config.IPBanner + data.getBannerImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.bannerImage);
    }

    @Override
    public int getItemCount() {
        return dtBanner.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView bannerImage;
        TextView bannerName, bannerDesc;

        public Holder(@NonNull View itemView) {
            super(itemView);

            bannerImage = itemView.findViewById(R.id.banner_img);
            bannerName = itemView.findViewById(R.id.banner_name);
            bannerDesc = itemView.findViewById(R.id.banner_desc);
        }
    }
}
