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
    private Map < String, Color > objectColorMap;
    private Map < String, String > closestColorMap;
    private final LoggerService logger;
    private final Map < String, String > colorsNotFoundInPalette;
    private final boolean tryClosestColorInPalette;
    private final boolean reducedPalette;

    public PaletteService(String PalettePath, LoggerService logger, boolean tryClosestColorInPalette, boolean reducedPalette) {
        this.PalettePath = PalettePath;
        this.logger = logger;
        this.colorsNotFoundInPalette = new HashMap < > ();
        this.tryClosestColorInPalette = tryClosestColorInPalette;
        this.reducedPalette = reducedPalette;
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
            outputString = this.toSprColoursMap.get(colorString);
        } else {
            this.logger.log("Color not found in palette: " + colorString);

            if (this.tryClosestColorInPalette) {
                String replacementValue = this.getClosestColor(color);
                this.logger.log("Replacing with replacement value: " + replacementValue);
                this.colorsNotFoundInPalette.put(colorString, replacementValue);
                outputString = replacementValue; 
            } else {
                String defaultValue = "38"; // Bright red by default
                this.colorsNotFoundInPalette.put(colorString, colorString);
                this.logger.log("Replacing with defaut value: " + defaultValue);
                outputString = defaultValue; 
            }
        }
        
        return outputString;
    }

    // Extract when you need the palette to be indexed by the Hex value
    private void extractForConversionToBmp() throws FileNotFoundException, IOException {
        this.toBmpColoursMap = new HashMap < > ();
        Map < Integer, String > lines = this.extract(this.PalettePath);

        for (Map.Entry < Integer, String > line: lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            this.toBmpColoursMap.put(values[4].trim(), new PaletteColor(Integer.valueOf(values[0].trim()), Integer.valueOf(values[1].trim()), Integer.valueOf(values[2].trim())));
        }
    }

    // Extract when you need the palette to be indexed by the RGB value. For instance "255020255"
    private void extractForConversionToSpr() throws FileNotFoundException, IOException {
        this.toSprColoursMap = new HashMap < > ();
        this.objectColorMap = new HashMap < > ();
        this.closestColorMap = new HashMap < > ();
        Map < Integer, String > lines = this.extract(this.PalettePath);

        for (Map.Entry < Integer, String > line: lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            String R = values[0].trim();
            String G = values[1].trim();
            String B = values[2].trim();
            String hexValue = values[4].trim();
            
            if (hexValue.equals("84") && this.reducedPalette) { // For addboards, palette is limited
                break;
            }
            
            if (hexValue.length() == 1) {
                hexValue = "0" + hexValue;
            }
            
            int intR = Integer.valueOf(R);
            int intG = Integer.valueOf(G);
            int intB = Integer.valueOf(B);
            
            String colorKey = this.createToSprColorKey(intR, intG, intB);
            
            this.toSprColoursMap.put(colorKey, hexValue);
            this.objectColorMap.put(colorKey, new Color(intR, intG, intB));
        }
    }
    
    private String getClosestColor(Color color) {
        String targetKey = this.createToSprColorKey(color.getRed(), color.getGreen(), color.getBlue());
        
        if (this.closestColorMap.containsKey(targetKey)) {
            return this.toSprColoursMap.get(this.closestColorMap.get(targetKey));
        }
        
        double currentBestDistance = 999999;
        String currentClosestKey = "";
        
        for(String currentKey: this.objectColorMap.keySet()) {
            double currentDistance = this.calculateDistance(color, this.objectColorMap.get(currentKey));
            
            if (currentDistance < currentBestDistance) {
                currentBestDistance = currentDistance;
                currentClosestKey = currentKey;
            }
        }
        
        this.closestColorMap.put(targetKey, currentClosestKey);
        
        return this.toSprColoursMap.get(currentClosestKey);
    }
    
    // source: https://stackoverflow.com/questions/6334311/whats-the-best-way-to-round-a-color-object-to-the-nearest-color-constant
    private double calculateDistance(Color c1, Color c2) {
            int red1 = c1.getRed();
            int red2 = c2.getRed();
            int rmean = (red1 + red2) >> 1;
            int r = red1 - red2;
            int g = c1.getGreen() - c2.getGreen();
            int b = c1.getBlue() - c2.getBlue();
            double distance = Math.sqrt((((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8));
            
            if (distance < 0) {
                distance = distance * -1;
            }
            
            return distance;
    }

    private String createToSprColorKey(int R, int G, int B) {
        return R + "-" + G + "-" + B;
    }
    
    private Map < Integer, String > extract(String path) throws FileNotFoundException, IOException {
        Map < Integer, String > map = new HashMap < > ();
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {           
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine == true) {
                    firstLine = false;
                    continue;
                }
                count++;
                map.put(count, line);
            }
        }

        return map;
    }

    public void outputNotFounds() throws IOException, Throwable {
        if (false == this.colorsNotFoundInPalette.isEmpty()) {
            Logger colorCorrespondanceLogger = new Logger("conversions.csv");
            colorCorrespondanceLogger.log("");
            this.logger.log("The following colors were not found in the given palettes or override:");
            
            for (String colorNotFound: this.colorsNotFoundInPalette.keySet()) {
                if (this.tryClosestColorInPalette) {
                    String closestValue = this.closestColorMap.get(colorNotFound);
                    this.logger.log(colorNotFound + " -> Replaced by: " + closestValue);

                    String[] rgbValues = colorNotFound.split("-", -1);
                    colorCorrespondanceLogger.log(rgbValues[0] + ";" + rgbValues[1] + ";" + rgbValues[2] + ";;" + this.toSprColoursMap.get(closestValue));
                } else {
                    this.logger.log(colorNotFound);
                }
            }
        }
    }
}
