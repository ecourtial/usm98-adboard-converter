package addboards;

import adboards.AdboardToSprConverter;
import adboards.AdboardsToBmpConverter;
import tools.BinaryService;
import tools.PaletteExtractor;
import org.junit.jupiter.api.Test;
import adboards.AdboardsService;
import java.util.HashMap;
import java.util.Map;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import tools.PaletteColor;

public class AdboardsServiceTest {
    private PaletteExtractor mockedPaletteExtractor;
    private AdboardsToBmpConverter mockedAdboardsToBmpConverter;
    private AdboardToSprConverter mockedAdboardToSprConverter;
    private BinaryService mockedBinaryService;


    @Test
    public void testConvertToBmp() throws Exception {
        System.out.println("convertToBmp");

        this.mockedPaletteExtractor = Mockito.mock(PaletteExtractor.class);
        this.mockedBinaryService = Mockito.mock(BinaryService.class);
        this.mockedAdboardsToBmpConverter = Mockito.mock(AdboardsToBmpConverter.class);

        AdboardsService adboardsService = new AdboardsService(
            this.mockedPaletteExtractor,
            this.mockedAdboardsToBmpConverter,
            this.mockedAdboardToSprConverter,
            this.mockedBinaryService
        );

        byte[] fileContent = {
            69,
            114,
            105,
            99
        };
        Mockito.when(this.mockedBinaryService.getFileContent("Adboards.spr")).thenReturn(fileContent);

        Map < String, PaletteColor > palette = new HashMap < > ();
        palette.put("00", new PaletteColor(255, 0, 255));
        palette.put("01", new PaletteColor(255, 111, 111));
        Mockito.when(this.mockedPaletteExtractor.extractForConversionToBmp()).thenReturn(palette);

        adboardsService.convertToBmp();

        Mockito.verify(this.mockedAdboardsToBmpConverter, times(1)).convert(
            palette,
            fileContent,
            85,
            2244,
            "Adboards.bmp"
        );
    }

    @Test
    public void testConvertToSpr() throws Exception {
        System.out.println("convertToSpr");

        this.mockedPaletteExtractor = Mockito.mock(PaletteExtractor.class);
        this.mockedBinaryService = Mockito.mock(BinaryService.class);
        this.mockedAdboardToSprConverter = Mockito.mock(AdboardToSprConverter.class);

        AdboardsService adboardsService = new AdboardsService(
            this.mockedPaletteExtractor,
            this.mockedAdboardsToBmpConverter,
            this.mockedAdboardToSprConverter,
            this.mockedBinaryService
        );

        Map < String, String > ToSprColoursMap = new HashMap < > ();
        ToSprColoursMap.put("ahah", "hoho");
        Mockito.when(this.mockedPaletteExtractor.extractForConversionToSpr()).thenReturn(ToSprColoursMap);

        String hexContent = "35383835";
        Mockito.when(this.mockedAdboardToSprConverter.convert(ToSprColoursMap, "Adboards.bmp", 85, 2244)).thenReturn(hexContent);

        adboardsService.convertToSpr();

        Mockito.verify(this.mockedBinaryService, times(1)).writeHexString("Adboards.spr", hexContent);
        Mockito.verify(this.mockedPaletteExtractor, times(1)).extractForConversionToSpr();
        Mockito.verify(this.mockedAdboardToSprConverter, times(1)).convert(ToSprColoursMap, "Adboards.bmp", 85, 2244);
    }
}