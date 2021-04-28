package pitch;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import pitch.tools.FileSuffix;
import tools.BinaryService;
import tools.PaletteColor;
import tools.PaletteExtractor;

public class PitchServiceTest {
    @Test
    public void testConvertToBmp() throws IOException, Exception {
        PaletteExtractor paletteExtractor = Mockito.mock(PaletteExtractor.class);
        PitchToBmpConverter pitchToBmpConverter = Mockito.mock(PitchToBmpConverter.class);
        PitchToSprConverter pitchToSprConverter = Mockito.mock(PitchToSprConverter.class);
        BinaryService binaryExtractor = Mockito.mock(BinaryService.class);
        FileSuffix fileSuffixMock = Mockito.mock(FileSuffix.class);

        PitchService service = new PitchService(paletteExtractor, pitchToBmpConverter, pitchToSprConverter, binaryExtractor, fileSuffixMock);

        Mockito.when(fileSuffixMock.getFileCorrespondence(1)).thenReturn("Pitch_rn");
        
        Map < String, PaletteColor > palette = new HashMap < > ();
        palette.put("00", new PaletteColor(255, 0, 255));
        palette.put("01", new PaletteColor(255, 111, 111));
        Mockito.when(paletteExtractor.extractForConversionToBmp()).thenReturn(palette);

        byte[] fileContent = {
            69,
            114,
            105,
            99
        };
        Mockito.when(binaryExtractor.getFileContent("Pitch_rn.spr")).thenReturn(fileContent);

        service.convertToBmp(1);

        Mockito.verify(pitchToBmpConverter, times(1)).convert(
            palette,
            fileContent,
            670,
            305,
            "Pitch_rn.bmp"
        );
    }

    @Test
    public void testConvertToSpr() throws IOException, Exception {
        PaletteExtractor paletteExtractor = Mockito.mock(PaletteExtractor.class);
        PitchToBmpConverter pitchToBmpConverter = Mockito.mock(PitchToBmpConverter.class);
        PitchToSprConverter pitchToSprConverter = Mockito.mock(PitchToSprConverter.class);
        BinaryService binaryExtractor = Mockito.mock(BinaryService.class);
        FileSuffix fileSuffixMock = Mockito.mock(FileSuffix.class);

        Mockito.when(fileSuffixMock.getFileCorrespondence(1)).thenReturn("Pitch_rn");
        
        Map < String, String > ToSprColoursMap = new HashMap < > ();
        ToSprColoursMap.put("ahah", "hoho");
        Mockito.when(paletteExtractor.extractForConversionToSpr()).thenReturn(ToSprColoursMap);

        String hexContent = "35383835";
        Mockito.when(pitchToSprConverter.convert(ToSprColoursMap, "Pitch_rn.bmp", 670, 305)).thenReturn(hexContent);

        PitchService service = new PitchService(paletteExtractor, pitchToBmpConverter, pitchToSprConverter, binaryExtractor, fileSuffixMock);
        service.convertToSpr(1);

        Mockito.verify(binaryExtractor, times(1)).writeHexString("Pitch_rn.spr", hexContent);
        Mockito.verify(paletteExtractor, times(1)).extractForConversionToSpr();
    }
}
