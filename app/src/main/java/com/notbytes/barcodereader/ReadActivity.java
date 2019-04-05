package com.notbytes.barcodereader;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.util.List;

public class ReadActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener {

    private TextView result;
    private Button copy;
    private Button open;
    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        result = findViewById(R.id.result);
        copy = findViewById(R.id.copy);
        open = findViewById(R.id.open);

        open.setEnabled(false);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!value.equals("")) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("", value);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(view.getContext(), R.string.copy_t, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(view.getContext(), R.string.error, Toast.LENGTH_LONG).show();
                }
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
                    startActivity(myIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(view.getContext(), R.string.open_t, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addBarcodeReaderFragment();
            }
        }, 300);
    }

    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment = BarcodeReaderFragment.newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onScanned(Barcode barcode) {
        value = barcode.rawValue;
        result.setText(value);
        open.setEnabled(URLUtil.isHttpUrl(value) || URLUtil.isHttpsUrl(value));
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, R.string.permission, Toast.LENGTH_LONG).show();
    }
}
