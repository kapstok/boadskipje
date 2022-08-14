package be.allersma.boadskipje;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {
    public static final int CAMERA_CODE = 100;
    public static final int RECORD_AUDIO = 101;
    public static final int WRITE_EXTERNAL_STORAGE = 102;
    public static final int READ_EXTERNAL_STORAGE = 103;

    private Permissions() {} // Singleton

    public static boolean checkPermission(Activity activity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(activity, new String[] { permission }, requestCode);
            return checkPermission(activity, permission, requestCode);
        }
        return true;
    }
}
