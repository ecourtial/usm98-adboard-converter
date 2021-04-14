package pitch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import pitch.tools.ColorSequence;
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
    private boolean debug = false;
    
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
       Map<Integer, ColorSequence> sequences = new HashMap<>();
       ColorSequence currentSequence = new ColorSequence();
        String previousColor = "";

        for (String[] chunk : chunks) {
            for (String currentColor : chunk) {
                if  (!previousColor.equals(currentColor)) {
                    this.logMsg("Color is different");
                    if (hasSwitched) {
                        this.logMsg("Has switched, handling current sequence...");
                        currentSequence = this.handleSequence(sequences, currentSequence);
                        hasSwitched = false;
                    }
                    currentSequence.add(currentColor);
                } else {
                    this.logMsg("Color is the same");
                    if (hasSwitched == false) {
                        if (currentSequence.getLastElement().equals(currentColor) ) {
                            currentSequence.removeLastElement();
                        }
                        
                        currentSequence = this.handleSequence(sequences, currentSequence);
                        hasSwitched  = true;
                        currentSequence.add(currentColor);
                    }
                    currentSequence.add(currentColor);
                    
                    // For dominant, max repetition = 63
                    if (
                            ((currentColor.equals(this.dominantColor1) || currentColor.equals(this.dominantColor2)) && currentSequence.getSize()== 63)
                            || ((!currentColor.equals(this.dominantColor1) && !currentColor.equals(this.dominantColor2)) && currentSequence.getSize()==  16)
                            ) {
                         currentSequence = this.handleSequence(sequences, currentSequence);
                        currentColor = "";
                    }
                }
                previousColor = currentColor;
            } 
             currentSequence = this.handleSequence(sequences, currentSequence);
        }
        
        // Finally convert
        this.logMsg("There are a total of " + sequences.size() + " sequences");
        
        for (int i = 0; i < sequences.size(); i++) {
             this.logMsg("Parsing sequence with key: " + i);
             ColorSequence  sequence = sequences.get(i);
       
            if (null == sequence) {
                throw new Exception("Unknown sequence with key " + i);
            }
       
            if (this.linePixelCount == 0) {
                String pixel = sequence.getFirstElement();
                
                this.logMsg("New line. Working on first pixel: " + pixel);
                if (pixel.equals(this.dominantColor1) || pixel.equals(this.dominantColor2)) {
                    if (sequence.isUniqueColor()) {
                        int sequenceCount = sequence.getSize();
                        this.logMsg("1: " + pixel);
                        outputDuplication(pixel, sequenceCount);
                        sequence = new ColorSequence();
                    } else {
                            sequence.removeFirstElement();
                            this.logMsg("2: " + pixel);
                           this.outputDuplication(pixel, 1);
                    }
                } else {
                    sequence.removeFirstElement();
                    this.logMsg("3: "+ pixel);
                    this.outputDuplication(pixel, 1);
                }
            }
            
            if (sequence.getSize() == 0) {
                continue;
            }

            if (sequence.isUniqueColor()) {
                String pixel = sequence.getFirstElement();
                
                this.logMsg("4: "+ pixel);
                this.outputDuplication(pixel, sequence.getSize());
            } else {
                this.outputSequence(sequence);
            }
        }
        //});
        
        this.hexStringToOutPut = "50414B3200021E7D" + this.hexStringToOutPut;
        
        this.logMsg(this.hexStringToOutPut);
        
        return this.hexStringToOutPut;
    }
    
    private void outputSequence(ColorSequence currentSequence) {
        int sequenceSize = currentSequence.getSize();
        
        if (sequenceSize == 0) {
            return;
        }
        
        int cValue = sequenceSize + 191;
        this.hexStringToOutPut += Integer.toHexString(cValue);
        
        for (int i = 0; i < sequenceSize; i++) {
            this.hexStringToOutPut += currentSequence.getElement(i);
        }
        
        this.linePixelCount += sequenceSize;
        if (this.linePixelCount == this.width) {
            this.logMsg("End of line !!!!!!!!!!!!");
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
            this.logMsg("End of line !!!!!!!!!!!!");
            this.linePixelCount = 0;
        }
            
    }
    
    /**
     * Used to handle the sequence of bytes
     */
    private ColorSequence handleSequence(Map<Integer, ColorSequence> sequences, ColorSequence currentSequence) {
        if (currentSequence.getSize() != 0) {
            int index = sequences.size();
            sequences.put(index, currentSequence);

            // Log. Remove when done and test written
         this.logMsg("Handling the current sequence (with index '"  + index + "') contains a total of " + currentSequence.getSize()+ " colors");
         
            for (int i=0; i<currentSequence.getSize(); i++) {
                this.logMsg("Entry -> " + currentSequence.getElement(i));
            }
            
        }
        
        return new ColorSequence();
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
            this.logMsg("Color not found in palette: " + colorString + ". Return default value 00.");
            return "38"; // Bright red by default to help highlight unrecognized colors
        }
    }
    
    private void logMsg(String msg) {
        if (this.debug) {
            System.out.println(msg);
        }
    }
}
