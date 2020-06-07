package com.coel.codyn;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityDisplayQR extends AppCompatActivity {
    public static final String EXTRA_QR_IMAGE = "com.coel.codyn.EXTRA_QR_IMAGE";
    public static final String BUNDLE = "com.coel.codyn.BUNDLE";
    public static Bitmap bm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayqr);

        ImageView imageView = findViewById(R.id.qr);
        /*
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        assert bundle != null;
        Bitmap bitmap = (Bitmap) bundle.getParcelable(EXTRA_QR_IMAGE);
        */
        imageView.setImageBitmap(bm);
        Log.d("ActivityDisplayQR","display success");
    }
}
