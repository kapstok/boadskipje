package be.allersma.boadskipje.persistence;

import android.content.Context;
import android.widget.Toast;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FoardielRegister {
    private Map<String, String> register = null;
    private final String CSV_NAME = "foardiel.csv";

    public FoardielRegister() {}

    public Map<String, String> getRegister(Context context) {
        if (register == null) {
            register = loadRegister(context);
        }

        return register;
    }

    public void addToRegister(Context context, String name, String code) {
        if (register == null) {
            register = loadRegister(context);
        }
        if (register.containsKey(name)) {
            return;
        }

        register.put(name, code);
        saveRegister(context);
    }

    private Map<String, String> loadRegister(Context context) {
        Map<String, String> register = new HashMap<>();
        try {
            InputStream stream = context.openFileInput(CSV_NAME);
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            String[] line;

            while ((line = reader.readNext()) != null) {
                if (line.length == 2) {
                    register.put(line[0], line[1]);
                } else {
                    Toast.makeText(context, "Barcode register is net jildich", Toast.LENGTH_LONG).show();
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Barcode register net fûn", Toast.LENGTH_LONG).show();
            return createNewRegister(context);
        } catch (IOException | CsvValidationException e) {
            Toast.makeText(context, "Barcode register is net jildich", Toast.LENGTH_LONG).show();
        }
        return register;
    }

    private Map<String, String> createNewRegister(Context context) {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(CSV_NAME, Context.MODE_PRIVATE));
            writer.write("\"AH Bonuskaart\", \"2610400000012\"\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            Toast.makeText(context, "Foardiel register oanmeitsjen mislearre", Toast.LENGTH_LONG).show();
        }
        Map<String, String> result = new HashMap<>();
        result.put("AH Bonuskaart", "2610400000012");
        return result;
    }

    private void saveRegister(Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(CSV_NAME, Context.MODE_PRIVATE));
            CSVWriter writer = new CSVWriter(outputStreamWriter);

            for (Map.Entry<String, String> pair : register.entrySet()) {
                String[] row = {pair.getKey(), pair.getValue()};
                writer.writeNext(row);
            }

            writer.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "Foardiel register net fûn", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Foardiel register is net jildich", Toast.LENGTH_LONG).show();
        }
    }
}
