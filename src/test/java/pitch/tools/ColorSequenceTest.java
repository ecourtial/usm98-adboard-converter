package pitch.tools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ColorSequenceTest {
    @Test
    public void testBehavior() throws Exception {
        ColorSequence sequence = new ColorSequence();

        // Test add
        sequence.add("First value");
        assertEquals(1, sequence.getSize());
        assertTrue(sequence.isUniqueColor());
        assertEquals("First value", sequence.getElement(0));
        assertEquals("First value", sequence.getFirstElement());
        assertEquals("First value", sequence.getLastElement());

        // Add more values
        sequence.add("First value");
        assertEquals(2, sequence.getSize());
        assertTrue(sequence.isUniqueColor());

        sequence.add("Second value");
        assertEquals(3, sequence.getSize());
        assertFalse(sequence.isUniqueColor());

        sequence.add("Third value");
        assertEquals(4, sequence.getSize());
        assertEquals("First value", sequence.getElement(0));
        assertEquals("First value", sequence.getElement(1));
        assertEquals("Second value", sequence.getElement(2));
        assertEquals("Third value", sequence.getElement(3));
        assertEquals("First value", sequence.getFirstElement());
        assertEquals("Third value", sequence.getLastElement());

        // Play with values
        sequence.removeFirstElement();
        sequence.removeLastElement();

        assertEquals("First value", sequence.getElement(1));
        assertEquals("Second value", sequence.getElement(2));
        assertEquals("First value", sequence.getFirstElement());
        assertEquals("Second value", sequence.getLastElement());

        assertFalse(sequence.isUniqueColor());

        sequence.removeFirstElement();
        sequence.removeFirstElement();
        assertEquals(0, sequence.getSize());
        sequence.add("Fourth value");
        assertEquals("Fourth value", sequence.getFirstElement());
        assertEquals("Fourth value", sequence.getLastElement());
        assertEquals("Fourth value", sequence.getElement(0));

        // Try collision
        sequence = new ColorSequence();
        sequence.add("First value");
        sequence.add("Second value");
        sequence.add("Third value");

        sequence.removeFirstElement();
        sequence.removeFirstElement();
        assertEquals("Third value", sequence.getFirstElement());
        sequence.removeLastElement();
        assertEquals(0, sequence.getSize());

        sequence.add("A New First value");
        assertEquals(1, sequence.getSize());
        assertEquals("A New First value", sequence.getFirstElement());
        assertEquals("A New First value", sequence.getLastElement());
        
        // Clear
        sequence = new ColorSequence();
        sequence.add("First value");
        assertEquals(1, sequence.getSize());
        assertEquals("First value", sequence.getElement(0));
        assertEquals("First value", sequence.getElement(0));
        sequence.clear();
        assertEquals(0, sequence.getSize());
        
        sequence.add("A New First value");
        assertEquals(1, sequence.getSize());
        assertEquals("A New First value", sequence.getFirstElement());
        assertEquals("A New First value", sequence.getLastElement());
    }
}
