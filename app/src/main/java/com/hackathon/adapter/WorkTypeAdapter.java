package com.hackathon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackathon.model.kakao.category_search.Document;

import java.util.ArrayList;

import com.hackathon.job.R;
import com.hackathon.room.worktype.Type;
import com.hackathon.util.BusProvider;

public class WorkTypeAdapter extends RecyclerView.Adapter<WorkTypeAdapter.WorkTypeViewHolder> {
    Context context;
    ArrayList<Type> items;
    EditText editText;
    RecyclerView recyclerView;

    public WorkTypeAdapter(ArrayList<Type> items, Context context, EditText editText, RecyclerView recyclerView) {
        this.context = context;
        this.items = items;
        this.editText = editText;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItem(Type item) {
        items.add(item);
    }


    public void clear() {
        items.clear();
    }



    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public WorkTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.work_type_adapter, viewGroup, false);
        return new WorkTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkTypeViewHolder holder, int i) {
        final Type model = items.get(i);
        holder.item_work_type_big.setText(model.getBigtype());
        holder.item_work_type_small.setText(model.getSmalltype());
        holder.ll_work_type_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(model.getSmalltype());
                recyclerView.setVisibility(View.GONE);
//                BusProvider.getInstance().post(model);
            }
        });
    }


    public class WorkTypeViewHolder extends RecyclerView.ViewHolder {
        TextView item_work_type_big;
        TextView item_work_type_small;
        LinearLayout ll_work_type_item;
        public WorkTypeViewHolder(@NonNull final View itemView) {
            super(itemView);
            item_work_type_big = itemView.findViewById(R.id.item_work_type_big);
            item_work_type_small = itemView.findViewById(R.id.item_work_type_small);
            ll_work_type_item = itemView.findViewById(R.id.ll_work_type_item);
        }
    }
}
