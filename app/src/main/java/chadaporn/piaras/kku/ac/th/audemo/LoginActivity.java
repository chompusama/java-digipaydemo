package chadaporn.piaras.kku.ac.th.audemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText phoneEdt, passwordEdt;
    TextView mNoticeTextview, signupBtn;
    String sPhoneNumber, sPassword;
    String resCode, resToken, resID;
    JSONObject Jobject;
    Button loginBtn;
    GifImageView gifLoadingAnimated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneEdt = (EditText) findViewById(R.id.edt_login_phone);
        passwordEdt = (EditText) findViewById(R.id.edt_login_password);
        mNoticeTextview = (TextView) findViewById(R.id.tv_notice);
        loginBtn = (Button) findViewById(R.id.btn_login);
        signupBtn = (TextView) findViewById(R.id.btn_tv_signup);
        gifLoadingAnimated = (GifImageView) findViewById(R.id.loading_animated);
        gifLoadingAnimated.setGifImageResource(R.drawable.loading_5);

        StatusBarUtil.setTranslucentForImageView(LoginActivity.this, 5, null);

        //Check if login already
        if (SharedPrefManager.getInstans(this).isLogin()){
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            return;
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    logIn();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    signUp();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void logIn() throws IOException {

        sPhoneNumber = phoneEdt.getText().toString();
        sPassword = passwordEdt.getText().toString();


        if (sPhoneNumber.matches("") || sPassword.matches("")){
            mNoticeTextview.setText("please fill all information");
        }
        else {
            gifLoadingAnimated.setVisibility(View.VISIBLE);

            /** request service <POST> */
            OkHttpClient client = new OkHttpClient();
            String url = "https://d1a983ed.ngrok.io/v2/login/" + sPhoneNumber;

            RequestBody formBody = new FormBody.Builder()
                    .add("password", sPassword)
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String jsonData = response.body().string();

                        try {
                            Jobject = new JSONObject(jsonData);
                            resCode = Jobject.getString("res_code");
                            resToken = Jobject.getString("token");
                            resID = Jobject.getString("_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (resCode.equals("0000")) {

                                    gifLoadingAnimated.setVisibility(View.INVISIBLE);

                                    Log.d("555", resID);

                                    //Shared Pref
                                    SharedPrefManager.getInstans(getApplicationContext()).userLogin(resID, resToken);

                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                                else if (resCode.equals("0002")) {
                                    gifLoadingAnimated.setVisibility(View.INVISIBLE);
                                    mNoticeTextview.setText("This phone number has not been registered.");
                                }
                                else if (resCode.equals("0003")) {
                                    gifLoadingAnimated.setVisibility(View.INVISIBLE);
                                    mNoticeTextview.setText("Password you entered is incorrect.");
                                }
                            }
                        });
                    }
                }

            });

        }
    }

    public void signUp() throws IOException {
        gifLoadingAnimated.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
        dialog.setCancelable(false);
        dialog.setTitle("");
        dialog.setMessage("Do you really want to exit?" );
        dialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
                System.exit(0);
            }
        });
        dialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }
}
