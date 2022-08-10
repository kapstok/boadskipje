package be.allersma.boadskipje.ui.scancode;

import android.widget.Button;
import androidx.camera.view.PreviewView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScanCodeViewModel extends ViewModel {

//    private final MutableLiveData<String> mText;
    private final MutableLiveData<Button> imageCaptureButton;
    private final MutableLiveData<Button> videoCaptureButton;
    private final MutableLiveData<PreviewView> previewView;

    public ScanCodeViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is dashboard fragment");
        imageCaptureButton = new MutableLiveData<>();
        videoCaptureButton = new MutableLiveData<>();
        previewView = new MutableLiveData<>();
    }

//    public LiveData<String> getText() {
//        return mText;
//    }

    public LiveData<PreviewView> getPreviewView() {
        return previewView;
    }
}