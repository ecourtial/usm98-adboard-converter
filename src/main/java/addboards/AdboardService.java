package addboards;

import com.ecourtial.usm98textures.tools.BinaryService;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.io.IOException;

public class AdboardService {
    private static final String OUTPUT_BMP_FILE_PATH = "AdBoards.bmp";
    private static final String OUTPUT_SPR_FILE_PATH = "Adboards.spr";
    private static final int ADDBOARDS_IMAGE_WIDTH = 85;
    private static final int ADDBOARDS_IMAGE_HEIGHT = 2244;

    private final PaletteExtractor paletteExtractor;
    private final AdboardToBmpConverter addBoardToBmpConverter;
    private final AdboardToSprConverter addBoardToSprConverter;
    private final BinaryService binaryExtractor;

    public AdboardService(
        PaletteExtractor paletteExtractor,
        AdboardToBmpConverter addBoartToBmpConverter,
        AdboardToSprConverter addBoardToSprConverter,
        BinaryService binaryExtractor
    ) {
        this.paletteExtractor = paletteExtractor;
        this.addBoardToBmpConverter = addBoartToBmpConverter;
        this.addBoardToSprConverter = addBoardToSprConverter;
        this.binaryExtractor = binaryExtractor;
    }

    public void convertToBmp() throws IOException {
        this.addBoardToBmpConverter.convert(this.paletteExtractor.extractForConversionToBmp(),
            this.binaryExtractor.getFileContent(AdboardService.OUTPUT_SPR_FILE_PATH),
            AdboardService.ADDBOARDS_IMAGE_WIDTH,
            AdboardService.ADDBOARDS_IMAGE_HEIGHT,
            AdboardService.OUTPUT_BMP_FILE_PATH
        );
    }

    public void convertToSpr() throws IOException, Exception {
        this.binaryExtractor.writeHexString(AdboardService.OUTPUT_SPR_FILE_PATH,
            this.addBoardToSprConverter.convert(this.paletteExtractor.extractForConversionToSpr(),
                AdboardService.OUTPUT_BMP_FILE_PATH,
                AdboardService.ADDBOARDS_IMAGE_WIDTH,
                AdboardService.ADDBOARDS_IMAGE_HEIGHT
            ),
            0
        );
    }
}
