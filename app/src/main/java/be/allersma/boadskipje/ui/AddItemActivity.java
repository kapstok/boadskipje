package be.allersma.boadskipje.ui;

import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import be.allersma.boadskipje.BoadskipjeList;
import be.allersma.boadskipje.R;
import com.google.android.material.textfield.TextInputLayout;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        TextView submitButton = findViewById(R.id.submitBoadskip);
        submitButton.setOnClickListener(view -> {
            TextInputLayout textInputLayout = findViewById(R.id.textInput);
            EditText newItemElement = textInputLayout.getEditText();

            if (newItemElement == null) {
                finish();
                return;
            }

            if (newItemElement.getText() != null && !newItemElement.getText().toString().trim().isEmpty()) {
                BoadskipjeList.addBoadskip(newItemElement.getText().toString());
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}