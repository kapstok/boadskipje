package be.allersma.boadskipje.ui.list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import be.allersma.boadskipje.BoadskipjeList;
import be.allersma.boadskipje.R;
import be.allersma.boadskipje.databinding.FragmentListBinding;
import be.allersma.boadskipje.ui.AddItemActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListViewModel listViewModel =
                new ViewModelProvider(this).get(ListViewModel.class);

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LinearLayout itemList = root.findViewById(R.id.item_list);
        FloatingActionButton addItemButton = root.findViewById(R.id.add_item);
        addItemButton.setOnClickListener(this::addNewItem);

        for (Map.Entry<String, Integer> entry : BoadskipjeList.getBoadskippen().entrySet()) {
            LinearLayout item = createNewEntry(itemList.getContext(), entry.getKey());
            itemList.addView(item);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addNewItem(View view) {
        Intent intent = new Intent(getActivity(), AddItemActivity.class);
        startActivity(intent);
    }

    public LinearLayout createNewEntry(Context context, String boadskip) {
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);

        TextView minButton = createClickableText(context, "-", view -> {
            BoadskipjeList.removeBoadskip(boadskip);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, this.getClass(), null).commit();
        });
        TextView plusButton = createClickableText(context, "+", view -> {
            BoadskipjeList.addBoadskip(boadskip);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, this.getClass(), null).commit();
        });
        TextView message = createMessage(context, boadskip);
        layout.addView(minButton);
        layout.addView(plusButton);
        layout.addView(message);

        return layout;
    }

    private TextView createClickableText(Context context, String text, View.OnClickListener listener) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.listitem));
        textView.setOnClickListener(listener);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        int fifteenDp = dpToPixels(context, 15);
        int tenDp = dpToPixels(context, 10);
        layoutParams.setMargins(tenDp, fifteenDp, 0, fifteenDp);
        textView.setLayoutParams(layoutParams);
        textView.setPadding(fifteenDp, tenDp, fifteenDp, tenDp);

        return textView;
    }

    private TextView createMessage(Context context, String text) {
        StringBuilder message = new StringBuilder();
        int quantity;

        // Redundant?
        if (BoadskipjeList.getBoadskippen().containsKey(text)) {
            quantity = BoadskipjeList.getBoadskippen().get(text);
        } else {
            quantity = 1;
        }

        if (quantity > 1) {
            message.append(String.format("%dx ", quantity));
        }

        message.append(text);

        // Create actual view
        TextView textView = new TextView(context);
        textView.setText(message.toString());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        textView.setBackground(ContextCompat.getDrawable(context, R.drawable.listitem));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = dpToPixels(context, 15);
        layoutParams.setMargins(margin, margin, margin, margin);
        textView.setLayoutParams(layoutParams);

        int padding = dpToPixels(context, 10);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    private int dpToPixels(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }
}