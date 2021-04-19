/*
 * Represents the whole file. It is just a container for sequences, in order. This is used 
 * when converting from BMP to SPR.
 */
package pitch.tools;

import java.util.HashMap;
import java.util.Map;

public class SequencesContainer {
    private final Map < Integer, ColorSequence > sequences = new HashMap < > ();
    private int currentIndex = 0;

    public void add(ColorSequence sequence) {
        this.sequences.put(this.currentIndex, sequence);
        this.currentIndex++;
    }

    public int getSequencesCount() {
        return this.sequences.size();
    }

    public int getBytesCount() {
        int localCount = 0;
        for (int i = 0; i < this.sequences.size(); i++) {
            localCount += this.sequences.get(i).getSize();
        }

        return localCount;
    }

    public ColorSequence getSequence(int index) {
        return this.sequences.get(index);
    }
}
