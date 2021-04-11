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
       Map<String, Map < String, String >> sequences = new HashMap<>();
       Map < String, String > currentSequence = new HashMap <  >();
       int limit = 5;
        String previousColor = "";

        for (String[] chunk : chunks) {
            //this.logMsg("Looping on chunck.");
            for (String currentColor : chunk) {
                this.logMsg("Looping on current color: " + currentColor);
                if  (!previousColor.equals(currentColor)) {
                    this.logMsg("Color is different");
                    if (hasSwitched) {
                       // this.logMsg("Has switched, handling current sequence...");
                        currentSequence = this.handleSequence(sequences, currentSequence);
                        hasSwitched = false;
                    }
                    this.addEntryToSequence(currentColor, currentSequence);
                } else {
                    //this.logMsg("Color is the same");
                    if (hasSwitched == false) {
                        int lastIndex = currentSequence.size() - 1;
                        String lastIndexString = String.valueOf(lastIndex);
                        //this.logMsg("Last index: " + lastIndexString);
                        if (currentSequence.get(lastIndexString).equals(currentColor) ) {
                            //this.logMsg("Last index exists. Removing it from previous sequence.");
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
             currentSequence = this.handleSequence(sequences, currentSequence);
             
             limit--;
             if (limit == 0) {
                 //break;
             }
             
        }
        chunks = null;
        
        // Finally convert
        this.logMsg("There are a total of " + sequences.size() + " sequences");
        
        //sequences.values().forEach(sequence -> {

       //for (String key : sequences.keySet()) {    
        for (int i = 0; i < sequences.size(); i++) {
        String key = String.valueOf(i);
            
       this.logMsg("Parsing sequence with key: " + key);
       Map < String, String >  sequence = sequences.get(key);
       
       if (null == sequence) {
           throw new Exception("Unknown sequence with key " + key);
       }
       
//       for (String currentKey : sequence.keySet()) {    
//           this.logMsg("Key present: " + currentKey);
//       }
//       
        if (this.linePixelCount == 0) {

                 Map.Entry<String, String> entry = sequence.entrySet().iterator().next();
                String firstKey = entry.getKey();
                String pixel = sequence.get(firstKey);
                
    
                this.logMsg("New line. Working on first pixel: " + pixel);
                if (pixel.equals(this.dominantColor1) || pixel.equals(this.dominantColor2)) {
                    if (sequence.containsKey("-1")) {
                        sequence.remove("-1");
                        int sequenceCount = sequence.size();
                        this.logMsg("1: " + pixel);
                        outputDuplication(pixel, sequenceCount);
                        sequence = new HashMap <  >();
                    } else {
                            sequence.remove(firstKey);
                            this.logMsg("2: " + pixel);
                           this.outputDuplication(pixel, 1);
                    }
                } else {
                    sequence.remove(firstKey);
                    this.logMsg("3: "+ pixel);
                    this.outputDuplication(pixel, 1);
                }
            }

            if (sequence.containsKey("-1")) {
                sequence.remove("-1");
                int sequenceCount = sequence.size();
                

                
                                 Map.Entry<String, String> entry = sequence.entrySet().iterator().next();
                String pixel = entry.getValue();
                
                this.logMsg("4: "+ pixel);
                this.outputDuplication(pixel, sequenceCount);
            } else {
                this.outputSequence(sequence);
            }
        }
        //});
        
        this.hexStringToOutPut = "50414B3200021E7D" + this.hexStringToOutPut;
        
        this.logMsg(this.hexStringToOutPut);
        
        return this.hexStringToOutPut;
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
    private Map < String, String > handleSequence(Map<String, Map < String, String >> sequences, Map < String, String > currentSequence) {
        if (!currentSequence.isEmpty()) {
            String index = String.valueOf(sequences.size());
            sequences.put(index, currentSequence);
            
            
                     
         this.logMsg("Handling the current sequence (with index '"  + index + "') contains a total of " + currentSequence.size() + " bytes");
        currentSequence.values().forEach(entry -> {
                this.logMsg("Entry -> " + entry);
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
