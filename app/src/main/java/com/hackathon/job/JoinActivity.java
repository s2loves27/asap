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
            flag = "???????????? ?????? ?????? ???????????????.";

        }
        else if (name.equals("") || name == null){
            flag = "????????? ?????? ?????? ???????????????.";

        }
        else if (password.equals("") || password == null){
            flag = "??????????????? ?????? ?????? ???????????????.";

        }
        else if (password2.equals("") || password2 == null){
            flag = "???????????? ????????? ?????? ?????? ???????????????.";

        }
        else if(!password.equals(password2)){
            flag = "??????????????? ????????? ????????????.";
        }

        else if (!email.matches(regEmail)){
            flag = "email ????????? ???????????????.";

        }
        else if (!password.matches(regPassword)){
            flag = "password??? ??????, ??????, ??????????????? ????????? ?????? 8??? ?????? 16??? ?????? ???????????????.";

        }
        else if (name.length() > 8){
            flag = "name??? ?????? 8??? ?????? ???????????????";
        }
        return flag;
    }

    public String checkConfirm2(boolean ch_1, boolean ch_2){
        String flag = "";
        if ((ch_1 && ch_2) == true){

        }else{
            flag = "?????? ????????? ????????? ???????????????.";

        }
        return flag;
    }

    public boolean checkPermission(){
        boolean mPermissionsGranted = false;
        String[] mRequiredPermissions = new String[1];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 11 ????????? ??????
            mRequiredPermissions[0] = Manifest.permission.READ_PHONE_NUMBERS;

        }else{
            // 10 ????????? ??????
            mRequiredPermissions[0] = Manifest.permission.READ_PRECISE_PHONE_STATE;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // ?????? ????????? ????????? ????????? ????????????.
            mPermissionsGranted = hasPermissions(JoinActivity.this, mRequiredPermissions);

            // ?????? ?????? ?????? ??? ????????? ?????? ??????
            if (!mPermissionsGranted) {
                // ????????? ????????????.
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

    // ????????? ?????? (?????? ??? ??? ??????)
    private void join_fail_1(String message) {
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("???????????? ??????");
        builder.setMessage(message);
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(getApplicationContext(), "?????? ??????????????????.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    //????????????????????? ????????????????????? ????????? ??? ???????????? AsyncTask??? ??????????????? ??????.
    public static class JoinAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userdao;

        public JoinAsyncTask(UserDao userdao) {
            this.userdao = userdao;
        }

        @Override //?????????????????????(??????????????? X)
        protected Void doInBackground(User... user) {
            //??????????????? ?????? SELECT?????? ????????? ????????????????????? ??????
            //getAll()??? ???????????? ???????????? ???????????? ????????? ?????????,  ????????????????????? ???????????? ??? ????????? ????????????. (????????????????????? ????????? ?????????????????? ???????????????.)
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
