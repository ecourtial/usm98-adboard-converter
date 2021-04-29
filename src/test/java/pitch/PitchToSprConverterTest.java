package pitch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import tools.BinaryService;
import tools.LoggerService;
import tools.PaletteExtractor;

public class PitchToSprConverterTest {
    @Test
    public void testConvert() throws Exception {
        LoggerService mockedLogger = Mockito.mock(LoggerService.class);
        PitchToSprConverter converter = new PitchToSprConverter(mockedLogger);
        PaletteExtractor palette = new PaletteExtractor("USM-Colour-Palette.csv");
        BinaryService binaryService = new BinaryService();        
        
        String content = converter.convert(palette.extractForConversionToSpr(), "src/test/assets/pitch/pitch_rn.bmp", 670, 305);
        binaryService.writeHexString("src/test/assets/pitch/TEST.SPR", content);
        
        String targetChecksum = binaryService.getFileCheckSum("src/test/assets/pitch/PITCH_RN.SPR");
        String currentChecksum = binaryService.getFileCheckSum("src/test/assets/pitch/TEST.SPR");
        
        assertEquals(targetChecksum, currentChecksum);
    }
}
