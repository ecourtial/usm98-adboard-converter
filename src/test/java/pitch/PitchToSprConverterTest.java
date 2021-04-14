package pitch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tools.PaletteExtractor;

public class PitchToSprConverterTest {
    @Test
    public void testConvert() throws Exception {
        PitchToSprConverter converter = new PitchToSprConverter();
        PaletteExtractor palette = new PaletteExtractor("USM-Colour-Palette.csv");
        
        String prefix ="50414B3200021E7D";
        String dominantColors = "0A35";
        String expectedLine1 = "00380038c30A0D142041";
        String expectedLine2 = "40c00D824180";
        
        assertEquals(
                prefix + dominantColors + expectedLine1 + expectedLine2, 
                converter.convert(palette.extractForConversionToSpr(), "src/test/assets/pitch/pitch-sample.bmp", 8, 2)
        );
    }
}
