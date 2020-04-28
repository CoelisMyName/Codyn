package com.coel.codyn.appUtil;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class SystemUtil {
    private static ClipboardManager cm;

    public static void setClipboard(Context context, String str) {
        if (cm == null) {
            cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        }
        ClipData mClipData = ClipData.newPlainText("Label", str);
        cm.setPrimaryClip(mClipData);
    }
}
