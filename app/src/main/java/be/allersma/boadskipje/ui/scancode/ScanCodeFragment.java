package be.allersma.boadskipje.ui.scancode;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import be.allersma.boadskipje.BarcodeAnalyzer;
import be.allersma.boadskipje.Permissions;
import be.allersma.boadskipje.databinding.FragmentScanCodeBinding;
import be.allersma.boadskipje.ui.PermissionActivity;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanCodeFragment extends Fragment {

    private FragmentScanCodeBinding binding;
    private ExecutorService cameraExecutor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScanCodeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Begin my code
        allPermissionsGranted();
        startCamera();

        cameraExecutor = Executors.newSingleThreadExecutor();
        // End my code

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }

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
        boolean necessaryPermissionsGranted;
        necessaryPermissionsGranted = Permissions.checkPermission(getActivity(), Manifest.permission.CAMERA);

        if (!necessaryPermissionsGranted) {
            Intent intent = new Intent(getActivity(), PermissionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}