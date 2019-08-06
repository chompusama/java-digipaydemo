package chadaporn.piaras.kku.ac.th.audemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;
import com.jaeger.library.StatusBarUtil;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanqrcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        StatusBarUtil.setTranslucentForImageView(ScanqrcodeActivity.this, 5, null);

//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(ScanqrcodeActivity.this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
//        }

    }

    @Override
    public void handleResult(Result result) {
//        ScanqrcodeActivity.this.setContentView(R.layout.activity_scanqrcode);
        String resultString = result.getText();
        Toast.makeText(ScanqrcodeActivity.this, "QR code : " + resultString,
                    Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();

        ScannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();

        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }

}
