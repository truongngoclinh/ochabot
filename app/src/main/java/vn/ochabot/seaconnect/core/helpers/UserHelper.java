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
        return "Lê Minh Nhựt";
    }

    public static String getAvatarUrl(String name) {
        if (getUserName().equals("Hoàng Đức Thiện")) {
            return "https://static.tvgcdn.net/mediabin/showcards/celebs/c/thumbs/chris-evans_144489_768x1024.png";
        }
        return "https://pixel.nymag.com/imgs/fashion/daily/2016/04/26/26-leonardo-dicaprio.w330.h412.jpg";
    }

}
