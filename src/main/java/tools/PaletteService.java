package tools;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PaletteService {

    private final String PalettePath;
    private Map < String, PaletteColor > toBmpColoursMap;
    private Map < String, String > toSprColoursMap;
    private Map < Integer, Map < Integer, Map < Integer, String > > > rgbOrdererColorMap;
    private Map < String, String > closestColorMap;
    private final LoggerService logger;
    private final Map < String, String > colorsNotFoundInPalette;
    private final boolean tryClosestColorInPalette;

    public PaletteService(String PalettePath, LoggerService logger, boolean tryClosestColorInPalette) {
        this.PalettePath = PalettePath;
        this.logger = logger;
        this.colorsNotFoundInPalette = new HashMap < > ();
        this.tryClosestColorInPalette = tryClosestColorInPalette;
    }

    public Color getByHexValue(String hexValue) throws IOException {
        if (null == this.toBmpColoursMap) {
            this.extractForConversionToBmp();
        }

        if (this.toBmpColoursMap.containsKey(hexValue)) {
            PaletteColor colorPalette = this.toBmpColoursMap.get(hexValue);

            return new Color(colorPalette.getR(), colorPalette.getG(), colorPalette.getB(), 0);
        } else {
            this.logger.log("Missing color. Hex key is: " + hexValue);
            this.colorsNotFoundInPalette.put(hexValue, hexValue);

            // Return red to help developer to debug
            return new Color(181, 4, 2, 0);
        }
    }

    public String getByColor(Color color) throws IOException {
        if (null == this.toSprColoursMap) {
            this.extractForConversionToSpr();
        }

        String colorString =  this.createToSprColorKey(color.getRed(), color.getGreen() , color.getBlue());
        String outputString = "";

        if (this.toSprColoursMap.containsKey(colorString)) {
            outputString += this.toSprColoursMap.get(colorString);
        } else {
            this.logger.log("Color not found in palette: " + colorString);
            this.colorsNotFoundInPalette.put(colorString, colorString);
            
            if (this.tryClosestColorInPalette) {
                String replacementValue = this.getClosestColor(color);
                this.logger.log("Replacing with replacement value: " + replacementValue);
                outputString += replacementValue; 
            } else {
                String defaultValue = "38"; // Bright red by default
                this.logger.log("Replacing with defaut value: " + defaultValue);
                outputString += defaultValue; 
            }
        }

        return outputString;
    }

    // Extract when you need the palette to be indexed by the Hex value
    private void extractForConversionToBmp() throws FileNotFoundException, IOException {
        this.toBmpColoursMap = new HashMap < > ();
        Map < String, String > lines = this.extract(this.PalettePath);

        for (Map.Entry < String, String > line: lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            this.toBmpColoursMap.put(values[4].trim(), new PaletteColor(Integer.valueOf(values[0].trim()), Integer.valueOf(values[1].trim()), Integer.valueOf(values[2].trim())));
        }
    }

    // Extract when you need the palette to be indexed by the RGB value. For instance "255020255"
    private void extractForConversionToSpr() throws FileNotFoundException, IOException {
        this.toSprColoursMap = new HashMap < > ();
        this.rgbOrdererColorMap = new HashMap < > ();
        this.closestColorMap = new HashMap < > ();
        Map < String, String > lines = this.extract(this.PalettePath);

        for (Map.Entry < String, String > line: lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            String R = values[0].trim();
            String G = values[1].trim();
            String B = values[2].trim();
            String hexValue = values[4].trim();
            
            this.toSprColoursMap.put(this.createToSprColorKey(Integer.valueOf(R), Integer.valueOf(G), Integer.valueOf(B)), hexValue);
            this.addToClosestColorMap(R, G, B, hexValue);
        }
    }
    
    private void addToClosestColorMap(String R, String G, String B,  String hexValue) {
        int intR = Integer.valueOf(R);
        int intG = Integer.valueOf(G);
        int intB = Integer.valueOf(B);
        
        if (false == this.rgbOrdererColorMap.containsKey(intR)) {
            this.rgbOrdererColorMap.put(intR, new HashMap < > ());
        }
        
        if (false == this.rgbOrdererColorMap.get(intR).containsKey(intG)) {
            this.rgbOrdererColorMap.get(intR).put(intG, new HashMap < > ());
        }
        
         if (false == this.rgbOrdererColorMap.get(intR).get(intG).containsKey(intB)) {
             this.rgbOrdererColorMap.get(intR).get(intG).put(intB, hexValue);
         }
    }

    private String getClosestColor(Color color) {
        String targetKey = this.createToSprColorKey(color.getRed(), color.getGreen(), color.getBlue());
        
        if (this.closestColorMap.containsKey(targetKey)) {
            return this.closestColorMap.get(targetKey);
        }

        int askedR = color.getRed();
        int askedG = color.getGreen();
        int askedB = color.getBlue();
                 
        int closestR = 0;
        int closestG = 0;
        int closestB = 0;
        
        for(int R: this.rgbOrdererColorMap.keySet()) {
            closestR = this.calculateClosest(closestR, R, askedR);
        }
        
        for(int G: this.rgbOrdererColorMap.get(closestR).keySet()) {
            closestG = this.calculateClosest(closestG, G, askedG);
        }
        
        for(int B: this.rgbOrdererColorMap.get(closestR).get(closestG).keySet()) {
            closestB = this.calculateClosest(closestB, B, askedB);
        }
        
        String newKey = this.createToSprColorKey(closestR, closestG, closestB);
        String value = this.toSprColoursMap.get(newKey);

        this.closestColorMap.put(targetKey, value);
        
        return value;
    }
    
    private int calculateClosest(int currentClosest, int current, int asked) {        
        if (currentClosest == 0) {
            return current;
        }
        
        int currentDifference = asked - currentClosest;
        
        if (currentDifference < 0 ) {
            currentDifference = currentDifference * -1;
        }
        
        int difference = asked - current;
        
        if (difference < 0 ) {
            difference = difference * -1;
        }
        
        if (difference > currentDifference) {
            return currentClosest;
        } else {
            return current;
        }
    }
    
    private String createToSprColorKey(int R, int G, int B) {
        return R + "-" + G + "-" + B;
    }
    
    private Map < String, String > extract(String path) throws FileNotFoundException, IOException {
        Map < String, String > map = new HashMap < > ();
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

    public void outputNotFounds() throws IOException, Throwable {
        if (false == this.colorsNotFoundInPalette.isEmpty()) {
            this.logger.log("The following colors were not found in the given palettes or override:");
            for (String colorNotFound: this.colorsNotFoundInPalette.keySet()) {
                this.logger.log("color with key " + colorNotFound);
            }
        }
    }
}
