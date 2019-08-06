package chadaporn.piaras.kku.ac.th.audemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    String resUrl, resCode, resFirstName, resLastname, resCitizenid,
            first_name, last_name, citizen_id, phone, _id, token;
    JSONObject Jobject , myJobject;
    String urlRegister = "https://dev-villa.digio.co.th:8448/digipayV2/api/v1/thirdParties/webview/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StatusBarUtil.setTranslucentForImageView(RegisterActivity.this, 5, null);

        registerButton = (Button) findViewById(R.id.btn_register);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    register();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        first_name = SharedPrefManager.getInstans(getApplicationContext()).getFirstname();
        last_name = SharedPrefManager.getInstans(getApplicationContext()).getLastname();
        citizen_id = SharedPrefManager.getInstans(getApplicationContext()).getCitizenId();
        phone = SharedPrefManager.getInstans(getApplicationContext()).getPhone();
    }

    public void register() throws IOException {

        Log.d("5555", first_name);
        Log.d("6666", last_name);
        Log.d("7777", citizen_id);

        /** request service <POST> */
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("first_name", first_name)
                .add("last_name", last_name)
                .add("citizen_id", citizen_id)
                .add("phone_no", phone)
                .build();

        Request request = new Request.Builder()
                .url(urlRegister)
                .post(formBody)
                .header("x-api-id", "hfObtxOYf00174")
                .header("x-api-key", "35215c55-aaee-4284-bf2c-65688f624b4a")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String jsonData = response.body().string();

                    try {
                        Jobject = new JSONObject(jsonData);
                        resCode = Jobject.getString("res_code");
                        Log.d("66666666666666666", resCode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resCode.equals("0000")) {
                                try {
                                    resUrl = Jobject.getString("url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(RegisterActivity.this.getApplicationContext(), WebviewActivity.class);
                                intent.putExtra("url", resUrl);
                                startActivity(intent);
                            }
                            else {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                                dialog.setCancelable(false);
                                dialog.setTitle("Alert");
                                dialog.setMessage("registered" );
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                                final AlertDialog alert = dialog.create();
                                alert.show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void onClickSkip(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
