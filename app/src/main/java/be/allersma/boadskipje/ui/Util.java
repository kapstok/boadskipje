package be.allersma.boadskipje.ui;

import android.content.Context;
import android.util.TypedValue;

public class Util {
    private Util() {
        // Singleton
    }

    public static int dpToPixels(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }
}
