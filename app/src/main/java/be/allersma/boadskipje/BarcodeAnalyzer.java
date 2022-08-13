package be.allersma.boadskipje;

import android.content.Context;
import android.media.Image;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
    Context context;

    public BarcodeAnalyzer(Context context) {
        this.context = context;
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
                        String value = barcode.getRawValue();
                        BoadskipjeList.addBoadskip(value);
                        Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    // No-op
                });
    }
}
