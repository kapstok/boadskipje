package be.allersma.boadskipje.ui;

import android.content.Intent;
import android.view.Display;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import be.allersma.boadskipje.BoadskipjeList;
import be.allersma.boadskipje.R;
import be.allersma.boadskipje.persistence.BarcodeRegister;
import com.google.android.material.textfield.TextInputLayout;

public class AddCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_code);
        String scannedCode = getIntent().getStringExtra("code");
        TextView codeView = findViewById(R.id.code);
        codeView.setText(scannedCode);
        TextView submitButton = findViewById(R.id.submitCode);
        submitButton.setOnClickListener(view -> {
            TextInputLayout textInputLayout = findViewById(R.id.textInputCode);
            EditText newItemElement = textInputLayout.getEditText();

            if (newItemElement == null) {
                finish();
                return;
            }

            if (newItemElement.getText() != null && !newItemElement.getText().toString().trim().isEmpty()) {
                String boadskip = newItemElement.getText().toString();
                BarcodeRegister register = new BarcodeRegister();
                register.addToRegister(this, scannedCode, boadskip);
                BoadskipjeList.addBoadskip(boadskip);
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}