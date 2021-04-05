package pitch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import tools.PaletteColor;

public class PitchToBmpConverter {
    private boolean cMode = false;
    private boolean duplicateDominantColor1 = false;
    private boolean duplicateDominantColor2 = false;
    private boolean standardMode = false;
    private boolean debugMode = false;
    private int x = 0;
    private int y = 0;
    private int width = 0;
    private int printedPixels = 0;
    private BufferedImage img;
    private Map < String, PaletteColor > coloursMap;

    public void convert(Map < String, PaletteColor > coloursMap, byte[] fileContent, int Width, int Height, String outputPath) throws IOException {
        this.width = Width;
        this.coloursMap = coloursMap;
        this.img = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
        String dominantColor1 = this.getHexValue(fileContent[8]);
        String dominantColor2 = this.getHexValue(fileContent[9]);
        boolean mod = false;
        int qty = 0;
        int cModeCount = 0;

        this.debugMessage("Starting process. Dominant colors are " + dominantColor1 + " and " + dominantColor2);

        for (int i = 10; i < fileContent.length; i++) {
            int currentByte = fileContent[i];
            String hex = this.getHexValue(currentByte);
            currentByte = (int) Long.parseLong(hex, 16);

            this.debugMessage("Current value : " + currentByte + " - in hex: " + hex + " at " + i);

            if (mod == false) {
                /**
                 * Detection of the mode. (see all comments below)
                 *
                 * Switching mode. There are many modes:
                 * - Duplicate one of the two dominant colors. We Detect this mode with the current byte.
                 * - C0: the color in the following byte will be displayed once (qty not given in the content)
                 * - C1: the color in the TWO following bytes will be displayed once (qty not given in the content)
                 * - C2: the color in the THREE following bytes will be displayed once (qty not given in the content)
                 * - C3: the color in the FOUR following bytes will be displayed once (qty not given in the content)
                 * ... 
                 * - Standard mode = one byte is the qty, the second is the color
                 */
                if (currentByte >= 192 && currentByte <= 255) { // 207 = CA
                    qty = 1;
                    this.debugMessage("To Cx mode : " + currentByte);
                    cModeCount = currentByte - 191;
                    // Cx End of previous mode. Switch to unique mode ONLY for the following value.
                    cMode = true;
                    continue;
                } else if (false == this.isDuplicationModeEnabled() && currentByte >= 64 && currentByte <= 127) { // Usage of dominant color
                    this.debugMessage("To Dominant color 1 mode : " + currentByte);
                    this.duplicateDominantColor1 = true;
                    mod = true;
                    qty = currentByte - 63;
                } else if (false == this.isDuplicationModeEnabled() && currentByte >= 128 && currentByte <= 191) {
                    this.debugMessage("To Dominant color 2 mode : " + currentByte);
                    this.duplicateDominantColor2 = true;
                    qty = currentByte - 127;
                    mod = true;
                } else if (false == this.isModeEnabled()) {
                    // Standard mode: 2 bytes (one for the qty, one for the color)
                    this.debugMessage("To Standard  mode : " + currentByte);
                    this.standardMode = true;
                    mod = false;
                }
            }

            if (mod == false && this.standardMode) {
                qty = currentByte + 1; // 0 = 1, 1 = 2 ...
                mod = true;
                continue;
            } else {
                if (this.isDuplicatingDominantColorEnabled()) {
                    if (this.duplicateDominantColor1) {
                        hex = dominantColor1;
                    }
                    if (this.duplicateDominantColor2) {
                        hex = dominantColor2;
                    }
                }

                if (this.isDuplicationModeEnabled()) {
                    cModeCount--;
                    if (cModeCount == 0) {
                        this.cMode = false;
                    }
                }

                if (this.isDuplicationModeEnabled()) {
                    disableAllModes();
                    this.cMode = true;
                } else {
                    disableAllModes();
                }

                try {
                    this.draw(hex, qty);
                } catch (Exception e) {
                    this.outputImage(outputPath); // To help the user to see where the file is corrupted
                    this.debugMessage("An error was raised. Current coordinates are: " + this.x + " - " + this.y);
                    throw e;
                }

                mod = false;

                if (this.isDuplicationModeEnabled()) {
                    qty = 1;
                    mod = true;
                }
            }

            if (this.y >= Height) {
                break;
            }
        }

        this.outputImage(outputPath);
    }

    private void debugMessage(String msg) {
        if (this.debugMode) {
            System.out.println(msg);
        }
    }

    private void outputImage(String outputPath) throws IOException {
        RenderedImage rendImage = this.img;
        ImageIO.write(rendImage, "bmp", new File(outputPath));
    }

    private String getHexValue(int currentByte) {
        String hex = Integer.toHexString(currentByte);
        if (hex.length() > 2) {
            hex = hex.substring(hex.length() - 2);
        }

        hex = hex.toUpperCase();

        if (hex.length() == 1) {
            hex = "0" + hex;
        }

        return hex;
    }

    private boolean isModeEnabled() {
        return this.cMode || this.duplicateDominantColor1 || this.duplicateDominantColor2 || this.standardMode;
    }

    private boolean isDuplicationModeEnabled() {
        return this.cMode;
    }

    private boolean isDuplicatingDominantColorEnabled() {
        return this.duplicateDominantColor1 || this.duplicateDominantColor2;
    }

    private void disableAllModes() {
        this.cMode = false;
        this.duplicateDominantColor1 = false;
        this.duplicateDominantColor2 = false;
        this.standardMode = false;
    }

    private boolean isEndOfLine() {
        this.debugMessage(this.printedPixels + " pixels draw so far (of " + this.width + ")");
        if (this.printedPixels == this.width) {
            this.debugMessage("Triggering new line");
            this.x = 0;
            this.y++;
            this.printedPixels = 0;

            return true;
        }

        return false;
    }

    private void draw(String hex, int qty) {

        Color color = new Color(0, 0, 0, 0);
        if (coloursMap.containsKey(hex)) {
            PaletteColor colorPalette = coloursMap.get(hex);
            color = new Color(colorPalette.getR(), colorPalette.getG(), colorPalette.getB(), 0);
        } else {
            System.out.println("Missing color in the palette: " + hex);
        }

        this.debugMessage("Wrinting " + qty + "  *  " + hex + " at " + this.x + " " + this.y);
        int qtyCpy = qty;

        while (qtyCpy > 0) {
            this.img.setRGB(this.x, this.y, color.getRGB());
            this.printedPixels++;
            qtyCpy--;
            this.x++;

            if (this.isEndOfLine()) {
                qty = qtyCpy;
                if (qty > 0) {
                    this.draw(hex, qty);
                    qtyCpy = 0;
                }
            }
        }
    }
}
