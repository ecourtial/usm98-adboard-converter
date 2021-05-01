package addboards;

import adboards.AdboardToSprConverter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import tools.BinaryService;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import tools.LoggerService;
import tools.PaletteService;

public class AdboardToSprConverterTest {

    @Test
    public void testConvert() throws IOException, Exception, Throwable {
        // Create a basic BMP (same as in the toBmpConverter test)
        String bmpHexContent = "42 4D 46 00 00 00 00 00 00 00 36 00 00 00 28 00 00 00 02 00 00 00 02 00 00 00 01 00 18 00 00 00 00 00 10 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 04 B5 AA 33 2A 00 00 AA 33 2A 02 04 B5 00 00";
        BinaryService binaryService = new BinaryService();
        String outputPath = "src/test/assets/outputBmpTest2.bmp";
        binaryService.writeHexString(outputPath, bmpHexContent.replace(" ", ""));
        LoggerService mockedLogger = Mockito.mock(LoggerService.class);
        PaletteService mockedPalette = Mockito.mock(PaletteService.class);


        File file = new File(outputPath);
        BufferedImage image = ImageIO.read(file);

        Color color1 = new Color(image.getRGB(0, 0), true);
        Color color2 = new Color(image.getRGB(1, 0), true);

        Mockito.when(mockedPalette.getByColor(color1)).thenReturn("35");
        Mockito.when(mockedPalette.getByColor(color2)).thenReturn("38");

        // Test
        AdboardToSprConverter converter = new AdboardToSprConverter(mockedLogger, mockedPalette);

        assertEquals(
            "35383835",
            converter.convert(outputPath, 2, 2)
        );
    }
}
