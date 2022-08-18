package be.allersma.boadskipje.persistence;

import android.content.Context;
import android.widget.Toast;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoadskipjeList {
    private static Map<String, Integer> boadskippen = null;
    private static final String CSV_NAME = "boadskippen.csv";

    private BoadskipjeList() {
        // Singleton
    }

    public static Map<String, Integer> getBoadskippen(Context context) {
        if (boadskippen == null) {
            boadskippen = loadRegister(context);
        }

        return boadskippen;
    }

    public static void addBoadskip(Context context, String boadskip) {
        if (boadskippen == null) {
            boadskippen = loadRegister(context);
        }
        if (boadskippen.containsKey(boadskip)) {
            int quantity = boadskippen.get(boadskip);
            boadskippen.put(boadskip, quantity + 1);
        } else {
            boadskippen.put(boadskip, 1);
        }
        saveRegister(context);
    }

    public static void removeBoadskip(Context context, String boadskip) {
        if (!boadskippen.containsKey(boadskip)) {
            return;
        }

        int quantity = boadskippen.get(boadskip);
        if (quantity == 1) {
            boadskippen.remove(boadskip);
        } else if (quantity > 1) {
            boadskippen.put(boadskip, quantity - 1);
        }

        saveRegister(context);
    }

    private static Map<String, Integer> loadRegister(Context context) {
        Map<String, Integer> register = new HashMap<>();
        try {
            InputStream stream = context.openFileInput(CSV_NAME);
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length == 2) {
                    register.put(line[0], Integer.parseInt(line[1]));
                } else {
                    Toast.makeText(context, "Boadskip register is net jildich", Toast.LENGTH_LONG).show();
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Boadskip register net fûn", Toast.LENGTH_LONG).show();
            return createNewRegister(context);
        } catch (IOException | CsvValidationException e) {
            Toast.makeText(context, "Boadskip register is net jildich", Toast.LENGTH_LONG).show();
        }
        return register;
    }

    private static Map<String, Integer> createNewRegister(Context context) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(CSV_NAME, Context.MODE_PRIVATE));
            writer.write("");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Toast.makeText(context, "Boadskip register oanmeitsjen mislearre", Toast.LENGTH_LONG).show();
        }
        return new HashMap<>();
    }

    private static void saveRegister(Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(CSV_NAME, Context.MODE_PRIVATE));
            CSVWriter writer = new CSVWriter(outputStreamWriter);

            for (Map.Entry<String, Integer> pair : boadskippen.entrySet()) {
                String[] row = {pair.getKey(), String.valueOf(pair.getValue())};
                writer.writeNext(row);
            }

            writer.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Boadskip register net fûn", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Boadskip register is net jildich", Toast.LENGTH_LONG).show();
        }
    }
}
