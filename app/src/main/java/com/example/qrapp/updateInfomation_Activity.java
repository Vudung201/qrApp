package com.example.qrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class updateInfomation_Activity extends AppCompatActivity implements View.OnClickListener {
    EditText account,name,emails,phone,gender,password;
    Button back,save;
    private static String maquyen;
    private static final String URLupdate= "http://192.168.0.103/loginQRcode/update.php";
    private static final String URLgetInfo= "http://192.168.0.103/loginQRcode/getInfomation.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_update_infomation);
        Intent intentget = getIntent();
        String maNV = intentget.getStringExtra("maNV");
        addDatatoEdittext();
        save = findViewById(R.id.button_saveInfo);
        save.setOnClickListener(this);

        back = findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (maquyen.equals("1")){
                    Intent intent = new Intent(updateInfomation_Activity.this, homePage.class);
                    intent.putExtra("maNV",maNV);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(updateInfomation_Activity.this, homepage_Employee.class);
                    intent.putExtra("maNV",maNV);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
    }
    private void addDatatoEdittext() {
        Intent intent = getIntent();
        String maNV = intent.getStringExtra("maNV");
        maNV.trim();
        StringRequest request = new StringRequest(Request.Method.POST, URLgetInfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (result.equals("success")){
                        for (int i = 0; i<jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String tenDN = object.getString("tenDangNhap");
                            String tenNV = object.getString("tenNV");
                            String email = object.getString("email");
                            String soDienThoai = object.getString("soDienThoai");
                            String gioiTinhSo = object.getString("gioiTinh");
                            String matKhau = object.getString("matKhau");
                            maquyen = object.getString("maquyen");

                            account = findViewById(R.id.edditext_account);
                            name = findViewById(R.id.edditext_name);
                            emails = findViewById(R.id.edditext_email);
                            phone =findViewById(R.id.edditext_phone);
                            gender = findViewById(R.id.edditext_gender);
                            password =findViewById(R.id.edditext_password);

                            account.setFocusable(false);
                            account.setEnabled(false);

                            account.setText(tenDN);
                            name.setText(tenNV);
                            emails.setText(email);
                            phone.setText(soDienThoai);
                            if (gioiTinhSo.equals("1")){
                                gender.setText("Nam");
                            }else {
                                gender.setText("Nữ");
                            }
                            password.setText(matKhau);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pr = new HashMap<String,String>();
                pr.put("maNV",maNV);
                return pr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    public void onClick(View view) {

        Intent intent = getIntent();
        String maNV = intent.getStringExtra("maNV");
        maNV.trim();
        StringRequest request = new StringRequest(Request.Method.POST, URLupdate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")){
                    Toast.makeText(getApplicationContext(),"Update Thành Công",Toast.LENGTH_SHORT).show();
                    addDatatoEdittext();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pr = new HashMap<String,String>();
                name = findViewById(R.id.edditext_name);
                emails = findViewById(R.id.edditext_email);
                phone =findViewById(R.id.edditext_phone);
                gender = findViewById(R.id.edditext_gender);
                password =findViewById(R.id.edditext_password);

                String tenNV = name.getText().toString();
                String email = emails.getText().toString();
                String soDienThoai = phone.getText().toString();
                String gioiTinhSo;
                String gioiTinh = gender.getText().toString();
                if (gioiTinh.equals("Nam") || gioiTinh.equals("nam")){
                    gioiTinhSo = "1";
                }else {
                    gioiTinhSo = "0";
                }
                String matKhau = password.getText().toString();
                pr.put("maNV",maNV);
                pr.put("tenNV",tenNV);
                pr.put("email",email);
                pr.put("soDienThoai",soDienThoai);
                pr.put("gioiTinh",gioiTinhSo);
                pr.put("matKhau",matKhau);
                return pr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);


    }
}