package be.allersma.boadskipje;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoadskipjeList {
    private static Map<String, Integer> boadskippen = null;

    private BoadskipjeList() {
        // Singleton
    }

    public static Map<String, Integer> getBoadskippen() {
        if (boadskippen == null) {
            boadskippen = new HashMap<>();
        }

        return boadskippen;
    }

    public static void addBoadskip(String boadskip) {
        if (boadskippen.containsKey(boadskip)) {
            int quantity = boadskippen.get(boadskip);
            boadskippen.put(boadskip, quantity + 1);
        } else {
            boadskippen.put(boadskip, 1);
        }
    }

    public static void removeBoadskip(String boadskip) {
        if (!boadskippen.containsKey(boadskip)) {
            return;
        }

        int quantity = boadskippen.get(boadskip);
        if (quantity == 1) {
            boadskippen.remove(boadskip);
        } else if (quantity > 1) {
            boadskippen.put(boadskip, quantity - 1);
        }
    }
}
