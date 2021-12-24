    package com.example.qrapp;

    import androidx.appcompat.app.AppCompatActivity;

    import android.annotation.SuppressLint;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import com.android.volley.AuthFailureError;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;

    import java.io.StringReader;
    import java.security.SecureRandom;
    import java.security.cert.X509Certificate;
    import java.util.HashMap;
    import java.util.Map;

    import javax.net.ssl.HostnameVerifier;
    import javax.net.ssl.HttpsURLConnection;
    import javax.net.ssl.SSLContext;
    import javax.net.ssl.SSLSession;
    import javax.net.ssl.TrustManager;
    import javax.net.ssl.X509TrustManager;

    public class login_Activity extends AppCompatActivity {
        EditText account, password;
        String tenDangNhap, matKhau;
        String URL = "http://192.168.0.103/loginQRcode/login.php";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
            getSupportActionBar().hide(); //hide the title bar
            setContentView(R.layout.activity_login);
            tenDangNhap = matKhau = "";
            account = findViewById(R.id.edditext_account);
            password = findViewById(R.id.edditext_password);

        }

        public void login(View view){
            tenDangNhap = account.getText().toString();
            matKhau = password.getText().toString();
            if(!tenDangNhap.equals("") && !matKhau.equals("")){
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String maNV,res;
                        maNV = "";
                        if (!response.equals("failed")){
                            res = response.substring(0,8);
                            maNV = response.substring(9);
                        }else{
                            res = "failed";
                        }
                        res.trim();
                        if (res.equals("success1")) {
                            Intent intent = new Intent(login_Activity.this, homePage.class);
                            intent.putExtra("maNV",maNV);
                            startActivity(intent);
                            finish();
                        }else if (res.equals("success0")){
                            Intent intent = new Intent(login_Activity.this, homepage_Employee.class);
                            intent.putExtra("maNV",maNV);
                            startActivity(intent);
                            finish();
                        } else if (res.equals("failed")) {
                            Toast.makeText(login_Activity.this, "Tài Khoản Hoặc Mật Khẩu Không Chính Xác", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(login_Activity.this,error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> data = new HashMap<>();
                        data.put("tenDangNhap",tenDangNhap);
                        data.put("matKhau", matKhau);
                        return data;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }else{
                Toast.makeText(login_Activity.this,"Vui Lòng Nhập Đầy Đủ Các Trường", Toast.LENGTH_SHORT).show();
            }

        }

    }