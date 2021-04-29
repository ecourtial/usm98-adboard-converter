package pitch.tools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SequencesContainerTest {
    @Test
    public void testBehavior() throws Exception {
        SequencesContainer container = new SequencesContainer();

        // First line
        ColorSequence sequence1 = new ColorSequence();
        sequence1.add("38");
        sequence1.add("35");
        sequence1.add("75");

        // Second line
        ColorSequence sequence2 = new ColorSequence();
        sequence2.add("01");

        ColorSequence sequence3 = new ColorSequence();
        sequence3.add("16");
        sequence3.add("77");

        // Now let's test
        container.add(sequence1);
        container.add(sequence2);
        container.add(sequence3);

        assertEquals(3, container.getSequencesCount());
        assertEquals(6, container.getBytesCount());
        assertEquals(sequence1, container.getSequence(0));
        assertEquals(sequence2, container.getSequence(1));
        assertEquals(sequence3, container.getSequence(2));
    }
}
