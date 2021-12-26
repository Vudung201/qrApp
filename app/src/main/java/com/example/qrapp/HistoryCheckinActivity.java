package com.example.qrapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
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

public class HistoryCheckinActivity extends AppCompatActivity {
    private RecyclerView rcvHistory;
    private History_CheckIn_Adapter historyAdapter;
    private SearchView searchView;
    List<History> arrayList;
    String url ="http://192.168.0.103/loginQRcode/getdata_history_checkin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_checkin);

        ActionBar actionBar = getSupportActionBar();

        rcvHistory = findViewById(R.id.rcv_history);

        historyAdapter = new History_CheckIn_Adapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistory.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        historyAdapter.setData(arrayList);
        rcvHistory.setAdapter(historyAdapter);
        GetData(url);
    }
    private void GetData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject object = response.getJSONObject(i);
                        arrayList.add(new History(
                                object.getInt("Id"),
                                object.getString("MaNV"),
                                object.getString("TenNV"),
                                object.getString("NgayVao"),
                                object.getString("GioVao")

                        ));
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
                        Toast.makeText(HistoryCheckinActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                historyAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                historyAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}