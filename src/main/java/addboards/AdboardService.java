package addboards;

import com.ecourtial.usm98textures.main;
import com.ecourtial.usm98textures.tools.BinaryExtractor;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.io.IOException;

public class AdboardService {
    
    private PaletteExtractor paletteExtractor;
    private AddboardToBmpConverter addBoardToBmpConverter;
    private final BinaryExtractor binaryExtractor;

    public AdboardService(
            PaletteExtractor paletteExtractor,
            AddboardToBmpConverter addBoartToBmpConverter,
            BinaryExtractor binaryExtractor
    ) {
        this.paletteExtractor = paletteExtractor;
        this.addBoardToBmpConverter = addBoartToBmpConverter;
        this.binaryExtractor = binaryExtractor;
    }

    public void convertToBmp() throws IOException {
        this.addBoardToBmpConverter.convert(
                this.paletteExtractor.extractForConversionToBmp(),
                this.binaryExtractor.getFileContent(main.AddboardPath)
        );
    }
    
    public void convertToSpr() {
        
    }
}
