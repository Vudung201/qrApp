package com.example.qrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AddEmployeeActivity extends AppCompatActivity {
    EditText account,name,emails,phone,gender,password, repassword, manv, maquyen;
    Button back,save;
    private static final String FULLNAME_PATTERN = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ" +
            "ẸẺẼỀẾỂưăạảấầẩẫậắằẳẵặẹẻẽềếểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ" +
            "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN = "^(?=\\S+$)$";
    String url = "http://192.168.0.103/loginQRcode/add_employee.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_add_employee);
        AnhXa();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    add_Employee(url);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void add_Employee(String url){
        AnhXa();
        String tenNV = name.getText().toString();
        tenNV.trim();
        String email = emails.getText().toString();
        email.trim();
        String soDienThoai = phone.getText().toString();
        soDienThoai.trim();
        String gioiTinhSo;
        String gioiTinh = gender.getText().toString();
        if (gioiTinh.equals("Nam") || gioiTinh.equals("nam")){
            gioiTinhSo = "1";
        }else {
            gioiTinhSo = "0";
        }
        String matKhau = password.getText().toString();
        String reMatKhau = repassword.getText().toString();
        String maq = maquyen.getText().toString();
        matKhau.trim();
        reMatKhau.trim();
        String ma = manv.getText().toString();
        if(!ma.startsWith("NV")){
            Toast.makeText(getApplicationContext(), "Mã nhân viên phải Bắt Đầu bằng 'NV'", Toast.LENGTH_SHORT).show();
        }else if (soDienThoai.length() < 10) {
            Toast.makeText(getApplicationContext(), "SỐ ĐIỆN THOẠI chỉ nhận 10 hoặc 11 số", Toast.LENGTH_SHORT).show();
        }else if(!Pattern.matches("[0-9]+", soDienThoai)){
            Toast.makeText(getApplicationContext(), "SỐ ĐIỆN THOẠI chỉ nhận 'SỐ'", Toast.LENGTH_SHORT).show();
        }else if(!verifyFullname(tenNV)){
            Toast.makeText(getApplicationContext(), "TÊN không nhận KÝ TỰ ĐẶC BIỆT", Toast.LENGTH_SHORT).show();
        }else if(!verifyEmail(email)){
            Toast.makeText(getApplicationContext(), "EMAIL Không Hợp Lệ", Toast.LENGTH_SHORT).show();
        }else if(!gioiTinh.equals("Nam") && !gioiTinh.equals("Nữ") && !gioiTinh.equals("nam") && !gioiTinh.equals("nữ")){
            Toast.makeText(getApplicationContext(), "GIỚI TÍNH chỉ là Nam hoặc Nữ", Toast.LENGTH_SHORT).show();
        }else if(!matKhau.equals(reMatKhau)){
            Toast.makeText(getApplicationContext(), "Mật khẩu Nhập lại không khớp", Toast.LENGTH_SHORT).show();
        }else if(!matKhau.matches("\\S+")){
            Toast.makeText(getApplicationContext(), "Mật khẩu không được có Khoảng trắng", Toast.LENGTH_SHORT).show();
        }else if(matKhau.equals("")){
            Toast.makeText(getApplicationContext(), "Mật khẩu không được rỗng", Toast.LENGTH_SHORT).show();
        }else if(!maq.equals("1") && !maq.equals("0")){
            Toast.makeText(getApplicationContext(), "Mã quyền chỉ nhận 1 hoặc 0", Toast.LENGTH_SHORT).show();
        }else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")) {
                        Toast.makeText(AddEmployeeActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddEmployeeActivity.this, EmployeeManagerActivity.class));
                    } else {
                        Toast.makeText(AddEmployeeActivity.this, "Trùng Mã", Toast.LENGTH_SHORT).show();
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddEmployeeActivity.this, "xuat hien loi", Toast.LENGTH_SHORT).show();
                            Log.d("AAA", "Lôi!\n" + error.toString());

                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    String gioiTinh = "";
                    map.put("maNV", manv.getText().toString().trim());
                    map.put("hoTen", name.getText().toString().trim());
                    if (gender.getText().toString().trim().equals("Nam"))
                        map.put("gioiTinh", "1");
                    else
                        map.put("gioiTinh", "0");
                    map.put("Email", emails.getText().toString().trim());
                    map.put("tenDN", account.getText().toString().trim());
                    map.put("mK", password.getText().toString().trim());
                    map.put("soDT", phone.getText().toString().trim());
                    map.put("maQuyen", maquyen.getText().toString().trim());


                    return map;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public static boolean verifyFullname(String fullname) {
        if (fullname == null) return false;
        return fullname.matches(FULLNAME_PATTERN);
    }
    public static boolean verifyEmail(String email) {
        if (email == null) return false;
        return email.matches(EMAIL_PATTERN);
    }
    public static boolean verifyPassword(String password) {
        if (password == null) return false;
        return password.matches(PASSWORD_PATTERN);
    }
    private void AnhXa(){
        manv = findViewById(R.id.edditext_manv);
        account = findViewById(R.id.edditext_account);
        name = findViewById(R.id.edditext_name);
        emails = findViewById(R.id.edditext_email);
        phone =findViewById(R.id.edditext_phone);
        gender = findViewById(R.id.edditext_gender);
        password =findViewById(R.id.edditext_password);
        repassword = findViewById(R.id.edditext_rePassword);
        maquyen = findViewById(R.id.edditext_maquyen);
        back = findViewById(R.id.button_back);
        save = findViewById(R.id.button_saveInfo);
    }
}