package addboards;

import com.ecourtial.usm98textures.tools.PaletteColor;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdboardsToBmpConverterTest {

    @Test
    public void testConvert() throws Exception {
        System.out.println("convert to bmp");
        PaletteExtractor extractor = new PaletteExtractor("USM-Colour-Palette.csv");
        Map < String, PaletteColor > coloursMap = extractor.extractForConversionToBmp();
        byte[] fileContent = {
            53,
            56,
            56,
            53
        };
        int Width = 2;
        int Height = 2;
        String outputPath = "src/test/assets/outputBmpTest.bmp";
        AdboardsToBmpConverter instance = new AdboardsToBmpConverter();
        instance.convert(coloursMap, fileContent, Width, Height, outputPath);

        // Check content
        File file = new File(outputPath);
        BufferedImage image = ImageIO.read(file);

        for (int y = 0; y < Height; y++) {
            for (int x = 0; x < Width; x++) {
                Color color = new Color(image.getRGB(x, y), true);

                if (x == 0 && y == 0 || x == 1 && y == 1) {
                    assertEquals(42, color.getRed());
                    assertEquals(51, color.getGreen());
                    assertEquals(170, color.getBlue());
                } else if (x == 1 && y == 0 || x == 0 && y == 1) {
                    assertEquals(181, color.getRed());
                    assertEquals(4, color.getGreen());
                    assertEquals(2, color.getBlue());
                } else {
                    fail("Only 4 pixels were expected.");
                }
            }

        }
    }
}
