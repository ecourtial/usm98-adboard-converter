package pitch;

import java.io.IOException;
import pitch.tools.FileSuffix;
import tools.BinaryService;

public class PitchService {
    private static final int PITCH_IMAGE_WIDTH = 670;
    private static final int PITCH_IMAGE_HEIGHT = 305;

    private final PitchToBmpConverter pitchToBmpConverter;
    private final PitchToSprConverter pitchToSprConverter;
    private final BinaryService binaryExtractor;
    private final FileSuffix fileSuffix;

    public PitchService(
        PitchToBmpConverter pitchToBmpConverter,
        PitchToSprConverter pitchToSprConverter,
        BinaryService binaryExtractor,
        FileSuffix fileSuffix
    ) {
        this.pitchToBmpConverter = pitchToBmpConverter;
        this.pitchToSprConverter = pitchToSprConverter;
        this.binaryExtractor = binaryExtractor;
        this.fileSuffix = fileSuffix;
    }

    public void convertToBmp(int key) throws IOException, Exception {
        String fileName = this.fileSuffix.getFileCorrespondence(key);
        this.pitchToBmpConverter.convert(
            this.binaryExtractor.getFileContent(fileName + ".spr"),
            PitchService.PITCH_IMAGE_WIDTH,
            PitchService.PITCH_IMAGE_HEIGHT,
            fileName + ".bmp"
        );
    }

    public void convertToSpr(int key) throws IOException, Exception, Throwable {
        String fileName = this.fileSuffix.getFileCorrespondence(key);
        this.binaryExtractor.writeHexString(fileName + ".spr",
            this.pitchToSprConverter.convert(
                fileName + ".bmp",
                PitchService.PITCH_IMAGE_WIDTH,
                PitchService.PITCH_IMAGE_HEIGHT
            )
        );
    }
}
