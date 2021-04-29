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
    private Map < String, String > ToSprColoursMap;
    private final LoggerService logger;
    private final Map < String, String > colorsNotFoundInPalette;

    public PaletteService(String PalettePath, LoggerService logger) {
        this.PalettePath = PalettePath;
        this.logger = logger;
        this.colorsNotFoundInPalette = new HashMap < > ();
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

            // Return bright red to help developer to debug
            return new Color(181, 4, 2, 0);
        }
    }

    public String getByColor(Color color) throws IOException {
        if (null == this.ToSprColoursMap) {
            this.extractForConversionToSpr();
        }

        String colorString = color.getRed() + "-" + color.getGreen() + "-" + color.getBlue();
        String outputString = "";

        if (this.ToSprColoursMap.containsKey(colorString)) {
            outputString += this.ToSprColoursMap.get(colorString);
        } else {
            outputString += "38"; // Bright red by default
            this.logger.log("Color not found in palette: " + colorString);
            this.colorsNotFoundInPalette.put(colorString, colorString);
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
        this.ToSprColoursMap = new HashMap < > ();
        Map < String, String > lines = this.extract(this.PalettePath);

        for (Map.Entry < String, String > line: lines.entrySet()) {
            String[] values = line.getValue().split(String.valueOf(";"));
            this.ToSprColoursMap.put(values[0].trim() + "-" + values[1].trim() + "-" + values[2].trim(), values[4].trim());
        }
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

    @Override
    protected void finalize() throws IOException, Throwable {
        if (false == this.colorsNotFoundInPalette.isEmpty()) {
            this.logger.log("The following colors were not found in the given palettes or override:");
            for (String colorNotFound: this.colorsNotFoundInPalette.keySet()) {
                this.logger.log("color with key " + colorNotFound);
            }
        }
    }
}
