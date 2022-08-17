package be.allersma.boadskipje.ui.foardiel;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.allersma.boadskipje.EAN13CodeBuilder;
import be.allersma.boadskipje.R;
import be.allersma.boadskipje.databinding.FragmentFoardielBinding;
import be.allersma.boadskipje.persistence.FoardielRegister;
import be.allersma.boadskipje.ui.Util;

import java.util.Map;

public class FoardielFragment extends Fragment {
    private FragmentFoardielBinding binding;
    private FoardielRegister foardielRegister;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoardielBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        foardielRegister = new FoardielRegister();
        LinearLayout foardielList = root.findViewById(R.id.foardiel_list);

        for (Map.Entry<String, String> entry : foardielRegister.getRegister(root.getContext()).entrySet()) {
            LinearLayout item = createNewEntry(foardielList.getContext(), entry.getKey(), entry.getValue());
            foardielList.addView(item);
        }

        return root;
    }

    public LinearLayout createNewEntry(Context context, String name, String code) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(getResources().getColor(R.color.white));
        int dpLayout = Util.dpToPixels(context, 30);
        layout.setPadding(dpLayout, dpLayout, dpLayout, dpLayout);


        TextView nameView = new TextView(context);
        nameView.setText(name);
        nameView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        nameView.setGravity(Gravity.CENTER);

        int dpBarCode = Util.dpToPixels(context, 20);
        TextView barCode = new TextView(context);
        barCode.setPadding(dpBarCode, dpBarCode, dpBarCode, dpBarCode);
        barCode.setTextSize(120);
        barCode.setTextColor(getResources().getColor(R.color.black));
        barCode.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        barCode.setGravity(Gravity.CENTER);

        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/ean13.ttf");
        barCode.setTypeface(font);
        EAN13CodeBuilder codeBuilder = new EAN13CodeBuilder(code);
        barCode.setText(codeBuilder.getCode());

        layout.addView(nameView);
        layout.addView(barCode);
        return layout;
    }
}