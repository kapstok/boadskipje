package be.allersma.boadskipje.ui.scancode;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.camera.core.*;
import androidx.camera.core.impl.PreviewConfig;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.Recording;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.Person;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import be.allersma.boadskipje.BarcodeAnalyzer;
import be.allersma.boadskipje.MainActivity;
import be.allersma.boadskipje.databinding.FragmentScanCodeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanCodeFragment extends Fragment {

    private FragmentScanCodeBinding binding;

    // CameraX
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    private Recording recording;
    private ExecutorService cameraExecutor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScanCodeViewModel scanCodeViewModel =
                new ViewModelProvider(this).get(ScanCodeViewModel.class);

        binding = FragmentScanCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Begin my code
        allPermissionsGranted();
        startCamera();

        // Set up the listeners for take photo and video capture buttons
        binding.imageCaptureButton.setOnClickListener(view -> takePhoto());
        binding.videoCaptureButton.setOnClickListener(view -> captureVideo());

        cameraExecutor = Executors.newSingleThreadExecutor();

        // End my code

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

    private void takePhoto() {}

    private void captureVideo() {}

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> listenableFuture = ProcessCameraProvider.getInstance(getActivity());

        listenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider processCameraProvider = listenableFuture.get();
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());

                ImageAnalysis analysis = new ImageAnalysis.Builder().build();
                analysis.setAnalyzer(cameraExecutor, new BarcodeAnalyzer(getActivity()));

                processCameraProvider.unbindAll();
                processCameraProvider.bindToLifecycle(getActivity(), cameraSelector, preview, analysis);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, ContextCompat.getMainExecutor(getActivity()));
    }

    private void allPermissionsGranted() {
        checkPermission(Manifest.permission.CAMERA, MainActivity.CAMERA_CODE);
        checkPermission(Manifest.permission.RECORD_AUDIO, MainActivity.RECORD_AUDIO);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, MainActivity.WRITE_EXTERNAL_STORAGE);
    }

    private boolean checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] { permission }, requestCode);
            return checkPermission(permission, requestCode);
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}