package be.allersma.boadskipje.persistence;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import be.allersma.boadskipje.Permissions;
import be.allersma.boadskipje.ui.PermissionActivity;

import java.io.*;

public class Persistence {
    protected String readFileOnInternalStorage(Context context, String fileName) {
        String result = "";

        try {
            InputStream stream = context.openFileInput(fileName);

            if (stream == null) {
                Toast.makeText(context, "Koe gegevens net ynlade yn cache", Toast.LENGTH_SHORT).show();
                return result;
            }

            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader buf = new BufferedReader(reader);
            String line;
            StringBuilder builder = new StringBuilder();

            while((line = buf.readLine()) != null) {
                builder.append(line).append("\n");
            }

            stream.close();
            result = builder.toString();
            return result.length() > 0 ? result.substring(0, result.length() - 1) : result;
        } catch (IOException e) {
            Toast.makeText(context, "Koe gegevens net ynlade yn cache", Toast.LENGTH_SHORT).show();
            Log.e("Exception", e.getMessage());
        }

        return result;
    }

    protected void writeFileOnInternalStorage(Context context, String fileName, String content) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            Toast.makeText(context, "Koe gegevens net opslaan yn cache", Toast.LENGTH_SHORT).show();
            Log.e("Exception", e.getMessage());
        }
    }

    protected void allPermissionsGranted(Activity activity) {
        boolean necessaryPermissionsGranted;
        necessaryPermissionsGranted = Permissions.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        necessaryPermissionsGranted &= Permissions.checkPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (!necessaryPermissionsGranted) {
            Intent intent = new Intent(activity, PermissionActivity.class);
            activity.startActivity(intent);
        }
    }
}
