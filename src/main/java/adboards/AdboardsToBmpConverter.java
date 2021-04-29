package adboards;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import tools.LoggerService;
import tools.PaletteService;

public class AdboardsToBmpConverter {

    private final LoggerService logger;
    private final PaletteService paletteService;

    public AdboardsToBmpConverter(LoggerService logger, PaletteService paletteService) {
        this.logger = logger;
        this.paletteService = paletteService;
    }
    
    public void convert(byte[] fileContent, int Width, int Height, String outputPath) throws IOException {

        final BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_INT_RGB);
        int x = 0;
        int y = 0;

        for (int i = 0; i < fileContent.length; i++) {
            this.logger.log("Processing line #" + y);
            String hex = Integer.toHexString(fileContent[i]);
            if (hex.length() > 2) {
                hex = hex.substring(hex.length() - 2);
            }

            hex = hex.toUpperCase();

            if (hex.length() == 1) {
                hex = "0" + hex;
            }

            Color color = this.paletteService.getByHexValue(hex);

            img.setRGB(x, y, color.getRGB());
            x++;

            if (x == Width) {
                x = 0;
                y++;
            }

            if (y == Height) {
                break;
            }
        }

        RenderedImage rendImage = img;
        ImageIO.write(rendImage, "bmp", new File(outputPath));
    }
}
