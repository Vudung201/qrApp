package com.example.qrapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class homepage_Employee extends AppCompatActivity implements View.OnClickListener {
    Button scanButton, updateButton, logOut, button_viewHistory_out;
    private static final String URLcheckout = "http://192.168.0.103/loginQRcode/insert_checkout.php";
    private static final String URLgetCart = "http://192.168.0.103/loginQRcode/getCart.php";
    static double tongTien;
    static String nhanvien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_homepage_employee);
        Intent intentget = getIntent();
        String maKH = intentget.getStringExtra("maKH");
        maKH.trim();
        scanButton = findViewById(R.id.button_ScanQR);
        scanButton.setOnClickListener(this);
        updateButton = findViewById(R.id.button_updateInfo);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homepage_Employee.this, updateInfomation_Activity.class);
                intent.putExtra("maKH", maKH);
                startActivity(intent);
            }
        });

        logOut = findViewById(R.id.button_logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homepage_Employee.this, login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        button_viewHistory_out = findViewById(R.id.button_viewHistory_out);
        button_viewHistory_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homepage_Employee.this, HistoryEmployeeCheckoutActivity.class);
                intent.putExtra("maKH", maKH);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        scanCode();
    }

    private void scanCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Đang Quét");
        intentIntegrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        getCartSum(result.getContents(), result);
        if (result == null) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void getCartSum(String nhanvien, IntentResult result1) {
        StringRequest request = new StringRequest(Request.Method.POST, URLgetCart, new Response.Listener<String>() {
            public void onResponse(String response) {
                final IntentResult result = result1;
                response.trim();
                String res = response.substring(1, 7);
                if (res.compareToIgnoreCase("failed") != 0) {
                    tongTien = Double.parseDouble(response);
                    if (result != null) {
                        if (result.getContents() != null) {
                            System.out.println(response);
                            AlertDialog.Builder builder = new AlertDialog.Builder(homepage_Employee.this);
                            builder.setMessage("Nhân Viên tiếp xúc: " + result.getContents() + "\n\nTổng tiền: " + String.format("%,.0f", tongTien) + " VNĐ");
                            builder.setTitle("Xác Nhận Thanh Toán Đơn Hàng ?");
                            builder.setPositiveButton("Quét Lại", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    scanCode();
                                }
                            }).setNegativeButton("Xác Nhận", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    insertPayment(result.getContents(),tongTien);
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(homepage_Employee.this, "no result", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(homepage_Employee.this, "Quét thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pr = new HashMap<String, String>();
                pr.put("nhanvien", nhanvien);
                return pr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void insertPayment(String nhanviens, double tongtiens) {
        Intent intent = getIntent();
        String maKH = intent.getStringExtra("maKH");
        maKH.trim();
        StringRequest request = new StringRequest(Request.Method.POST, URLcheckout, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Thanh Toán Thành Công", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pr = new HashMap<String, String>();
                pr.put("maKH", maKH);
                pr.put("nhanvien", nhanviens);
                pr.put("tongtien", String.valueOf(tongtiens));
                return pr;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }


}