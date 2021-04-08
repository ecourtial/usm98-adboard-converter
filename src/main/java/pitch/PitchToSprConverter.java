package pitch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import tools.ValueCounter;

public class PitchToSprConverter {
    private BufferedImage image;
    private Map < String, String > coloursMap;
    private String dominantColor1;
    private String dominantColor2;
    private int width;
    private int height;
    
    public String convert(
        Map < String, String > coloursMap,
        String bmpFilePath,
        int width,
        int height

    ) throws Exception {
        File file = new File(bmpFilePath);
        this.image = ImageIO.read(file);
        this.coloursMap = coloursMap;
        this.width = width;
        this.height = height;

        if (image.getWidth() != width || image.getHeight() != height) {
            throw new Exception("Image must have a width of  " + width + " pixels and height must be of  " + height + "!");
        }

        if (image.getType() != 5) {
            throw new Exception("Image must be a valid BMP file!");
        }

        // Init: extract dominant colors and split in chunks of the size of each line (670 pixels)
        String[][] chunks = this.parseFile();
        String hexStringToOutPut = this.dominantColor1 + this.dominantColor2;
        
       // Now we create sequences (repeated colors or sequences of single colors)
       boolean hasSwitched = false;
       Map<String, Object> sequences = new HashMap<>();
       Map < String, String > currentSequence = new HashMap <  >();
       
        String previousColor = "";

        for (String[] chunk : chunks) {
            for (String currentColor : chunk) {
                if (previousColor == null || !previousColor.equals(currentColor)) {
                    if (hasSwitched) {
                        currentSequence = this.handleSequence(sequences, currentSequence);
                        hasSwitched = false;
                    }
                    this.addEntryToSequence(currentColor, currentSequence);
                } else {
                    if (hasSwitched == false) {
                        int lastIndex = currentSequence.size() - 1;
                        String lastIndexString = String.valueOf(lastIndex);
                        if (lastIndex > 0 && currentSequence.get(lastIndexString).equals(currentColor) ) {
                            currentSequence.remove(lastIndexString);
                        }
                        
                        this.handleSequence(sequences, currentSequence);
                        hasSwitched         = true;
                        currentSequence.put("-1", "whatever");
                        this.addEntryToSequence(currentColor, currentSequence);
                    }
                    this.addEntryToSequence(currentColor, currentSequence);
                    
                    // For dominant, max repetition = 63
                    if (
                            ((currentColor.equals(this.dominantColor1) || currentColor.equals(this.dominantColor2)) && currentSequence.size() == 63)
                            || ((!currentColor.equals(this.dominantColor1) && !currentColor.equals(this.dominantColor2)) && currentSequence.size() ==  16)
                            ) {
                        this.handleSequence(sequences, currentSequence);
                        currentColor = null;
                    }
                }
                
                previousColor = currentColor;
            }
            this.handleSequence(sequences, currentSequence);
        }
        
        
        
        
        
        return "";
    }
    
    /**
     * Used to handle the sequence of bytes
     */
    private Map < String, String > handleSequence(Map<String, Object> sequences, Map < String, String > currentSequence) {
        if (!currentSequence.isEmpty()) {
            String index = String.valueOf(sequences.size());
            sequences.put(index, currentSequence);
        }
        
        return new HashMap <  >();
    }
    

    private void addEntryToSequence(String newEntry, Map < String, String > currentSequence) {
        String index = String.valueOf(currentSequence.size());
        currentSequence.put(index, newEntry);
    }
    
    /**
     * This method has two objective while parsing the file:
     * - extract the two dominant colors;
     * - split the file in chunks of the size of the image width
     */
    private String[][] parseFile() {
        Map < String, ValueCounter > colorsIndex = new HashMap < > ();
        String[][] chunks = new String[this.height][this.width];

        // Parse the file
          for (int y = 0; y < this.image.getHeight(); y++) {
            String[] chunk = new String[this.width];
            for (int x = 0; x < this.image.getWidth(); x++) {
                Color color = new Color(this.image.getRGB(x, y), true);
                String colorString = color.getRed() + "-" + color.getGreen() + "-" + color.getBlue();

                if (colorsIndex.containsKey(colorString)) {
                    colorsIndex.get(colorString).increment();
                } else {
                    ValueCounter tmpCounter = new ValueCounter();
                    tmpCounter.setValue(colorString);
                    tmpCounter.increment();
                    colorsIndex.put(colorString, tmpCounter);
                }
                
                chunk[x] = this.getColourFromPalette(colorString);
            }
            
            chunks[y] = chunk;
        }

          ValueCounter biggestOne = new ValueCounter();
          ValueCounter biggestTwo = new ValueCounter();
          
          for (String key : colorsIndex.keySet()) {
              if (colorsIndex.get(key).getCount()> biggestOne.getCount()) {
                  // First case: the value is bigger than both the two bigger we got so far
                  biggestTwo = biggestOne;
                  biggestOne = new ValueCounter();
                  biggestOne.setValue(key);
                  biggestOne.setCount(colorsIndex.get(key).getCount());
              } else if(colorsIndex.get(key).getCount()> biggestTwo.getCount()) {
                  // First case: the value is bigger than the second bigger
                  biggestTwo.setValue(key);
                  biggestTwo.setCount(colorsIndex.get(key).getCount());
              }
        }
          
          this.dominantColor1 = this.getColourFromPalette(biggestOne.getValue());
          this.dominantColor2 = this.getColourFromPalette(biggestTwo.getValue());
          
          return chunks;
    }
    
    private String getColourFromPalette(String colorString) {
        if (this.coloursMap.containsKey(colorString)) {
            return  this.coloursMap.get(colorString);
        } else {
            System.out.println("Color not found in palette: " + colorString + ". Return default value 00.");
            return "00"; // Black by default
        }
    }
}
