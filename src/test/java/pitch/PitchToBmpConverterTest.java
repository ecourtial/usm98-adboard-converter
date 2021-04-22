package pitch;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tools.BinaryService;
import tools.Logger;
import tools.PaletteExtractor;

public class PitchToBmpConverterTest {
    @Test
    public void testConvert() throws IOException, NoSuchAlgorithmException {
        PitchToBmpConverter converter = new PitchToBmpConverter(new Logger(), false);
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
