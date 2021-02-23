package addboards;

import com.ecourtial.usm98textures.Kernel;
import com.ecourtial.usm98textures.tools.BinaryService;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.io.IOException;

public class AddboardService {
    private static final String outputBmpFilePath = "AddBoard.bmp";
    private static final String outputSprFilePath = "Adboards.spr";
    private static final int addboardsImageWidth = 85;
 private static final int addboardsImageHeight = 2244;   
    
    private PaletteExtractor paletteExtractor;
    private AddboardToBmpConverter addBoardToBmpConverter;
    private final AddboardToSprConverter addBoardToSprConverter;
    private final BinaryService binaryExtractor;

    public AddboardService(
            PaletteExtractor paletteExtractor,
            AddboardToBmpConverter addBoartToBmpConverter,
            AddboardToSprConverter addBoardToSprConverter,
            BinaryService binaryExtractor
    ) {
        this.paletteExtractor = paletteExtractor;
        this.addBoardToBmpConverter = addBoartToBmpConverter;
        this.addBoardToSprConverter = addBoardToSprConverter;
        this.binaryExtractor = binaryExtractor;
    }

    public void convertToBmp() throws IOException {
        this.addBoardToBmpConverter.convert(this.paletteExtractor.extractForConversionToBmp(),
                this.binaryExtractor.getFileContent(Kernel.AddboardPath),
                AddboardService.addboardsImageWidth,
                AddboardService.addboardsImageHeight,
                AddboardService.outputBmpFilePath
        );
    }
    
    public void convertToSpr() throws IOException, Exception {
        this.binaryExtractor.writeHexString(
                AddboardService.outputSprFilePath,
                this.addBoardToSprConverter.convert (
                        this.paletteExtractor.extractForConversionToSpr(),
                    AddboardService.outputBmpFilePath,
                    AddboardService.addboardsImageWidth,
                    AddboardService.addboardsImageHeight
            ),
                0
        );
    }
}
