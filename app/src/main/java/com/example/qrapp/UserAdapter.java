package com.example.qrapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> implements Filterable {
    private EmployeeManagerActivity context;
    private List<User> userList;
    private List<User> userListOld;

    public UserAdapter(EmployeeManagerActivity context) {
        this.context = context;
    }

    public void setData (List<User> list){
        this.userList = list;
        this.userListOld = list;
        notifyDataSetChanged(); //load Bind du lieu vao UserAdapter
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null)
            return;


        holder.tvName.setText(user.getName());

        // bat su kien sua & xoa

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateEmployeeActivity.class);
                intent.putExtra("dataEmployee",user);
                context.startActivity(intent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete_Employee(user.getName(), user.getMasv());
            }
        });

    }
    private void Delete_Employee(String ten, String maNV){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có muốn xóa nhân viên: " + ten + " không?");
        builder.setPositiveButton("CÓ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            context.DeleteEmployee(maNV);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        if(userList != null)
            return userList.size();
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

    private ImageView imgUser;
    private ImageView imgEdit;
    private TextView  tvName;
    private ImageView imgDelete;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imgUser = itemView.findViewById(R.id.avatar);
            imgEdit = itemView.findViewById(R.id.edit);
            tvName  = itemView.findViewById(R.id.tvName);
            imgDelete = itemView.findViewById(R.id.delete);

        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    userList =  userListOld;
                }else {
                    List<User> list = new ArrayList<>();
                    for (User user: userListOld){
                        if (user.getName().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(user);
                        }

                    }
                    userList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = userList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userList= (List<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
