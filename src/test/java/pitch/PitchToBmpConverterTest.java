package pitch;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import tools.BinaryService;
import tools.LoggerService;
import tools.PaletteService;

public class PitchToBmpConverterTest {
    @Test
    public void testConvert() throws IOException, NoSuchAlgorithmException, Exception {
        LoggerService mockedLogger = Mockito.mock(LoggerService.class);
        PaletteService palette = new PaletteService("USM-Colour-Palette.csv", mockedLogger, false);
        PitchToBmpConverter converter = new PitchToBmpConverter(mockedLogger, palette);
        BinaryService binaryService = new BinaryService();   

        String inputPath = "src/test/assets/pitch/Pitch_rn.spr";
        String targetPath = "src/test/assets/pitch/pitch_rn_test.bmp";
        String outputPath = "src/test/assets/pitch/pitch_rn_test2.bmp";
        
        converter.convert(
                binaryService.getFileContent(inputPath),
                670,
                305,
                outputPath
        );
        
        assertEquals(binaryService.getFileCheckSum(targetPath), binaryService.getFileCheckSum(targetPath));
    }
}
