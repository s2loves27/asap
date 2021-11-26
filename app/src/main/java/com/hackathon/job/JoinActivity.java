package com.hackathon.job;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.hackathon.room.AppDatabase;
import com.hackathon.room.User.User;
import com.hackathon.room.User.UserDao;

import java.util.List;

public class JoinActivity extends AppCompatActivity {

    CheckBox cb_join_agree_all;
    CheckBox cb_join_agree_1;
    CheckBox cb_join_agree_2;
    CheckBox cb_join_agree_3;

    TextInputEditText et_join_email;
    TextInputEditText et_join_name;
    TextInputEditText et_join_password;
    TextInputEditText et_join_password2;

    Button btn_join;

    private static final int PERMISSIONS_REQUEST_CODE = 100;


    String TAG = "@JOINACTIVITY";

    String[] PERMISSIONS = {
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_PHONE_NUMBERS
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);

        initView();

    }

    void initView() {
        cb_join_agree_all = findViewById(R.id.cb_join_agree_all);
        cb_join_agree_1 = findViewById(R.id.cb_join_agree_1);
        cb_join_agree_2 = findViewById(R.id.cb_join_agree_2);
        cb_join_agree_3 = findViewById(R.id.cb_join_agree_3);

        cb_join_agree_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cb_join_agree_1.setChecked(b);
                cb_join_agree_2.setChecked(b);
                cb_join_agree_3.setChecked(b);

            }
        });

        btn_join = findViewById(R.id.btn_join);

        et_join_email = findViewById(R.id.et_join_email);
        et_join_name = findViewById(R.id.et_join_name);
        et_join_password = findViewById(R.id.et_join_password);
        et_join_password2 = findViewById(R.id.et_join_password2);

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = et_join_email.getText().toString();
                String name = et_join_name.getText().toString();
                String password = et_join_password.getText().toString();
                String password2 = et_join_password2.getText().toString();

                boolean ch_1 = cb_join_agree_1.isChecked();
                boolean ch_2 = cb_join_agree_2.isChecked();
                boolean ch_3 = cb_join_agree_3.isChecked();


                String dialogStr = checkConfirm1(email, name, password, password2);

                if (dialogStr != "") {
                    join_fail_1(dialogStr);
                    return;
                }
                String dialogStr2 = checkConfirm2(ch_1, ch_2);

                if (dialogStr2 != "") {
                    join_fail_1(dialogStr2);
                    return;
                }

                String phoneNum = "";
                TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                boolean grant = checkPermission();
                if (grant == true){
                    phoneNum = telManager.getLine1Number();
                    if(phoneNum.startsWith("+82")){
                        phoneNum = phoneNum.replace("+82", "0");
                    }
                }


                AppDatabase db = AppDatabase.getInstance(JoinActivity.this);




                User user = new User();

                user.setEmail(email);
                user.setPassword(password);
                user.setName(name);
                user.setScore(4.3);
                if (phoneNum != ""){
                    user.setPhone(phoneNum);
                }
                new JoinAsyncTask(db.userDao()).execute(user);

                finish();

            }

        });






    }

    public String checkConfirm1(String email, String name, String password, String password2){
        String regEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String regPassword = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
        String flag = "";
        Log.i("@JOIN", email);
        if (email.equals("") || email == null){
            flag = "이메일이 입력 되지 않았습니다.";

        }
        else if (name.equals("") || name == null){
            flag = "이름이 입력 되지 않았습니다.";

        }
        else if (password.equals("") || password == null){
            flag = "패스워드가 입력 되지 않았습니다.";

        }
        else if (password2.equals("") || password2 == null){
            flag = "패스워드 확인이 입력 되지 않았습니다.";

        }
        else if(!password.equals(password2)){
            flag = "패스워드와 확인이 다릅니다.";
        }

        else if (!email.matches(regEmail)){
            flag = "email 형식을 지켜주세요.";

        }
        else if (!password.matches(regPassword)){
            flag = "password는 숫자, 문자, 특수문자를 포함한 최소 8자 최대 16자 까지 가능합니다.";

        }
        else if (name.length() > 8){
            flag = "name은 최대 8시 까지 가능합니다";
        }
        return flag;
    }

    public String checkConfirm2(boolean ch_1, boolean ch_2){
        String flag = "";
        if ((ch_1 && ch_2) == true){

        }else{
            flag = "필수 항목의 동의가 필요합니다.";

        }
        return flag;
    }

    public boolean checkPermission(){
        boolean mPermissionsGranted = false;
        String[] mRequiredPermissions = new String[1];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 11 이상인 경우
            mRequiredPermissions[0] = Manifest.permission.READ_PHONE_NUMBERS;

        }else{
            // 10 이하인 경우
            mRequiredPermissions[0] = Manifest.permission.READ_PRECISE_PHONE_STATE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 필수 권한을 가지고 있는지 확인한다.
            mPermissionsGranted = hasPermissions(JoinActivity.this, mRequiredPermissions);

            // 필수 권한 중에 한 개라도 없는 경우
            if (!mPermissionsGranted) {
                // 권한을 요청한다.
                ActivityCompat.requestPermissions(JoinActivity.this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
            }
        } else {
            mPermissionsGranted = true;
        }

        return mPermissionsGranted;

    }
    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    // 로그인 실패 (입력 값 미 입력)
    private void join_fail_1(String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("회원가입 실패");
        builder.setMessage(message);
        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getApplicationContext(), "예를 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class JoinAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userdao;

        public JoinAsyncTask(UserDao userdao) {
            this.userdao = userdao;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(User... user) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            userdao.insert(user[0]);


            List<User> users = userdao.getALL();

            for (int i = 0; i <users.size(); i++) {
                Log.i("@JOINACTIVITY", users.get(i).getEmail() + " : " + users.get(i).getPassword() );

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

        }
    }
}
