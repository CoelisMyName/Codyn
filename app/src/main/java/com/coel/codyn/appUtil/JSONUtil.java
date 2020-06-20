package com.coel.codyn.appUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
    public static final String KEY_TYPE = "KEY_TYPE";
    public static final String KEY_ATTR = "KEY_ATTR";
    public static final String KEY_VALUE = "KEY_VALUE";

    public static JSONObject createKeyJSON(int t, int a, String k) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(KEY_TYPE, t);
        json.put(KEY_ATTR, a);
        json.put(KEY_VALUE, k);
        return json;
    }

    public static String JSONString(JSONObject json) throws JSONException {
        return json.toString(1);
    }

}
