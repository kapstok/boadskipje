package be.allersma.boadskipje;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import be.allersma.boadskipje.persistence.BarcodeRegister;
import be.allersma.boadskipje.persistence.BoadskipjeList;
import be.allersma.boadskipje.ui.AddCodeActivity;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
    Activity activity;
    BarcodeRegister register;

    public BarcodeAnalyzer(Activity activity) {
        this.activity = activity;
        this.register = new BarcodeRegister();
    }

    @Override
    @androidx.camera.core.ExperimentalGetImage
    public void analyze(@NonNull @NotNull ImageProxy imageProxy) {
        Image mediaImage = imageProxy.getImage();
        if (mediaImage != null) {
            InputImage image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
            scanBarCode(image);
            imageProxy.close();
        }
    }

    private void scanBarCode(InputImage image) {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_EAN_13)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient();

        Task<List<Barcode>> result = scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    for (Barcode barcode : barcodes) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(activity, R.raw.bliep);
                        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
                        mediaPlayer.start();
                        String value = barcode.getRawValue();
                        Map<String, String> register = this.register.getRegister(activity);

                        if (register.containsKey(value)) {
                            BoadskipjeList.addBoadskip(activity, register.get(value));
                        } else {
                            Intent intent = new Intent(activity, AddCodeActivity.class);
                            intent.putExtra("code", value);
                            activity.startActivity(intent);
                        }
                    }
                }).addOnFailureListener(e -> {
                    // No-op
                });
    }
}
