package com.example.qrapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryEmployee_checkin_Adapter extends RecyclerView.Adapter<HistoryEmployee_checkin_Adapter.UserViewHolder> implements Filterable {
    private Context context;
    private List<History> historyList;
    private List<History> historyListOld;

    public HistoryEmployee_checkin_Adapter(Context context) {
        this.context = context;
    }

    public void setData (List<History> list){
        this.historyList = list;
        this.historyListOld = list;
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
        String content ="Đã vào lúc: ";
        holder.tvName.setText(history.getName());
        holder.tvContent.setText(content+ history.getTime() + " " + history.getDate());
    }

    @Override
    public int getItemCount() {
        if(historyList != null)
            return historyList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    historyList =  historyListOld;
                }else {
                    List<History> list = new ArrayList<>();
                    for (History history: historyListOld){
                        if (history.getDate().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(history);
                        }

                    }
                    historyList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = historyList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                historyList= (List<History>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
