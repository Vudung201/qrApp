package com.example.qrapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryEmployeeCheckoutActivity extends AppCompatActivity {
    private RecyclerView rcvHistory;
    private HistoryAdapter historyAdapter;
    List<History> arrayList;
    String url ="http://192.168.0.103/loginQRcode/getdata_history_checkOut.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_employee_checkout);

        ActionBar actionBar = getSupportActionBar();

        rcvHistory = findViewById(R.id.rcv_history);

        historyAdapter = new HistoryAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistory.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        historyAdapter.setData(arrayList);
        rcvHistory.setAdapter(historyAdapter);

        Intent intent = getIntent();
        String maNV = String.valueOf(intent.getStringExtra("maNV"));
        GetData(url, maNV);
    }
    private void GetData(String url, String maNV) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        if(maNV.trim().equals(object.getString("MaNV"))){
                        arrayList.add(new History(
                                object.getInt("Id"),
                                object.getString("MaNV"),
                                object.getString("TenNV"),
                                object.getString("NgayRa"),
                                object.getString("GioRa")

                        ));}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                historyAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HistoryEmployeeCheckoutActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}