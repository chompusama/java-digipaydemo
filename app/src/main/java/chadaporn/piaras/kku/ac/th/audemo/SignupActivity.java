package chadaporn.piaras.kku.ac.th.audemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {

    EditText firstnameEdt, lastnameEdt, phoneNumberEdt, citizenIdEdt, passwordEdt;
    Button signupButton;
    String sFirstname, sLastname, sPhoneNumber, sCitizenId, sPassword;
    TextView mNoticeSignup;
    String resCode;
    JSONObject Jobject;
    GifImageView gifLoadingAnimated;
    String ngrok = "https://d1a983ed.ngrok.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstnameEdt = (EditText) findViewById(R.id.edt_first_name);
        lastnameEdt = (EditText) findViewById(R.id.edt_last_name);
        phoneNumberEdt = (EditText) findViewById(R.id.edt_phone_number);
        citizenIdEdt = (EditText) findViewById(R.id.edt_citizen_id);
        passwordEdt = (EditText) findViewById(R.id.edt_password);
        signupButton = (Button) findViewById(R.id.btn_signup);
        mNoticeSignup = (TextView) findViewById(R.id.tv_signup_notice);
        gifLoadingAnimated = (GifImageView) findViewById(R.id.loading_animated_signup);
        gifLoadingAnimated.setGifImageResource(R.drawable.loading_4);

        StatusBarUtil.setTranslucentForImageView(SignupActivity.this, 5, null);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createAccount();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void onClickSignin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public  void createAccount() throws IOException {

        sFirstname = firstnameEdt.getText().toString();
        sLastname = lastnameEdt.getText().toString();
        sCitizenId = citizenIdEdt.getText().toString();
        sPhoneNumber = phoneNumberEdt.getText().toString();
        sPassword = passwordEdt.getText().toString();


        if (sPhoneNumber.matches("") || sPassword.matches("") ||
            sFirstname.matches("") || sLastname.matches("") || sCitizenId.matches("")){
            mNoticeSignup.setText("please enter all information");
        }
        else {

            gifLoadingAnimated.setVisibility(View.VISIBLE);

            /** request service <POST> */
            OkHttpClient client = new OkHttpClient();
            String url = ngrok + "/register";

            RequestBody formBody = new FormBody.Builder()
                    .add("first_name", sFirstname)
                    .add("last_name", sLastname)
                    .add("phone_number", sPhoneNumber)
                    .add("citizen_id", sCitizenId)
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SignupActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (resCode.equals("0000")) {
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    gifLoadingAnimated.setVisibility(View.INVISIBLE);
                                }
                                else if (resCode.equals("0001")) {
                                    mNoticeSignup.setText("has this account already");
                                    gifLoadingAnimated.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                }

            });

        }

    }
}
