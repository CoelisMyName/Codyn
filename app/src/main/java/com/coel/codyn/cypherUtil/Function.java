package com.coel.codyn.cypherUtil;

import android.util.Log;

import com.coel.codyn.R;
import com.coel.codyn.room.Key;
import com.coel.codyn.viewmodel.AdEdVM;
import com.coel.codyn.viewmodel.MainVM;

public interface Function {

    static String key_type2Str(int i) {
        switch (i) {
            case Key.AES:
                return AdEdVM.KEY_AES;

            case Key.ECC:
                return AdEdVM.KEY_ECC;

            case Key.RSA:
                return AdEdVM.KEY_RSA;

            default:
                Log.d("Function", "key_type2Rid reach default");
        }
        return "";
    }

    static String key_attr2String(int i) {
        switch (i) {
            case MainVM.PRIVATE_KEY:
                return "私钥";
            case MainVM.PUBLIC_KEY:
                return "私钥";
            case MainVM.SYMMETRIC_KEY:
                return "对称";
        }
        return "";
    }

    static int key_type2Rid(int i) {
        switch (i) {
            case Key.AES:
                return R.string.AES;

            case Key.ECC:
                return R.string.ECC;

            case Key.RSA:
                return R.string.RSA;

            default:
                Log.d("Function", "key_type2Rid reach default");
        }
        return -1;
    }

    static int Rid2key_type(int i) {
        switch (i) {
            case R.string.AES:
                return Key.AES;

            case R.string.ECC:
                return Key.ECC;

            case R.string.RSA:
                return Key.RSA;

            default:
                Log.d("Function", "Rid2key_type reach default");
        }
        return -1;
    }

    static boolean key_type_is_symmetric(int i) {
        switch (i) {
            case Key.AES:
                return true;
            case Key.ECC:
                return false;
            case Key.RSA:
                return false;
            default:
                return false;
        }

    }
}
