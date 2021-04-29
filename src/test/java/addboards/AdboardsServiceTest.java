package addboards;

import adboards.AdboardToSprConverter;
import adboards.AdboardsToBmpConverter;
import tools.BinaryService;
import tools.PaletteService;
import org.junit.jupiter.api.Test;
import adboards.AdboardsService;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

public class AdboardsServiceTest {
    private AdboardsToBmpConverter mockedAdboardsToBmpConverter;
    private AdboardToSprConverter mockedAdboardToSprConverter;
    private BinaryService mockedBinaryService;


    @Test
    public void testConvertToBmp() throws Exception {
        this.mockedBinaryService = Mockito.mock(BinaryService.class);
        this.mockedAdboardsToBmpConverter = Mockito.mock(AdboardsToBmpConverter.class);

        AdboardsService adboardsService = new AdboardsService(
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

        adboardsService.convertToBmp();

        Mockito.verify(this.mockedAdboardsToBmpConverter, times(1)).convert(
            fileContent,
            85,
            2244,
            "Adboards.bmp"
        );
    }

    @Test
    public void testConvertToSpr() throws Exception {
        this.mockedBinaryService = Mockito.mock(BinaryService.class);
        this.mockedAdboardToSprConverter = Mockito.mock(AdboardToSprConverter.class);

        AdboardsService adboardsService = new AdboardsService(
            this.mockedAdboardsToBmpConverter,
            this.mockedAdboardToSprConverter,
            this.mockedBinaryService
        );

        String hexContent = "35383835";
        Mockito.when(this.mockedAdboardToSprConverter.convert("Adboards.bmp", 85, 2244)).thenReturn(hexContent);

        adboardsService.convertToSpr();

        Mockito.verify(this.mockedBinaryService, times(1)).writeHexString("Adboards.spr", hexContent);
    }
}
