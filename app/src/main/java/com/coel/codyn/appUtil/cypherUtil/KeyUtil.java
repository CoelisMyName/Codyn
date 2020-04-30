package com.coel.codyn.appUtil.cypherUtil;

import android.util.Log;

import com.coel.codyn.R;
import com.coel.codyn.room.Key;

public interface KeyUtil {

    static String type2Str(int i) {
        switch (i) {
            case Key.AES_INT:
                return Key.AES_STR;

            case Key.ECC_INT:
                return Key.ECC_STR;

            case Key.RSA_INT:
                return Key.RSA_STR;

            default:
                Log.d("Function", "key_Int2Str reach default");
        }
        return "";
    }

    static String attrStr(int i) {
        switch (i) {
            case Key.PRIVATE_KEY:
                return Key.PRIVATE_KEY_STR;
            case Key.PUBLIC_KEY:
                return Key.PUBLIC_KEY_STR;
            case Key.SYMMETRIC_KEY:
                return Key.SYMMETRIC_KEY_STR;
        }
        return "";
    }

    static int type2Rid(int i) {
        switch (i) {
            case Key.AES_INT:
                return R.string.AES;

            case Key.ECC_INT:
                return R.string.ECC;

            case Key.RSA_INT:
                return R.string.RSA;

            default:
                Log.d("Function", "key_type2Rid reach default");
        }
        return -1;
    }

    static boolean isSymmetric(int i) {
        switch (i) {
            case Key.AES_INT:
                return true;
            default:
                return false;
        }

    }
}
