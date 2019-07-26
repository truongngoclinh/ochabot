package vn.ochabot.seaconnect.core.helpers;

import android.os.Build;
import android.util.Log;

/**
 * @author Thien.
 */
public class UserHelper {

    public static String getUserName() {
        long deviceId = (Build.MODEL + Build.BRAND + Build.MANUFACTURER).hashCode();
        Log.e("deviceKey", "" + deviceId);
        if (deviceId == -1835170376 || deviceId == 326176262) {
            return "Hoàng Đức Thiện";
        }
        return "Le Minh Nhut";
    }

}
