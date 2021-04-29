package pitch;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import tools.BinaryService;
import tools.LoggerService;
import tools.PaletteExtractor;

public class PitchToBmpConverterTest {
    @Test
    public void testConvert() throws IOException, NoSuchAlgorithmException {
        LoggerService mockedLogger = Mockito.mock(LoggerService.class);
        PitchToBmpConverter converter = new PitchToBmpConverter(mockedLogger);
        PaletteExtractor palette = new PaletteExtractor("USM-Colour-Palette.csv");
        BinaryService binaryService = new BinaryService();   

        String inputPath = "src/test/assets/pitch/PITCH_RN.SPR";
        String targetPath = "src/test/assets/pitch/pitch_rn_test.bmp";
        String outputPath = "src/test/assets/pitch/pitch_rn_test2.bmp";
        
        converter.convert(
                palette.extractForConversionToBmp(),
                binaryService.getFileContent(inputPath),
                670,
                305,
                outputPath
        );
        
        assertEquals(binaryService.getFileCheckSum(targetPath), binaryService.getFileCheckSum(targetPath));
    }
}
