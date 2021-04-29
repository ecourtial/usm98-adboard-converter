package pitch;

import java.io.IOException;
import pitch.tools.FileSuffix;
import tools.BinaryService;
import tools.PaletteExtractor;

public class PitchService {
    private static final int PITCH_IMAGE_WIDTH = 670;
    private static final int PITCH_IMAGE_HEIGHT = 305;

    private final PaletteExtractor paletteExtractor;
    private final PitchToBmpConverter pitchToBmpConverter;
    private final PitchToSprConverter pitchToSprConverter;
    private final BinaryService binaryExtractor;
    private final FileSuffix fileSuffix;

    public PitchService(
        PaletteExtractor paletteExtractor,
        PitchToBmpConverter pitchToBmpConverter,
        PitchToSprConverter pitchToSprConverter,
        BinaryService binaryExtractor,
        FileSuffix fileSuffix
    ) {
        this.paletteExtractor = paletteExtractor;
        this.pitchToBmpConverter = pitchToBmpConverter;
        this.pitchToSprConverter = pitchToSprConverter;
        this.binaryExtractor = binaryExtractor;
        this.fileSuffix = fileSuffix;
    }

    public void convertToBmp(int key) throws IOException, Exception {
        String fileName = this.fileSuffix.getFileCorrespondence(key);
        this.pitchToBmpConverter.convert(
            this.paletteExtractor.extractForConversionToBmp(),
            this.binaryExtractor.getFileContent(fileName + ".spr"),
            PitchService.PITCH_IMAGE_WIDTH,
            PitchService.PITCH_IMAGE_HEIGHT,
            fileName + ".bmp"
        );
    }

    public void convertToSpr(int key) throws IOException, Exception {
        String fileName = this.fileSuffix.getFileCorrespondence(key);
        this.binaryExtractor.writeHexString(
            fileName + ".spr",
            this.pitchToSprConverter.convert(
                this.paletteExtractor.extractForConversionToSpr(),
                fileName + ".bmp",
                PitchService.PITCH_IMAGE_WIDTH,
                PitchService.PITCH_IMAGE_HEIGHT
            )
        );
    }
}
