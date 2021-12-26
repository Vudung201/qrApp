package com.example.qrapp;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UpdateEmployeeActivity extends AppCompatActivity {
    EditText account,name,emails,phone,gender,password, repassword, manv, maQuyen;
    Button back,save;
    private static String oldpassword;
    private static final String FULLNAME_PATTERN = "^[a-zA-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶ" +
            "ẸẺẼỀẾỂưăạảấầẩẫậắằẳẵặẹẻẽềếểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợ" +
            "ụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ\\s]+$";
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PASSWORD_PATTERN = "^(?=\\S+$)$";
    String url = "http://192.168.0.103/loginQRcode/update_employee.php";
    String URLgetInfo = "http://192.168.0.103/loginQRcode/getInfomation.php";
    String maNV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_update_employee);


        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("dataEmployee");

        maNV = user.getMasv();
        AnhXa();
        addDatatoEdittext();
        manv.setText(maNV);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update_employee(url);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void addDatatoEdittext() {
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
                            String maquyen = object.getString("maquyen");

                            account = findViewById(R.id.edditext_account);
                            name = findViewById(R.id.edditext_name);
                            emails = findViewById(R.id.edditext_email);
                            phone =findViewById(R.id.edditext_phone);
                            gender = findViewById(R.id.edditext_gender);
                            password =findViewById(R.id.edditext_password);
                            repassword = findViewById(R.id.edditext_rePassword);
                            repassword.setText("");
                            manv.setFocusable(false);
                            manv.setEnabled(false);
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
                            oldpassword = password.getText().toString();
                            maQuyen.setText(maquyen);

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
    public static boolean verifyFullname(String fullname) {
        if (fullname == null) return false;
        return fullname.matches(FULLNAME_PATTERN);
    }
    public static boolean verifyEmail(String email) {
        if (email == null) return false;
        return email.matches(EMAIL_PATTERN);
    }

    private  void  update_employee(String url)
    {
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
        matKhau.trim();
        reMatKhau.trim();
        if (soDienThoai.length() < 10) {
            Toast.makeText(getApplicationContext(), "SỐ ĐIỆN THOẠI chỉ nhận 10 hoặc 11 số", Toast.LENGTH_SHORT).show();
        }else if(!Pattern.matches("[0-9]+", soDienThoai)){
            Toast.makeText(getApplicationContext(), "SỐ ĐIỆN THOẠI chỉ nhận 'SỐ'", Toast.LENGTH_SHORT).show();
        }else if(!verifyFullname(tenNV)){
            Toast.makeText(getApplicationContext(), "TÊN không nhận KÝ TỰ ĐẶC BIỆT", Toast.LENGTH_SHORT).show();
        }else if(!verifyEmail(email)){
            Toast.makeText(getApplicationContext(), "EMAIL Không Hợp Lệ", Toast.LENGTH_SHORT).show();
        }else if(!gioiTinh.equals("Nam") && !gioiTinh.equals("Nữ") && !gioiTinh.equals("nam") && !gioiTinh.equals("nữ")){
            Toast.makeText(getApplicationContext(), "GIỚI TÍNH chỉ là Nam hoặc Nữ", Toast.LENGTH_SHORT).show();
        }else if(!matKhau.equals(reMatKhau) && !matKhau.equals(oldpassword) || matKhau.equals(oldpassword) && !matKhau.equals(reMatKhau) && !reMatKhau.equals("")){
            Toast.makeText(getApplicationContext(), "Mật khẩu Nhập lại không khớp", Toast.LENGTH_SHORT).show();
        }else if(!matKhau.matches("\\S+")){
            Toast.makeText(getApplicationContext(), "Mật khẩu không được có Khoảng trắng", Toast.LENGTH_SHORT).show();
        }else if(matKhau.equals("")){
            Toast.makeText(getApplicationContext(), "Mật khẩu không được rỗng", Toast.LENGTH_SHORT).show();
        }else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("success")) {
                        Toast.makeText(UpdateEmployeeActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UpdateEmployeeActivity.this, EmployeeManagerActivity.class));
                    } else {
                        Toast.makeText(UpdateEmployeeActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                    }

                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UpdateEmployeeActivity.this, "Xảy ra lỗi, Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<>();
                    map.put("maNV", maNV);
                    map.put("hoTen", name.getText().toString());
                    if (gender.getText().toString().trim().equals("Nam"))
                        map.put("gioiTinh", "1");
                    else
                        map.put("gioiTinh", "0");
                    map.put("Email", emails.getText().toString());
                    map.put("tenDN", account.getText().toString());
                    map.put("mK", password.getText().toString());
                    map.put("soDT", phone.getText().toString());
                    map.put("maQuyen", maQuyen.getText().toString());

                    return map;
                }
            };
            requestQueue.add(stringRequest);
        }
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
        maQuyen = findViewById(R.id.edditext_maquyen);
        back = findViewById(R.id.button_back);
        save = findViewById(R.id.button_saveInfo);
    }
}