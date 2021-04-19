package pitch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import pitch.tools.ColorSequence;
import pitch.tools.Line;
import pitch.tools.LineContainer;
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
    private int linePixelCount = 0;
    private boolean debug = false;
    private final Logger logger;
    
    public PitchToSprConverter(Logger logger, boolean debug) {
        this.logger = logger;
        this.debug = debug;
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
        String[][] chunks = this.parseFile();
        this.hexStringToOutPut = this.dominantColor1 + this.dominantColor2;
        
       // Now we create sequences (repeated colors or sequences of single colors)
       boolean hasSwitched = false;
       LineContainer lines = new LineContainer();
       Line line;
       ColorSequence currentSequence = new ColorSequence();
        String previousColor = "";
        int lineCount =  0;
        int limit = 6;
        int lineLenght = 0;
        
        for (String[] chunk : chunks) {
            this.logMsg("Working on line #" + lineCount);
            
            limit--;
            
           if (limit >= 2) {
                //continue;
            }
            
            if (limit == 0) {
                //break;
            }
            
            line = new Line();
            
            for (String currentColor : chunk) {
                lineLenght += chunk.length;
                if  (!previousColor.equals(currentColor)) {
                    //this.logMsg("Color is different");
                    if (hasSwitched) {
                        //this.logMsg("Has switched, handling current sequence...");
                        currentSequence = this.handleSequence(line, currentSequence);
                        hasSwitched = false;
                    }
                    currentSequence.add(currentColor);
                } else {
                    //this.logMsg("Color is the same");
                    if (hasSwitched == false) {
                        if (currentSequence.getLastElement().equals(currentColor) ) {
                            currentSequence.removeLastElement();
                        }
                        
                        currentSequence = this.handleSequence(line, currentSequence);
                        hasSwitched  = true;
                        currentSequence.add(currentColor);
                    }
                    currentSequence.add(currentColor);
                    
                    // For dominant, max repetition = 63
                    if (
                            ((currentColor.equals(this.dominantColor1) || currentColor.equals(this.dominantColor2)) && currentSequence.getSize()== 63)
                            || ((!currentColor.equals(this.dominantColor1) && !currentColor.equals(this.dominantColor2)) && currentSequence.getSize()==  16)
                            ) {
                         currentSequence = this.handleSequence(line, currentSequence);
                        currentColor = "";
                    }
                }
                previousColor = currentColor;
                
//                if (currentSequence.getSize() == 64 && currentSequence.isUniqueColor() == false) {
//                    previousColor = "";
//                    currentSequence = this.handleSequence(line, currentSequence);
//                }
                
            }
             currentSequence = this.handleSequence(line, currentSequence);
             
             
             this.logMsg("There are a total of " + line.getSequencesCount()+ " sequences for this line");
             int bytesCount = line.getBytesCount();
             if (bytesCount == this.width) {
                  this.logMsg("GOOD There are a total of " + this.width + " colors in this line"); 
             } else {
                  throw new Exception("FAILED There are a total of " + this.width + " colors in this line"); 
             }
             
             if (lineLenght % 2 != 0) {
                 throw new Exception("LINE #" + lineCount + " is not of even lenght!");
             }
             
             lines.add(line);
             lineCount++;
             previousColor = "";
             lineLenght = 0;
        }
        
                     this.logMsg("There are a total of " + lines.getLinesCount()+ " lines for this file");
             int bytesCount = lines.getBytesCount();
             if (bytesCount == this.width * this.height) {
                  this.logMsg("GOOD There are a total of " + bytesCount + " colors in this file"); 
             } else {
                  this.logMsg("FAILED There are a total of " + bytesCount + " colors in this file"); 
             }
        
        // Finally convert

        ColorSequence sequence;
         int currentByteCount;
                 
        for (int i = 0; i < lines.getLinesCount(); i++) {
             this.logMsg("Parsing line with key: " + i);
             
             currentByteCount = this.hexStringToOutPut.length();
             
             line = lines.getLine(i);
             
             for (int y = 0; y < line.getSequencesCount(); y++) {
                 sequence = line.getSequence(y);
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
                    this.logMsg("5: writting sequence");
                    this.outputSequence(sequence);
                }
        }
             
        this.logMsg("End of line with key: " + i);
       
                if (this.hexStringToOutPut.length() % 2 != 0) {
            throw new Exception("Hex string to output is not of even lenght! Last line was #" + i + ". Line color count was:  " + line.getBytesCount() + ". Previous total count was: " + currentByteCount);
        }
        
    }
        this.logMsg("Job is finished, returning content.");
       
        
        return "50414B3200021E7D" + this.hexStringToOutPut;
    }
    
    private void outputSequence(ColorSequence currentSequence) throws Exception {
        int sequenceSize = currentSequence.getSize();
        
        if (sequenceSize == 0) {
            return;
        }
        
        int cValue = sequenceSize + 191;
        this.hexStringToOutPut += Integer.toHexString(cValue);
        
        if (Integer.toHexString(cValue).length() % 2 != 0) {
            throw new Exception (Integer.toHexString(cValue) + " is not of even length. Sequence size was: " + sequenceSize);
        }
        
        //for (int i = 0; i < currentSequence.getCurrentFirstIndex() ; i++) {
        for (String colorElement: currentSequence) {
            this.hexStringToOutPut += colorElement;
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
    private void outputDuplication(String color, int count) throws IOException {
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
    private ColorSequence handleSequence(Line line, ColorSequence currentSequence) throws IOException {
        if (currentSequence.getSize() != 0) {
            line.add(currentSequence);

            // Log. Remove when done and test written
         this.logMsg("Handling the current sequence (with index '"  + (line.getSequencesCount() - 1) + "') contains a total of " + currentSequence.getSize()+ " colors");
         
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
    private String[][] parseFile() throws IOException {
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
    
    private String getColourFromPalette(String colorString) throws IOException {        
        if (this.coloursMap.containsKey(colorString)) {
            return   this.coloursMap.get(colorString);
        } else {
            this.logMsg("Color not found in palette: " + colorString + ". Return default value 00.");
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
