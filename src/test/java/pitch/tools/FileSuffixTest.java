package pitch.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class FileSuffixTest {
    @Test
    public void testBehavior() throws Exception {
        FileSuffix suffixer = new FileSuffix();
        
        assertEquals("Pitch_ln", suffixer.getFileCorrespondence(0));
        assertEquals("Pitch_rn", suffixer.getFileCorrespondence(1));
        assertEquals("Pitch_ls", suffixer.getFileCorrespondence(2));
        assertEquals("Pitch_rs", suffixer.getFileCorrespondence(3));
        assertEquals("Pitch_lw", suffixer.getFileCorrespondence(4));
        assertEquals("Pitch_rw", suffixer.getFileCorrespondence(5));
        assertEquals("Pitch_lm", suffixer.getFileCorrespondence(6));
        assertEquals("Pitch_rm", suffixer.getFileCorrespondence(7));
    }
}
