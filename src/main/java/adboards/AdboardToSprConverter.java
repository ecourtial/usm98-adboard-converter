package adboards;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import tools.LoggerService;
import tools.PaletteService;

public class AdboardToSprConverter {

    private final LoggerService logger;
    private final PaletteService paletteService;

    public AdboardToSprConverter(LoggerService logger, PaletteService paletteService) {
        this.logger = logger;
        this.paletteService = paletteService;
    }
    
    public String convert(
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
                 outputString += this.paletteService.getByColor(new Color(image.getRGB(x, y), true));
            }
        }

        return outputString;
    }
}
