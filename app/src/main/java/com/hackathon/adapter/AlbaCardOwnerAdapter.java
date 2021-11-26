package com.hackathon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackathon.job.R;
import com.hackathon.room.AnnounceCard.AnnounceCard;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AlbaCardOwnerAdapter extends RecyclerView.Adapter<AlbaCardOwnerAdapter.AlbaCardOwnerViewHolder>{

    Context context;
    List<AnnounceCard> items;
    RecyclerView recyclerView;

    public AlbaCardOwnerAdapter(List<AnnounceCard> items, Context context,  RecyclerView recyclerView){
        this.context = context;
        this.items = items;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(AnnounceCard announceCard) {items.add(announceCard);}

    public void clear(){items.clear();}

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @NotNull
    @Override
    public AlbaCardOwnerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.alba_card_owner_item, viewGroup, false);
        return new AlbaCardOwnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AlbaCardOwnerViewHolder holder, int position) {
        final AnnounceCard card = items.get(position);

        if (card.getState() == 0){
            holder.tv_alba_card_owner_status.setText("매칭이 진행 되고 있어요!");
            holder.ll_card_owner_flag_1.setVisibility(View.VISIBLE);
            holder.ll_card_owner_flag_2.setVisibility(View.GONE);

        }
        else if (card.getState() == 1){
            holder.tv_alba_card_owner_status.setText("매칭 완료!");
            holder.ll_card_owner_flag_1.setVisibility(View.GONE);
            holder.ll_card_owner_flag_2.setVisibility(View.VISIBLE);
        }
        holder.tv_alba_card_owner_place_1.setText(card.getAddress());
        holder.tv_alba_card_owner_place_2.setText(card.getLocation() + " ▼");

        holder.tv_alba_card_owner_time.setText("근무시간 : "+card.getStartTime() + " ~ "  + card.getFinishTime());
        holder.tv_alba_card_owner_payment.setText("시급 : "+card.getPayment() + "원");
        holder.tv_alba_card_owner_category.setText("근무내용 : "+card.getCategory());
        holder.tv_alba_card_owner_desc.setText("기타사항 : " + card.getDesc());


        holder.tv_alba_card_owner_place_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ll_alba_owner_detail.getVisibility() == View.GONE){
                    holder.ll_alba_owner_detail.setVisibility(View.VISIBLE);
                    holder.tv_alba_card_owner_place_2.setText(card.getLocation() + "    ▲");
                }else{
                    holder.ll_alba_owner_detail.setVisibility(View.GONE);
                    holder.tv_alba_card_owner_place_2.setText(card.getLocation() + "    ▼");
                }
            }
        });



    }

    public class AlbaCardOwnerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_alba_card_owner_status;
        TextView tv_alba_card_owner_place_1;
        TextView tv_alba_card_owner_place_2;

        TextView tv_alba_card_owner_time;
        TextView tv_alba_card_owner_payment;
        TextView tv_alba_card_owner_category;
        TextView tv_alba_card_owner_desc;

        LinearLayout ll_alba_owner_detail;

        LinearLayout ll_card_owner_flag_1;
        LinearLayout ll_alba_owner_change;
        LinearLayout ll_alba_owner_delete;
//        LinearLayout ll_alba_owner_cancel;


        LinearLayout ll_card_owner_flag_2;

        LinearLayout ll_alba_owner_chat;
        LinearLayout ll_alba_owner_schedule;
        LinearLayout ll_alba_owner_cancel;








        public AlbaCardOwnerViewHolder(@NonNull final View itemView) {


            super(itemView);
            tv_alba_card_owner_status = itemView.findViewById(R.id.tv_alba_card_owner_status);
            tv_alba_card_owner_place_1 = itemView.findViewById(R.id.tv_alba_card_owner_place_1);
            tv_alba_card_owner_place_2 = itemView.findViewById(R.id.tv_alba_card_owner_place_2);

            tv_alba_card_owner_time = itemView.findViewById(R.id.tv_alba_card_owner_time);
            tv_alba_card_owner_payment = itemView.findViewById(R.id.tv_alba_card_owner_payment);
            tv_alba_card_owner_category =  itemView.findViewById(R.id.tv_alba_card_owner_category);
            tv_alba_card_owner_desc = itemView.findViewById(R.id.tv_alba_card_owner_desc);


            ll_alba_owner_detail = itemView.findViewById(R.id.ll_alba_owner_detail);

            ll_alba_owner_chat = itemView.findViewById(R.id.ll_alba_owner_chat);
            ll_alba_owner_schedule = itemView.findViewById(R.id.ll_alba_owner_schedule);
            ll_alba_owner_cancel = itemView.findViewById(R.id.ll_alba_owner_cancel);

            ll_card_owner_flag_1 = itemView.findViewById(R.id.ll_card_owner_flag_1);
            ll_card_owner_flag_2 = itemView.findViewById(R.id.ll_card_owner_flag_2);

            ll_alba_owner_change = itemView.findViewById(R.id.ll_alba_owner_change);
            ll_alba_owner_delete = itemView.findViewById(R.id.ll_alba_owner_delete);

        }
    }
}
