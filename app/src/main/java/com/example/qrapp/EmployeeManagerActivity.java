package com.example.qrapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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
    Button add_Emplyee, sreach_Emplyee,back;
    private RecyclerView rcvUser;
    private UserAdapter userAdapter;
    private SearchView searchView;
    List<User> arrayList;
    String url ="http://192.168.0.103/loginQRcode/getdata_employee.php";
    String url_delete ="http://192.168.0.103/loginQRcode/delete_employee.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_manager);
        ActionBar actionBar = getSupportActionBar();

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
        back = findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                userAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}