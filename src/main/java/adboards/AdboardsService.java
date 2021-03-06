package adboards;

import tools.BinaryService;
import java.io.IOException;

public class AdboardsService {
    private static final String OUTPUT_BMP_FILE_PATH = "Adboards.bmp";
    private static final String OUTPUT_SPR_FILE_PATH = "Adboards.spr";
    private static final int ADDBOARDS_IMAGE_WIDTH = 85;
    private static final int ADDBOARDS_IMAGE_HEIGHT = 2244;

    private final AdboardsToBmpConverter addBoardToBmpConverter;
    private final AdboardToSprConverter addBoardToSprConverter;
    private final BinaryService binaryExtractor;

    public AdboardsService(
        AdboardsToBmpConverter addBoartToBmpConverter,
        AdboardToSprConverter addBoardToSprConverter,
        BinaryService binaryExtractor
    ) {
        this.addBoardToBmpConverter = addBoartToBmpConverter;
        this.addBoardToSprConverter = addBoardToSprConverter;
        this.binaryExtractor = binaryExtractor;
    }

    public void convertToBmp() throws IOException, Exception {
        this.addBoardToBmpConverter.convert(
            this.binaryExtractor.getFileContent(AdboardsService.OUTPUT_SPR_FILE_PATH),
            AdboardsService.ADDBOARDS_IMAGE_WIDTH,
            AdboardsService.ADDBOARDS_IMAGE_HEIGHT,
            AdboardsService.OUTPUT_BMP_FILE_PATH
        );
    }

    public void convertToSpr() throws IOException, Exception, Throwable {
        this.binaryExtractor.writeHexString(
            AdboardsService.OUTPUT_SPR_FILE_PATH,
            this.addBoardToSprConverter.convert(
                AdboardsService.OUTPUT_BMP_FILE_PATH,
                AdboardsService.ADDBOARDS_IMAGE_WIDTH,
                AdboardsService.ADDBOARDS_IMAGE_HEIGHT
            )
        );
    }
}
