package pitch.tools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LineContainerTest {
    @Test
    public void testBehavior() throws Exception {
        LineContainer container = new LineContainer();

        // First line
        Line line1 = new Line();
        ColorSequence sequence1 = new ColorSequence();
        sequence1.add("38");
        sequence1.add("35");
        sequence1.add("75");
        line1.add(sequence1);

        // Second line
        Line line2 = new Line();
        ColorSequence sequence2 = new ColorSequence();
        sequence2.add("01");
        line2.add(sequence2);

        ColorSequence sequence3 = new ColorSequence();
        sequence3.add("16");
        sequence3.add("77");
        line2.add(sequence3);

        // Now let's test
        container.add(line1);
        container.add(line2);

        assertEquals(2, container.getLinesCount());
        assertEquals(6, container.getBytesCount());
        assertEquals(line1, container.getLine(0));
        assertEquals(line2, container.getLine(1));
    }
}
