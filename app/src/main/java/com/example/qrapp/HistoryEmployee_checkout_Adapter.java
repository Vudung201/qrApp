package com.example.qrapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryEmployee_checkout_Adapter extends RecyclerView.Adapter<HistoryEmployee_checkout_Adapter.UserViewHolder>{
    private Context context;
    private List<History> historyList;

    public HistoryEmployee_checkout_Adapter(Context context) {
        this.context = context;
    }

    public void setData (List<History> list){
        this.historyList = list;
        notifyDataSetChanged(); //load Bind du lieu vao UserAdapter
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_history,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        com.example.qrapp.History history = historyList.get(position);
        if (history == null)
            return;
        String content ="Đã ra lúc: ";
        holder.tvName.setText(history.getName());
        holder.tvContent.setText(content+ history.getTime() + " " + history.getDate());
    }

    @Override
    public int getItemCount() {
        if(historyList != null)
            return historyList.size();
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
        private TextView  tvName;
        private TextView tvContent;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName  = itemView.findViewById(R.id.tvName);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
