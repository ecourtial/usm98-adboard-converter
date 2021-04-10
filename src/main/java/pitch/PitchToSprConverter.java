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
    private String hexStringToOutPut;
    private int linePixelCount = 0;
    
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
        this.hexStringToOutPut = this.dominantColor1 + this.dominantColor2;
        
       // Now we create sequences (repeated colors or sequences of single colors)
       boolean hasSwitched = false;
       Map<String, Map < String, String >> sequences = new HashMap<>();
       Map < String, String > currentSequence = new HashMap <  >();
       
        String previousColor = "";

        for (String[] chunk : chunks) {
            System.out.println("Looping on chunck.");
            for (String currentColor : chunk) {
                System.out.println("Looping on current color: " + currentColor);
                if  (!previousColor.equals(currentColor)) {
                     System.out.println("Color is different");
                    if (hasSwitched) {
                        System.out.println("Has switched, handling current sequence...");
                        currentSequence = this.handleSequence(sequences, currentSequence);
                        hasSwitched = false;
                    }
                    this.addEntryToSequence(currentColor, currentSequence);
                } else {
                    System.out.println("Color is the same");
                    if (hasSwitched == false) {
                        int lastIndex = currentSequence.size() - 1;
                        String lastIndexString = String.valueOf(lastIndex);
                        System.out.println("Last index: " + lastIndexString);
                        if (currentSequence.get(lastIndexString).equals(currentColor) ) {
                            System.out.println("Last index exists. Removing it from previous sequence.");
                            currentSequence.remove(lastIndexString);
                        }
                        
                         currentSequence = this.handleSequence(sequences, currentSequence);
                        hasSwitched         = true;
                        currentSequence.put("-1", "-1");
                        this.addEntryToSequence(currentColor, currentSequence);
                    }
                    this.addEntryToSequence(currentColor, currentSequence);
                    
                    // For dominant, max repetition = 63
                    if (
                            ((currentColor.equals(this.dominantColor1) || currentColor.equals(this.dominantColor2)) && currentSequence.size() == 63)
                            || ((!currentColor.equals(this.dominantColor1) && !currentColor.equals(this.dominantColor2)) && currentSequence.size() ==  16)
                            ) {
                         currentSequence = this.handleSequence(sequences, currentSequence);
                        currentColor = "";
                    }
                }
                
                previousColor = currentColor;
            }
             this.handleSequence(sequences, currentSequence);
            
            //break;
        }
        chunks = null;
        
        // Finally convert
        System.out.println("There are a total of " + sequences.size() + " sequences");
//         Map < String, String > sequence = sequences.get("0");
//         System.out.println("The current sequence contains a total of " + sequence.size() + " bytes");
//        sequence.values().forEach(entry -> {
//                System.out.println("Entry -> " + entry);
//            });
        return "";
        
        sequences.values().forEach(sequence -> {
//            if (this.linePixelCount == 0) {
//                String pixel = sequence.get("0");
//                if (pixel.equals(this.dominantColor1) || pixel.equals(this.dominantColor2)) {
//                    if (sequence.containsKey("-1")) {
//                        sequence.remove("-1");
//                        int sequenceCount = sequence.size();
//                        System.out.println("1" + pixel);
//                        outputDuplication(pixel, sequenceCount);
//                        sequence = new HashMap <  >();
//                    } else {
//                            sequence.remove("0");
//                            System.out.println("2" + pixel);
//                           this.outputDuplication(pixel, 1);
//                    }
//                } else {
//                    sequence.remove("0");
//                    System.out.println("3"+ pixel);
//                    this.outputDuplication(pixel, 1);
//                }
//            }
//
//            if (sequence.containsKey("-1")) {
//                sequence.remove("-1");
//                int sequenceCount = sequence.size();
//                String pixel = sequence.get("0");
//                System.out.println("4"+ pixel);
//                this.outputDuplication(pixel, sequenceCount);
//            } else {
//                this.outputSequence(sequence);
//            }
        });
        
        this.hexStringToOutPut = "50414B3200021E7D" + this.hexStringToOutPut;
        
        System.out.println(this.hexStringToOutPut);
        
        return "";
    }
    
    private void outputSequence(Map < String, String > currentSequence) {
        int sequenceSize = currentSequence.size();
        
        if (sequenceSize == 0) {
            return;
        }
        
        int cValue = sequenceSize + 191;
        this.hexStringToOutPut += Integer.toHexString(cValue);
        
        currentSequence.values().forEach(value -> {
            this.hexStringToOutPut += value;
        });
        
        this.linePixelCount += sequenceSize;
        if (this.linePixelCount == this.width) {
            this.linePixelCount = 0;
        }
        
        currentSequence.clear();
    }
    
    // Output qty * color. Ex: 02 35 will output 3 times the color 35
// Or if one of the two dominant colors
    private void outputDuplication(String color, int count) {
        if (color.equals(this.dominantColor1)) {
            this.hexStringToOutPut += Integer.toHexString(count + 63);
        } else if(color.equals(this.dominantColor2)) {
            this.hexStringToOutPut += Integer.toHexString(count + 127);
        } else {
            this.hexStringToOutPut += "0" + Integer.toHexString(count - 1) + color;
        }
        
        this.linePixelCount += count;
        if (this.linePixelCount == this.width) {
            this.linePixelCount = 0;
        }
            
    }
    
    /**
     * Used to handle the sequence of bytes
     */
    private Map < String, String > handleSequence(Map<String, Map < String, String >> sequences, Map < String, String > currentSequence) {
        if (!currentSequence.isEmpty()) {
            String index = String.valueOf(sequences.size());
            sequences.put(index, currentSequence);
            
            
                     
         System.out.println("Handling the current sequence (with index '"  + index + "') contains a total of " + currentSequence.size() + " bytes");
        currentSequence.values().forEach(entry -> {
                System.out.println("Entry -> " + entry);
            });
            
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
            return "38"; // Bright red by default to help highlight unrecognized colors
        }
    }
}
