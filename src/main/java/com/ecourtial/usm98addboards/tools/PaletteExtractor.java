package com.ecourtial.usm98addboards.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PaletteExtractor {

    // Extract when you need the palette to be indexed by the Hex value
    public Map<String, BmpPixel> extractForConversionToBmp(String path) throws FileNotFoundException, IOException {
        Map<String, BmpPixel> coloursMap = new HashMap<>();
        Map<String, String> lines = this.extract(path);

        for (Map.Entry<String, String> line : lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            coloursMap.put(values[4], new BmpPixel(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2])));
        }               
         
        return coloursMap;
    }
    
    // Extract when you need the palette to be indexed by the RGB value. For instance "255020255"
    public Map<String, String> extractForConversionToSpr(String path) throws FileNotFoundException, IOException {
        Map<String, String> coloursMap = new HashMap<>();
        Map<String, String> lines = this.extract(path);

        for (Map.Entry<String, String> line : lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            coloursMap.put(values[0] + values[1] + values[2], values[4]);
        }               
         
        return coloursMap;
    }

    private Map<String, String> extract(String path) throws FileNotFoundException, IOException {
        Map<String, String> map = new HashMap<>();
        int count = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (firstLine == true) {
                    firstLine = false;
                    continue;
                }
                count++;
                map.put(String.valueOf(count), line);
            }
        }

        return map;
    }
}
