package com.ecourtial.usm98textures.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PaletteExtractor {
    private String PalettePath;
    private Map<String, PaletteColor> ToBmpColoursMap;
    private Map<String, String> ToSprColoursMap;
    
    public PaletteExtractor(String PalettePath) {
        this.PalettePath = PalettePath;
    }

    // Extract when you need the palette to be indexed by the Hex value
    public Map<String, PaletteColor> extractForConversionToBmp() throws FileNotFoundException, IOException {
        if (null != this.ToBmpColoursMap) {
            return this.ToBmpColoursMap;
        }
        
        this.ToBmpColoursMap = new HashMap<>();
        Map<String, String> lines = this.extract(this.PalettePath);

        for (Map.Entry<String, String> line : lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            this.ToBmpColoursMap.put(values[4], new PaletteColor(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2])));
        }               
         
        return this.ToBmpColoursMap;
    }
    
    // Extract when you need the palette to be indexed by the RGB value. For instance "255020255"
    public Map<String, String> extractForConversionToSpr() throws FileNotFoundException, IOException {
       if (null != this.ToSprColoursMap) {
            return this.ToSprColoursMap;
        }
        
        this.ToSprColoursMap = new HashMap<>();
        Map<String, String> lines = this.extract(this.PalettePath);

        for (Map.Entry<String, String> line : lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            this.ToSprColoursMap.put(values[0] + values[1] + values[2], values[4]);
        }               
         
        return this.ToSprColoursMap;
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
