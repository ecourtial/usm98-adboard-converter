package pitch;

import java.io.IOException;
import tools.BinaryService;
import tools.PaletteExtractor;

public class PitchService {
    private static final int PITCH_IMAGE_WIDTH = 670;
    private static final int PITCH_IMAGE_HEIGHT = 305;

    private final PaletteExtractor paletteExtractor;
    private final PitchToBmpConverter pitchToBmpConverter;
    private final BinaryService binaryExtractor;

    public PitchService(
        PaletteExtractor paletteExtractor,
        PitchToBmpConverter pitchToBmpConverter,
        BinaryService binaryExtractor
    ) {
        this.paletteExtractor = paletteExtractor;
        this.pitchToBmpConverter = pitchToBmpConverter;
        this.binaryExtractor = binaryExtractor;
    }

    public void convertToBmp(int key) throws IOException, Exception {
        String fileName = this.getFileCorrespondance(key);
        this.pitchToBmpConverter.convert(
            this.paletteExtractor.extractForConversionToBmp(),
            this.binaryExtractor.getFileContent(fileName + ".spr"),
            PitchService.PITCH_IMAGE_WIDTH,
            PitchService.PITCH_IMAGE_HEIGHT,
            fileName + ".bmp"
        );
    }
    
    private String getFileCorrespondance(int index) throws Exception {
        String value = "";
        
        switch(index) {
            case 0: value = "ln"; break;
            case 1: value = "rn"; break;
            case 2: value = "ls"; break;
            case 3: value = "rs"; break;
            case 4: value = "lw"; break;
            case 5: value = "rw"; break;
            case 6: value = "lm"; break;
            case 7: value = "rm"; break;
            default: throw new Exception("Unknown index for pitch correpondance: " + index + "!");
        }
        
        return "Pitch_" + value;
    }
}