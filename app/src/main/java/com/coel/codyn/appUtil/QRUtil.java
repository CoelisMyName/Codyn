package com.coel.codyn.appUtil;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRUtil {
    private static int IMAGE_HALFWIDTH = 50;//宽度值，影响中间图片大小
    private static BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

    public static Bitmap create(@NonNull String contents) {
        try {
            return barcodeEncoder.encodeBitmap(contents, BarcodeFormat.QR_CODE, 500, 500);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap create(@NonNull String contents, int size) {
        try {
            return barcodeEncoder.encodeBitmap(contents, BarcodeFormat.QR_CODE, size, size);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
