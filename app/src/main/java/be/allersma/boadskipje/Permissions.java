package be.allersma.boadskipje;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {
    public static final int CAMERA_CODE = 100;
    public static final int WRITE_EXTERNAL_STORAGE = 102;
    public static final int READ_EXTERNAL_STORAGE = 103;

    private Permissions() {} // Singleton

    public static boolean checkPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_DENIED;
    }

    public static boolean checkPermission(Activity activity, String permission, int foobar) {
        return ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_DENIED;
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
    }
}
