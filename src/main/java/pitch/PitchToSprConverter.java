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
import tools.LoggerService;
import tools.PaletteService;
import tools.ValueCounter;

public class PitchToSprConverter {
  private BufferedImage image;
  private String dominantColor1;
  private String dominantColor2;
  private int width;
  private int height;
  private String hexStringToOutPut = "";
  private final LoggerService logger;
  private final PaletteService paletteService;

  public PitchToSprConverter(LoggerService logger, PaletteService paletteService) {
    this.logger = logger;
    this.paletteService = paletteService;
  }

  // Main method, called for the conversion process.
  public String convert(
    String bmpFilePath,
    int width,
    int height

  ) throws Exception, Throwable {
    File file = new File(bmpFilePath);
      try {
          this.image = ImageIO.read(file);
      } catch (IOException ex) {
          throw new Exception("Image file not found: " + bmpFilePath);
      }
    this.width = width;
    this.height = height;

    if (image.getWidth() != width || image.getHeight() != height) {
      throw new Exception("Image must have a width of  " + width + " pixels and height must be of  " + height + "!");
    }

    if (image.getType() != 5) {
      throw new Exception("Image must be a valid BMP file!");
    }

    // Init: extracts pixels and calculates dominant colors. Note: we could easily improve memory footprint with refacto.
    String[] chunks = this.parseFile();

    // Now we create sequences (repeated colors or sequences of single colors)
    boolean hasSwitched = false;
    SequencesContainer container = new SequencesContainer();
    ColorSequence currentSequence = new ColorSequence();
    String previousColor = "";

    for (String currentColor: chunks) {
      if (!previousColor.equals(currentColor)) {
        if (hasSwitched) {
          currentSequence = this.handleSequence(container, currentSequence);
          hasSwitched = false;
        }
        currentSequence.add(currentColor);
      } else {
        if (hasSwitched == false) {
          if (currentSequence.getLastElement().equals(currentColor)) {
            currentSequence.removeLastElement();
          }

          currentSequence = this.handleSequence(container, currentSequence);
          hasSwitched = true;
          currentSequence.add(currentColor);
        }
        currentSequence.add(currentColor);

        // For dominant, max repetition = 63. For non dominants: max repetition = 16.
        if (
          ((currentColor.equals(this.dominantColor1) || currentColor.equals(this.dominantColor2)) && currentSequence.getSize() == 63) ||
          ((!currentColor.equals(this.dominantColor1) && !currentColor.equals(this.dominantColor2)) && currentSequence.getSize() == 16)
        ) {
          currentSequence = this.handleSequence(container, currentSequence);
          currentColor = "";
        }
      }
      previousColor = currentColor;
    }

    this.handleSequence(container, currentSequence);

    // Little check-up
    int bytesCount = container.getBytesCount();
    if (bytesCount != this.width * this.height) {
      throw new Exception("Extraction failed: we extracted a total of " + bytesCount + " colors in this file");
    }

    // Finally convert
    ColorSequence sequence;

    for (int i = 0; i < container.getSequencesCount(); i++) {
      sequence = container.getSequence(i);

      if (sequence.isUniqueColor()) {
        this.outputDuplication(sequence.getFirstElement(), sequence.getSize());
      } else {
        this.outputSequence(sequence);
      }
    }

    /**
     * Now we need to calculate the file length to fill the header to tell the game where to stop when
     * loading the file. Otherwise, some part of the data might be missing.
     */
    int totalLength = (this.hexStringToOutPut.length() + 16) / 2;
    String hexLength = Integer.toHexString(totalLength);
    this.logger.log("Total length: " + totalLength + " bytes (" + hexLength + ").");
    
    this.paletteService.outputNotFounds();

    return "50414B32000" + hexLength + this.dominantColor1 + this.dominantColor2 + this.hexStringToOutPut;
  }

  // This method is used to output the content of a colors sequence.
  private void outputSequence(ColorSequence currentSequence) throws Exception {
    if (currentSequence.getSize() == 0) {
      return;
    }

    int localCount = 0;
    ColorSequence newSequence = new ColorSequence();

    for (String color: currentSequence) {
      newSequence.add(color);
      localCount++;

      if (localCount == 64) {
        localCount = 0;
        this.processColorSequence(newSequence);
      }
    }

    // If some colors are remaining because the last iteration of the sequence contains less than 64 colors.
    if (newSequence.getSize() != 0) {
      this.processColorSequence(newSequence);
    }

    currentSequence.clear();
  }

  // Used by outputSequence() for factorization.
  private void processColorSequence(ColorSequence newSequence) {
    int cValue = newSequence.getSize() + 191;
    this.hexStringToOutPut += Integer.toHexString(cValue);

    for (String colorElement: newSequence) {
      this.hexStringToOutPut += colorElement;
    }

    newSequence.clear();
  }

  // Output repeatition of the same color.
  private void outputDuplication(String color, int count) throws IOException {
    if (color.equals(this.dominantColor1)) {
      this.hexStringToOutPut += Integer.toHexString(count + 63);
    } else if (color.equals(this.dominantColor2)) {
      this.hexStringToOutPut += Integer.toHexString(count + 127);
    } else {
      this.hexStringToOutPut += "0" + Integer.toHexString(count - 1) + color;
    }
  }

  // Used to handle a sequence of bytes when splitting the files in to sequences
  private ColorSequence handleSequence(SequencesContainer container, ColorSequence currentSequence) throws IOException {
    if (currentSequence.getSize() != 0) {
      container.add(currentSequence);

      // Toggle comment when debugging.
      //  this.logger.log("Handling the current sequence (with index '" + (container.getSequencesCount() - 1) + "') contains a total of " + currentSequence.getSize() + " colors");
      //            for (String colorElement: currentSequence) {
      //                this.logger.log("Entry -> " + colorElement);
      //            }

    }

    return new ColorSequence();
  }

  /**
   * This method has two objectives while parsing the file:
   * - extract the two dominant colors;
   * - split the file in chunks of the size of the image width
   */
  private String[] parseFile() throws IOException {
    Map < String, Integer > colorsIndex = new HashMap < > ();
    String[] chunks = new String[this.height * this.width];
    int index = 0;

    // Parse the file
    for (int y = 0; y < this.height; y++) {
      for (int x = 0; x < this.width; x++) {
        Color color = new Color(this.image.getRGB(x, y), true);
        String colorString = this.paletteService.getByColor(color);

        if (colorsIndex.containsKey(colorString)) {
          colorsIndex.put(colorString, colorsIndex.get(colorString) + 1);
        } else {
          colorsIndex.put(colorString, 1);
        }

        chunks[index] = colorString;
        index++;
      }
    }

    ValueCounter biggestOne = new ValueCounter();
    ValueCounter biggestTwo = new ValueCounter();

    for (String key: colorsIndex.keySet()) {
      if (colorsIndex.get(key) > biggestOne.getCount()) {
        // First case: the value is bigger than both the two bigger we got so far
        biggestTwo = biggestOne;
        biggestOne = new ValueCounter();
        biggestOne.setValue(key);
        biggestOne.setCount(colorsIndex.get(key));
      } else if (colorsIndex.get(key) > biggestTwo.getCount()) {
        // First case: the value is bigger than the second bigger
        biggestTwo.setValue(key);
        biggestTwo.setCount(colorsIndex.get(key));
      }
    }

    this.dominantColor1 = biggestOne.getValue();
    this.dominantColor2 = biggestTwo.getValue();

    this.logger.log("Dominant color #1: " + this.dominantColor1);
    this.logger.log("Dominant color #2: " + this.dominantColor2);

    return chunks;
  }
}
