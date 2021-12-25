package com.example.qrapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

public class homePage extends AppCompatActivity implements View.OnClickListener {
    Button scanButton,updateButton,logOut, buttonEmployee, button_viewHistory_Out, button_viewHistory;
    private static final String URLcheckin = "http://192.168.0.103/loginQRcode/insert_checkin.php";
    private static final String URLcheckout = "http://192.168.0.103/loginQRcode/insert_checkout.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_home_page);
        Intent intentget = getIntent();
        String maNV = intentget.getStringExtra("maNV");
        maNV.trim();
        scanButton = findViewById(R.id.button_ScanQR);
        scanButton.setOnClickListener(this);
        updateButton = findViewById(R.id.button_updateInfo);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this,updateInfomation_Activity.class);
                intent.putExtra("maNV",maNV);
                startActivity(intent);
            }
        });

        logOut = findViewById(R.id.button_logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this, login_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        buttonEmployee = findViewById(R.id.button_updateInfoEmployee);
        buttonEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( homePage.this, EmployeeManagerActivity.class);
                startActivity(intent);
            }
        });

        button_viewHistory_Out = findViewById(R.id.button_viewHistory_Out);
        button_viewHistory_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this, HistoryCheckoutActivity.class);
                startActivity(intent);
            }
        });

        button_viewHistory = findViewById(R.id.button_viewHistory);
        button_viewHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homePage.this, HistoryCheckinActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {
        scanCode();
    }
    private  void  scanCode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.setPrompt("Đang Quét");
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (result != null){
            if (result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Xác Nhận "+result.getContents()+" Công ty ?");
                builder.setTitle("Kết Quả Quét");
                builder.setPositiveButton("Quét Lại", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scanCode();
                    }
                }).setNegativeButton("Xác Nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (result.getContents().equals("checkin")){
                            insertCheckin();
                        }else if(result.getContents().equals("checkout")){
                            insertCheckout();
                        }
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Toast.makeText(this,"no result", Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void insertCheckin() {
        Intent intent = getIntent();
        String maNV = intent.getStringExtra("maNV");
        maNV.trim();
        StringRequest request = new StringRequest(Request.Method.POST, URLcheckin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")){
                    Toast.makeText(getApplicationContext(),"CheckIn Thành Công",Toast.LENGTH_SHORT).show();
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


    private void insertCheckout() {
        Intent intent = getIntent();
        String maNV = intent.getStringExtra("maNV");
        maNV.trim();
        StringRequest request = new StringRequest(Request.Method.POST, URLcheckout, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success")){
                    Toast.makeText(getApplicationContext(),"CheckOut Thành Công",Toast.LENGTH_SHORT).show();
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

}