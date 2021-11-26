package com.hackathon.job;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.hackathon.restapi.ServiceGenerator;
import com.hackathon.model.StoreDto;
import com.hackathon.restapi.kakao.ApiInterface;
import com.hackathon.room.AppDatabase;
import com.hackathon.room.User.User;
import com.hackathon.room.User.UserDao;
import com.hackathon.util.Util;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.Account;
import com.kakao.sdk.user.model.Profile;

import java.time.temporal.TemporalAdjuster;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageButton btn_kakao_login;
    private Button btn_login_login;
    private Button btn_login_join;
    private TextInputEditText login_edit_email;
    private TextInputEditText login_edit_password;
    private CheckBox chk_login_status;

    private static final String TAG = "전지훈";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        String initEmail = Util.getString(LoginActivity.this, "email" );
        String initPassword = Util.getString(LoginActivity.this, "password");

        btn_login_login = findViewById(R.id.btn_login_login);
        btn_kakao_login = findViewById(R.id.btn_kakao_login);
        login_edit_email = findViewById(R.id.login_edit_email);
        login_edit_password = findViewById(R.id.login_edit_password);
        btn_login_join = findViewById(R.id.btn_login_join);
        chk_login_status = findViewById(R.id.chk_login_status);

        login_edit_email.setText(initEmail);
        login_edit_password.setText(initPassword);


        //String TOKEN = getToken(); // access token을 가져오는 함수를 직접 정의하셔야합니다.
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, null);
        apiInterface.getPosts("1").enqueue(new Callback<StoreDto>() {
            @Override
            public void onResponse(Call<StoreDto> call, Response<StoreDto> response) {
                if (response.isSuccessful()) {
                    // response.body()
//                     response.body()에서 넘어오는 데이터로 Adapter에 뿌려주기
                    Log.d(TAG, response.body().toString());
                } else {
                    Log.d("REST FAILED MESSAGE", response.message());
                }
            }

            @Override
            public void onFailure(Call<StoreDto> call, Throwable t) {
                Log.d("REST ERROR!", t.getMessage());
            }
        });


        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase db = AppDatabase.getInstance(LoginActivity.this);

                String email = login_edit_email.getText().toString();
                String password = login_edit_password.getText().toString();

//                User user = new User();



//                user.userEmail = email;
//                user.userPassword = password;

                DbAsyncTask task = new DbAsyncTask(db.userDao(), LoginActivity.this,chk_login_status);
                task.execute(email, password);




            }
        });

        btn_login_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });
        btn_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)) {
                    //카카오톡이 설치 되어 있으면 카톡으로 로그인 확인
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, (token, loginError) -> {
                        if (loginError != null) {
                            //로그인 실패
                            Log.e(TAG, "로그인 실패");
                        } else {
                            //로그인 성공

                            //사용자 정보 요청
                            requestMe();
                        }
                        return null;
                    });
                } else {
                    // 카카오톡이 설치 되어 있지 않는 경우 앱 내장 웹뷰 방식으로 카카오 계정 확인 요청
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, (token, loginError) -> {
                        if (loginError != null) {
                            //로그인 실패
                            Log.e(TAG, "로그인 실패");

                        } else {
                            //로그인 성공

                            //사용자 정보 요청
                            requestMe();
                        }
                        return null;
                    });
                }
            }
        });


    }

    private void requestMe() {

        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                show_fail("사용자 정보 요청 실패" + meError);
            } else {
                //사용자 아이디 : user.getId();¿

                Account kakaoAccount = user.getKakaoAccount();
                if (kakaoAccount != null) {
                    //프로필
                    Profile profile = kakaoAccount.getProfile();
                    Log.i(TAG, "test");
                    if (profile != null) {
                        //nickname : profile.getNickname()
                        // profile image: profile.getProfileImageUrl()
                        // thumbnail image: profile.getThumbnailImageUrl()

                        String email = kakaoAccount.getEmail();
                        Log.i(TAG, email);
                        if (email != null) {
                            //이메일 확인 완료
                            show_success(email);

                        } else if (kakaoAccount.getEmailNeedsAgreement()) {
                            //동의 요청 후 이메일 획득 가능
                            //단 선택 동의로 설정 되어 있다면 서비스 이용 시나리오 상에서 반드시 필요한 경우에만 요청해야한다.
                            //필요한 동의 항목의 scope ID(개발자 사이트 해당 동의 항목 설정에서 확인 가능)
                            List<String> scopes = Arrays.asList("account_email");

                            // 사용자 동의 요청
                            UserApiClient.getInstance().loginWithNewScopes(this, scopes, (ttt, error) -> {
                                if (error != null) {
                                    requestMe();
                                } else {

                                }
                                return null;
                            });
                        } else {
                            //이메일 획득 불가
                        }
                    } else {
                        //profile 회득 불가
                    }

                }
            }
            return null;
        });
    }

    private void show_fail(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 실패");
        builder.setMessage(message);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "예를 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    private void show_success(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 실패");
        builder.setMessage(message);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "예를 선택했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    //메인스레드에서 데이터베이스에 접근할 수 없으므로 AsyncTask를 사용하도록 한다.
    public static class DbAsyncTask extends AsyncTask<String, Void, Void> {
        private UserDao userdao;
        private Context context;
        private CheckBox chk_login_status;

        User user;
        public DbAsyncTask(UserDao userdao, Context context, CheckBox chk_login_status) {
            this.userdao = userdao;
            this.context = context;
            this.chk_login_status = chk_login_status;
        }

        @Override //백그라운드작업(메인스레드 X)
        protected Void doInBackground(String... data) {
            //추가만하고 따로 SELECT문을 안해도 라이브데이터로 인해
            //getAll()이 반응해서 데이터를 갱신해서 보여줄 것이다,  메인액티비티에 옵저버에 쓴 코드가 실행된다. (라이브데이터는 스스로 백그라운드로 처리해준다.)
            List<User> users = userdao.getUserEmail(data[0], data[1]);
            Log.i("JUN", users.size() + "");

            if (users.size() >= 1){
                user = users.get(0);
            }else{
                user = null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (user != null) {
                Util.loginUser = user;
                if (chk_login_status.isChecked()){
                    Util.setString(context, "email",user.getEmail());
                    Util.setString(context, "password", user.getPassword());
                }else{
                    Util.setString(context, "email","");
                    Util.setString(context, "password", "");
                }

                Log.i("JUN","LOGIN 성공");
//                Util.loginUser = user;
                Intent intent = new Intent(context, TestActivity.class);
                context.startActivity(intent);
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("로그인 실패");
                builder.setMessage("이메일 또는 패스워드가 알맞지 않습니다.");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //    private void loadStores() {
//        StoreService.getPosts().enqueue(new Callback<List<StoreDto>>() {
//            @Override
//            public void onResponse(Call<List<StoreDto>> call,
//                                   Response<List<StoreDto>> response) {
//                if (response.isSuccessful()) {
//                    // response.body()
//                    // response.body()에서 넘어오는 데이터로 Adapter에 뿌려주기
//                } else {
//                    Log.d("REST FAILED MESSAGE", response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<StoreDto>> call, Throwable t) {
//                Log.d("REST ERROR!", t.getMessage());
//            }
//
//        }
//    }
}
