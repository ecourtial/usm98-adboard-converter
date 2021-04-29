package adboards;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import tools.LoggerService;

public class AdboardToSprConverter {

    private final LoggerService logger;

    public AdboardToSprConverter(LoggerService logger) {
        this.logger = logger;
    }
    
    public String convert(
        Map < String, String > coloursMap,
        String bmpFilePath,
        int width,
        int height

    ) throws IOException, Exception {
        File file = new File(bmpFilePath);
        BufferedImage image = ImageIO.read(file);

        if (image.getWidth() != width || image.getHeight() != height) {
            throw new Exception("Image must have a width of  " + width + " pixels and height must be of  " + height + "!");
        }

        if (image.getType() != 5) {
            throw new Exception("Image must be a valid BMP file!");
        }

        String outputString = "";

        for (int y = 0; y < image.getHeight(); y++) {
            this.logger.log("Processing line #" + y);
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                String colorString = color.getRed() + "-" + color.getGreen() + "-" + color.getBlue();

                if (coloursMap.containsKey(colorString)) {
                    outputString += coloursMap.get(colorString);
                } else {
                    outputString += "00"; // Black by default
                    this.logger.log("Color not found in palette: " + colorString);
                }
            }
        }

        return outputString;
    }

}
