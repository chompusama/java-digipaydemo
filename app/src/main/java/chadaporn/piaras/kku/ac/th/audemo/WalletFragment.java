package chadaporn.piaras.kku.ac.th.audemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;


public class WalletFragment extends Fragment{

    View view;
    ImageButton profileBtn;
    Button historyBtn, registerBtn, transferBtn, paymentBtn;
    String resUrl, resCode, resFirstnameW,resLastnameW, resCitizenIdW, resPhone, resLink, _id, token;
    JSONObject Jobject;
    SharedPreferences prefs;
    TextView tv;
    RelativeLayout linkAccountLayout, walletContentLayout;
    GifImageView gifLoadingAnimated;

    private ZXingScannerView zXingScannerView;
    private ZXingScannerView mScannerView;

    String urlRegister = "https://dev-villa.digio.co.th:8448/digipayV2/api/v1/thirdParties/webview/register";
    String urlTransfer = "https://dev-villa.digio.co.th:8448/digipayV2/api/v1/thirdParties/webview/transfer";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        profileBtn = (ImageButton) view.findViewById(R.id.btn_profile);
        paymentBtn = (Button) view.findViewById(R.id.btn_payment);
        historyBtn = (Button) view.findViewById(R.id.btn_history);
        transferBtn = (Button) view.findViewById(R.id.btn_transfer);
        registerBtn = (Button) view.findViewById(R.id.btn_register_wallet);
        tv = (TextView) view.findViewById(R.id.tv_mywallet);
        linkAccountLayout = (RelativeLayout) view.findViewById(R.id.layout_link_account);
        walletContentLayout = (RelativeLayout) view.findViewById(R.id.layout_wallet_content);

        gifLoadingAnimated = (GifImageView) view.findViewById(R.id.loading_animated_wallet);
        gifLoadingAnimated.setGifImageResource(R.drawable.loading_4);

        prefs = this.getActivity().getSharedPreferences("mypref", MODE_PRIVATE);

//        mScannerView = new ZXingScannerView(getActivity());

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onClickProfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickHistory();
            }
        });

//        registerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    onClickRegister();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickTransfer();
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPayment();
            }
        });

        init();

        return view;
    }

    /** initial data */
    public void init(){

        _id = SharedPrefManager.getInstans(getActivity().getApplicationContext()).getUserId();
        token = SharedPrefManager.getInstans(getActivity().getApplicationContext()).getToken();

        /** request service <POST> */
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("_id", _id)
                .build();

        Request request = new Request.Builder()
                .url("https://d1a983ed.ngrok.io/v2/user")
                .post(formBody)
                .header("auth-token", token)
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
                        Jobject = new JSONObject(jsonData).getJSONObject("data");
                        resFirstnameW = Jobject.getString("first_name");
                        resLastnameW = Jobject.getString("last_name");
                        resCitizenIdW = Jobject.getString("citizen_id");
                        resPhone = Jobject.getString("phone_number");
                        resLink = Jobject.getString("link_account");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            tv.setText("Hi, " + resFirstnameW);

                            SharedPrefManager.getInstans(getActivity().getApplicationContext()).userData(resFirstnameW, resLastnameW, resCitizenIdW, resPhone);

                            walletContentLayout.setVisibility(View.VISIBLE);
                            gifLoadingAnimated.setVisibility(View.INVISIBLE);

//                            if (resLink.equals("false")) {
//                                walletContentLayout.setVisibility(View.VISIBLE);
//                                gifLoadingAnimated.setVisibility(View.INVISIBLE);
//                            }
//                            else {
//                                walletContentLayout.setVisibility(View.VISIBLE);
//                                gifLoadingAnimated.setVisibility(View.INVISIBLE);
//                            }
                        }
                    });
                }
            }
        });
    }

    public void onClickProfile() throws IOException {

    }

    public void onClickHistory() {

    }

    public void onClickPayment() {
        startActivity(new Intent(getActivity().getApplicationContext(), ScanqrcodeActivity.class));
    }

    public void onClickTransfer() {
        /** request service <POST> */
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("payee_user_id", "")
                .add("amount", "")
                .add("user_id", "")
                .build();

        Request request = new Request.Builder()
                .url(urlTransfer)
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resCode.equals("0000")) {
                                try {
                                    resUrl = Jobject.getString("url");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(getActivity().getApplicationContext(), WebviewActivity.class);
                                intent.putExtra("url", resUrl);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

//    public void onClickRegister() throws IOException {
//
//        /** request service <POST> */
//        OkHttpClient client = new OkHttpClient();
//
//        RequestBody formBody = new FormBody.Builder()
//                .build();
//
//        Request request = new Request.Builder()
//                .url(urlRegister)
//                .post(formBody)
//                .header("x-api-id", "hfObtxOYf00174")
//                .header("x-api-key", "35215c55-aaee-4284-bf2c-65688f624b4a")
//                .build();
//
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    final String jsonData = response.body().string();
//
//                    try {
//                        Jobject = new JSONObject(jsonData);
//                        resCode = Jobject.getString("res_code");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (resCode.equals("0000")) {
//                                try {
//                                    resUrl = Jobject.getString("url");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                Intent intent = new Intent(getActivity().getApplicationContext(), WebviewActivity.class);
//                                intent.putExtra("url", resUrl);
//                                startActivity(intent);
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }

}
