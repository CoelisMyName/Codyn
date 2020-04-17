package com.coel.codyn.collector;

import android.app.Activity;

import java.util.List;
import java.util.Vector;

public class ActivityCollector {
    private static List<Activity> activities = new Vector<>(6);

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing())
                activity.finish();
        }
    }
}
