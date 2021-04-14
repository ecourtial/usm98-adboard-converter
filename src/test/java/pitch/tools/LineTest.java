package pitch.tools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LineTest {
    @Test
    public void testBehavior() throws Exception {
        Line line = new Line();
        
        // Add a first sequence
        ColorSequence sequence1 = new ColorSequence();
        sequence1.add("38");
        sequence1.add("35");
        sequence1.add("75");
        line.add(sequence1);
        
        assertEquals(1, line.getSequencesCount());
        assertEquals(3, line.getBytesCount());
        assertEquals(sequence1, line.getSequence(0));
        
        // Add more
        ColorSequence sequence2 = new ColorSequence();
        sequence2.add("01");
        line.add(sequence2);
        
        ColorSequence sequence3 = new ColorSequence();
        sequence3.add("72");
        sequence3.add("73");
        line.add(sequence3);
        
        assertEquals(3, line.getSequencesCount());
        assertEquals(6, line.getBytesCount());
        assertEquals(sequence1, line.getSequence(0));
        assertEquals(sequence2, line.getSequence(1));
        assertEquals(sequence3, line.getSequence(2));
    }
}
