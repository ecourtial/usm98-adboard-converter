package pitch;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import pitch.tools.FileSuffix;
import tools.BinaryService;

public class PitchServiceTest {
    @Test
    public void testConvertToBmp() throws IOException, Exception {
        PitchToBmpConverter pitchToBmpConverter = Mockito.mock(PitchToBmpConverter.class);
        PitchToSprConverter pitchToSprConverter = Mockito.mock(PitchToSprConverter.class);
        BinaryService binaryExtractor = Mockito.mock(BinaryService.class);
        FileSuffix fileSuffixMock = Mockito.mock(FileSuffix.class);

        PitchService service = new PitchService(pitchToBmpConverter, pitchToSprConverter, binaryExtractor, fileSuffixMock);

        Mockito.when(fileSuffixMock.getFileCorrespondence(1)).thenReturn("Pitch_rn");
        
        byte[] fileContent = {
            69,
            114,
            105,
            99
        };
        Mockito.when(binaryExtractor.getFileContent("Pitch_rn.spr")).thenReturn(fileContent);

        service.convertToBmp(1);

        Mockito.verify(pitchToBmpConverter, times(1)).convert(
            fileContent,
            670,
            305,
            "Pitch_rn.bmp"
        );
    }

    @Test
    public void testConvertToSpr() throws IOException, Exception, Throwable {
        PitchToBmpConverter pitchToBmpConverter = Mockito.mock(PitchToBmpConverter.class);
        PitchToSprConverter pitchToSprConverter = Mockito.mock(PitchToSprConverter.class);
        BinaryService binaryExtractor = Mockito.mock(BinaryService.class);
        FileSuffix fileSuffixMock = Mockito.mock(FileSuffix.class);

        Mockito.when(fileSuffixMock.getFileCorrespondence(1)).thenReturn("Pitch_rn");
        
        String hexContent = "35383835";
        Mockito.when(pitchToSprConverter.convert("Pitch_rn.bmp", 670, 305)).thenReturn(hexContent);

        PitchService service = new PitchService(pitchToBmpConverter, pitchToSprConverter, binaryExtractor, fileSuffixMock);
        service.convertToSpr(1);

        Mockito.verify(binaryExtractor, times(1)).writeHexString("Pitch_rn.spr", hexContent);
    }
}
