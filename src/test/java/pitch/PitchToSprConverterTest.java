package pitch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import tools.BinaryService;
import tools.LoggerService;
import tools.PaletteService;

public class PitchToSprConverterTest {
    @Test
    public void testConvert() throws Exception {
        LoggerService mockedLogger = Mockito.mock(LoggerService.class);
        PaletteService palette = new PaletteService("USM-Colour-Palette.csv", mockedLogger, false);
        PitchToSprConverter converter = new PitchToSprConverter(mockedLogger, palette);
        BinaryService binaryService = new BinaryService();        
        
        String content = converter.convert("src/test/assets/pitch/pitch_rn.bmp", 670, 305);
        binaryService.writeHexString("src/test/assets/pitch/TEST.SPR", content);
        
        String targetChecksum = binaryService.getFileCheckSum("src/test/assets/pitch/PITCH_RN.SPR");
        String currentChecksum = binaryService.getFileCheckSum("src/test/assets/pitch/TEST.SPR");
        
        assertEquals(targetChecksum, currentChecksum);
    }
}
