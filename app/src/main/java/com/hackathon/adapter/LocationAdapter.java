package com.hackathon.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackathon.job.MainWorkerFragment;
import com.hackathon.job.SubListActivity;
import com.hackathon.model.kakao.category_search.Document;

import java.util.ArrayList;

import com.hackathon.job.R;
import com.hackathon.util.BusProvider;
import com.hackathon.util.BusProvider2;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    Context context;
    ArrayList<Document> items;
    EditText editText;
    RecyclerView recyclerView;
    int flag;
    public LocationAdapter(ArrayList<Document> items, Context context, EditText editText, RecyclerView recyclerView, int flag) {
        this.context = context;
        this.items = items;
        this.editText = editText;
        this.recyclerView = recyclerView;
        this.flag = flag;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItem(Document item) {
        items.add(item);
    }


    public void clear() {
        items.clear();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(items.get(position).getId());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_location, viewGroup, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int i) {
        final Document model = items.get(i);
        holder.placeNameText.setText(model.getPlaceName());
        holder.addressText.setText(model.getAddressName());
        holder.item_location_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(model.getPlaceName());
                recyclerView.setVisibility(View.GONE);


                if (flag == 1) {

                    BusProvider2.getInstance().post(model);
                }else{

                    BusProvider.getInstance().post(model);
                }

                ((Activity)context).finish();
            }
        });
    }


    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView placeNameText;
        TextView addressText;
        LinearLayout item_location_all;

        public LocationViewHolder(@NonNull final View itemView) {
            super(itemView);
            placeNameText = itemView.findViewById(R.id.item_location_tv_placename);
            addressText = itemView.findViewById(R.id.item_location_tv_address);
            item_location_all = itemView.findViewById(R.id.item_location_all);
        }
    }
}
