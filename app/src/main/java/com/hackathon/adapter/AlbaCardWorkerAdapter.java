package com.hackathon.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hackathon.job.R;
import com.hackathon.room.AnnounceCard.AnnounceCard;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AlbaCardWorkerAdapter extends RecyclerView.Adapter<AlbaCardWorkerAdapter.AlbaCardWorkerViewHolder>{

    Context context;
    List<AnnounceCard> items;
    RecyclerView recyclerView;

    public AlbaCardWorkerAdapter(List<AnnounceCard> items, Context context, RecyclerView recyclerView){
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
    public AlbaCardWorkerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.alba_card_worker_item, viewGroup, false);
        return new AlbaCardWorkerViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull AlbaCardWorkerViewHolder holder, int position) {
        final AnnounceCard card = items.get(position);

        LocalTime now = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        String formateNow = now.format(formatter);

        int hour = now.getHour();
        int minute = now.getMinute();

        String[] startTime = card.getStartTime().split(":");

        int starthour = Integer.parseInt(startTime[0]);
        int startminute = Integer.parseInt(startTime[0]);

        int resultMinute = startminute - minute;
        int resultHour = 0;
        if (resultMinute < 0){
            resultHour = starthour - hour - 1;
            resultMinute = 60 + resultMinute;
        }else{
            resultHour = starthour - hour;

        }

        if (card.getState() == 0){
            holder.tv_alba_card_worker_status.setText("매칭이 진행 되고 있어요!");
        }
        else if (card.getState() == 1){
            holder.tv_alba_card_worker_status.setText("시작까지 " + resultHour + "시간 " + resultMinute+ "분 " +"남았어요.");
        }

        holder.tv_alba_card_worker_place_1.setText(card.getAddress());
        holder.tv_alba_card_worker_place_2.setText(card.getLocation() + " ▼");

        holder.tv_alba_card_worker_time.setText("근무시간 : "+card.getStartTime() + " ~ "  + card.getFinishTime());
        holder.tv_alba_card_worker_payment.setText("시급 : "+card.getPayment() + "원");
        holder.tv_alba_card_worker_category.setText("근무내용 : "+card.getCategory());
        holder.tv_alba_card_worker_desc.setText("기타사항 : " + card.getDesc());


        holder.tv_alba_card_worker_place_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ll_alba_worker_detail.getVisibility() == View.GONE){
                    holder.ll_alba_worker_detail.setVisibility(View.VISIBLE);
                    holder.tv_alba_card_worker_place_2.setText(card.getLocation() + "    ▲");
                }else{
                    holder.ll_alba_worker_detail.setVisibility(View.GONE);
                    holder.tv_alba_card_worker_place_2.setText(card.getLocation() + "    ▼");
                }
            }
        });



    }

    public class AlbaCardWorkerViewHolder extends RecyclerView.ViewHolder {
        TextView tv_alba_card_worker_status;
        TextView tv_alba_card_worker_place_1;
        TextView tv_alba_card_worker_place_2;

        TextView tv_alba_card_worker_time;
        TextView tv_alba_card_worker_payment;
        TextView tv_alba_card_worker_category;
        TextView tv_alba_card_worker_desc;

        LinearLayout ll_alba_worker_detail;


        LinearLayout ll_card_worker_flag_1;

        LinearLayout ll_alba_worker_chat;
        LinearLayout ll_alba_worker_cancel;








        public AlbaCardWorkerViewHolder(@NonNull final View itemView) {


            super(itemView);
            tv_alba_card_worker_status = itemView.findViewById(R.id.tv_alba_card_worker_status);
            tv_alba_card_worker_place_1 = itemView.findViewById(R.id.tv_alba_card_worker_place_1);
            tv_alba_card_worker_place_2 = itemView.findViewById(R.id.tv_alba_card_worker_place_2);

            tv_alba_card_worker_time = itemView.findViewById(R.id.tv_alba_card_worker_time);
            tv_alba_card_worker_payment = itemView.findViewById(R.id.tv_alba_card_worker_payment);
            tv_alba_card_worker_category =  itemView.findViewById(R.id.tv_alba_card_worker_category);
            tv_alba_card_worker_desc = itemView.findViewById(R.id.tv_alba_card_worker_desc);


            ll_alba_worker_detail = itemView.findViewById(R.id.ll_alba_worker_detail);

            ll_alba_worker_chat = itemView.findViewById(R.id.ll_alba_worker_chat);
            ll_alba_worker_cancel = itemView.findViewById(R.id.ll_alba_worker_cancel);

            ll_card_worker_flag_1 = itemView.findViewById(R.id.ll_card_worker_flag_1);



        }
    }
}
