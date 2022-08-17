package be.allersma.boadskipje.ui;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.content.res.AppCompatResources;
import be.allersma.boadskipje.Permissions;
import be.allersma.boadskipje.R;

public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        addLogicToAllButtons(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        addLogicToAllButtons(true);
    }

    private void addLogicToAllButtons(boolean invalidate) {
        TextView readExternalStorage = findViewById(R.id.read_external_storage_permission_button);
        addLogicToButton(readExternalStorage,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Permissions.READ_EXTERNAL_STORAGE
        );
        if (invalidate) readExternalStorage.invalidate();

        TextView writeExternalStorage = findViewById(R.id.write_external_storage_permission_button);
        addLogicToButton(writeExternalStorage,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Permissions.WRITE_EXTERNAL_STORAGE
        );
        if (invalidate) writeExternalStorage.invalidate();

        TextView useCamera = findViewById(R.id.use_camera_permission_button);
        addLogicToButton(useCamera,
                Manifest.permission.CAMERA,
                Permissions.CAMERA_CODE
        );
        if (invalidate) useCamera.invalidate();

        TextView recordAudio = findViewById(R.id.record_audio_permission_button);
        addLogicToButton(recordAudio,
                Manifest.permission.RECORD_AUDIO,
                Permissions.RECORD_AUDIO
        );
        if (invalidate) recordAudio.invalidate();
    }

    private void addLogicToButton(TextView button, String manifestPermission, int permission) {
        if (Permissions.checkPermission(this, manifestPermission)) {
            button.setText(R.string.permission_granted);
            button.setTextColor(getResources().getColor(R.color.black));
            button.setOnClickListener(listener -> { /* Do nothing */ });
            button.setBackgroundColor(getResources().getColor(R.color.background));
        } else {
            button.setTextColor(getResources().getColor(R.color.white));
            int dp = Util.dpToPixels(this, 10);
            button.setPadding(0, dp, 0, dp);
            Drawable background = AppCompatResources.getDrawable(this, R.drawable.listitem);
            button.setBackground(background);
            button.setOnClickListener(listener -> {
                Permissions.requestPermission(this, manifestPermission, permission);
            });
        }
    }
}