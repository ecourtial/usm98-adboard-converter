package pitch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import pitch.tools.ColorSequence;
import pitch.tools.SequencesContainer;
import tools.Logger;
import tools.ValueCounter;

public class PitchToSprConverter {
    private BufferedImage image;
    private Map < String, String > coloursMap;
    private String dominantColor1;
    private String dominantColor2;
    private int width;
    private int height;
    private String hexStringToOutPut;
    private boolean debug = false;
    private final Logger logger;
    private final Map < String, String > colorsNotFoundInPalette; 
    
    public PitchToSprConverter(Logger logger, boolean debug) {
        this.logger = logger;
        this.debug = debug;
        this.colorsNotFoundInPalette = new HashMap <>();
    }
    
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
        String[] chunks = this.parseFile();
        
       // Now we create sequences (repeated colors or sequences of single colors)
       boolean hasSwitched = false;
       SequencesContainer container = new SequencesContainer();
       ColorSequence currentSequence = new ColorSequence();
        String previousColor = "";
        
        for (String currentColor : chunks) {
                if  (!previousColor.equals(currentColor)) {
                    //this.logMsg("Color is different");
                    if (hasSwitched) {
                        //this.logMsg("Has switched, handling current sequence...");
                        currentSequence = this.handleSequence(container, currentSequence);
                        hasSwitched = false;
                    }
                    currentSequence.add(currentColor);
                } else {
                    //this.logMsg("Color is the same");
                    if (hasSwitched == false) {
                        if (currentSequence.getLastElement().equals(currentColor) ) {
                            currentSequence.removeLastElement();
                        }

                        currentSequence = this.handleSequence(container, currentSequence);
                        hasSwitched  = true;
                        currentSequence.add(currentColor);
                    }
                    currentSequence.add(currentColor);

                    // For dominant, max repetition = 63
                    if (
                            ((currentColor.equals(this.dominantColor1) || currentColor.equals(this.dominantColor2)) && currentSequence.getSize()== 63)
                            || ((!currentColor.equals(this.dominantColor1) && !currentColor.equals(this.dominantColor2)) && currentSequence.getSize()==  16)
                            ) {
                         currentSequence = this.handleSequence(container, currentSequence);
                        currentColor = "";
                    }
                }
                previousColor = currentColor;

            }
            this.handleSequence(container, currentSequence);

             int bytesCount = container.getBytesCount();
             if (bytesCount == this.width * this.height) {
                  this.logMsg("GOOD There are a total of " + bytesCount + " colors in this file"); 
             } else {
                  throw new Exception("FAILED There are a total of " + bytesCount + " colors in this file"); 
             }
        
        // Finally convert

        ColorSequence sequence;

        for (int i = 0; i < container.getSequencesCount(); i++) {
             this.logMsg("sequence with key: " + i);
             
             sequence = container.getSequence(i);
             
           if (sequence.isUniqueColor()) {
               this.outputDuplication(sequence.getFirstElement(), sequence.getSize());
           } else {
               this.outputSequence(sequence);
           }
    }
        
       if ( false == this.colorsNotFoundInPalette.isEmpty()) {
           this.logMsg("The following colors were not found in the given palettes or override:");
            for (String colorNotFound: this.colorsNotFoundInPalette.keySet()) {
                    this.logMsg("color with key " + colorNotFound);
            }
       }

       /**
        * Now we need to calculate the file length to fill the header to tell the game where to stop when
        * loading the file. Otherwise, some part of the data might be missing.
        */
       int totalLength = (this.hexStringToOutPut.length() + 16) / 2;
       String hexLength = Integer.toHexString(totalLength);
       this.logMsg("Total length: " + totalLength + " bytes (" + hexLength + ").");
       String stopPoint = hexLength.substring(1);

        return "50414B320002" + stopPoint + this.dominantColor1 + this.dominantColor2 + this.hexStringToOutPut;
    }
    
    private void outputSequence(ColorSequence currentSequence) throws Exception {
        int sequenceSize = currentSequence.getSize();
        
        if (sequenceSize == 0) {
            return;
        }
        
        int cValue;
        int localCount = 0;
        ColorSequence newSequence = new ColorSequence();
        
        for (String color: currentSequence) {
            newSequence.add(color);
            localCount++;
            
            if (localCount == 64) {
                localCount = 0;
                cValue = sequenceSize + 191;
                this.hexStringToOutPut += Integer.toHexString(cValue);

                for (String colorElement: newSequence) {
                    this.hexStringToOutPut += colorElement;
                }
                
                newSequence = new ColorSequence();
            }
        }
        
        if (newSequence.getSize() != 0) {
                cValue = sequenceSize + 191;
                this.hexStringToOutPut += Integer.toHexString(cValue);

                for (String colorElement: newSequence) {
                    this.hexStringToOutPut += colorElement;
                }
        }

        currentSequence.clear();
    }
    
    // Output qty * color. Ex: 02 35 will output 3 times the color 35
// Or if one of the two dominant colors
    private void outputDuplication(String color, int count) throws IOException {
        if (color.equals(this.dominantColor1)) {
            this.hexStringToOutPut += Integer.toHexString(count + 63);
        } else if(color.equals(this.dominantColor2)) {
            this.hexStringToOutPut += Integer.toHexString(count + 127);
        } else {
            this.hexStringToOutPut += "0" + Integer.toHexString(count - 1) + color;
        }
    }
    
    /**
     * Used to handle the sequence of bytes
     */
    private ColorSequence handleSequence(SequencesContainer container, ColorSequence currentSequence) throws IOException {
        if (currentSequence.getSize() != 0) {
            container.add(currentSequence);

            // Log. Remove when done and test written
         this.logMsg("Handling the current sequence (with index '"  + (container.getSequencesCount() - 1) + "') contains a total of " + currentSequence.getSize()+ " colors");
         
            for (String colorElement: currentSequence) {
                this.logMsg("Entry -> " + colorElement);
            }
            
        }
        
        return new ColorSequence();
    }
    
    /**
     * This method has two objective while parsing the file:
     * - extract the two dominant colors;
     * - split the file in chunks of the size of the image width
     */
    private String[] parseFile() throws IOException {
        Map < String, ValueCounter > colorsIndex = new HashMap < > ();
        String[] chunks = new String[this.height * this.width];
        int index = 0;

        // Parse the file
          for (int y = 0; y < this.image.getHeight(); y++) {
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
                
                chunks[index] = this.getColourFromPalette(colorString);
                index++;
            }
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
    
    private String getColourFromPalette(String colorString) throws IOException {        
        if (this.coloursMap.containsKey(colorString)) {
            return   this.coloursMap.get(colorString);
        } else {
            this.logMsg("Color not found in palette: " + colorString + ". Return default value 00.");
            this.colorsNotFoundInPalette.put(colorString, colorString);
            return "38"; // Bright red by default to help highlight unrecognized colors
        }
    }
    
    private void logMsg(String msg) throws IOException {
        if (this.debug) {
            System.out.println(msg);
            this.logger.log(msg);
        }
    }
}
