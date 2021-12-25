package com.example.qrapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeManagerActivity extends AppCompatActivity {
    Button add_Emplyee, sreach_Emplyee;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    List<User> arrayList;
    String url ="http://10.0.2.2:81/loginQRcode/getdata_employee.php";
    String url_delete ="http://10.0.2.2:81/loginQRcode/delete_employee.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        rcvUser = findViewById(R.id.rcv_user);

        userAdapter = new UserAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvUser.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        userAdapter.setData(arrayList);
        rcvUser.setAdapter(userAdapter);
        GetData(url);
        add_Emplyee = findViewById(R.id.add_emplyee);
        add_Emplyee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeManagerActivity.this, AddEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }
    private void GetData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                arrayList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        arrayList.add(new User(
                                object.getString("HoTen"),
                                object.getString("MaNV"),
                                object.getString("TenDangNhap")
                                ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmployeeManagerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
    public void DeleteEmployee(String maNV){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_delete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.trim().equals("success")){
                    Toast.makeText(EmployeeManagerActivity.this,"Xóa Thành Công",Toast.LENGTH_SHORT).show();
                    GetData(url);
                }
                else
                {
                    Toast.makeText(EmployeeManagerActivity.this,"Lỗi Xóa",Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("maNV", maNV);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

}