/*
 * A line represents all the lines in the file. It is just a container for lines, in order. This is used 
 * when converting from BMP to SPR.
 */
package pitch.tools;

import java.util.HashMap;
import java.util.Map;

public class LineContainer {
    private final Map < Integer, Line > lines = new HashMap < > ();
    private int currentIndex = 0;

    public void add(Line line) {
        this.lines.put(this.currentIndex, line);
        this.currentIndex++;
    }

    public int getLinesCount() {
        return this.lines.size();
    }

    public int getBytesCount() {
        int localCount = 0;
        for (int i = 0; i < this.lines.size(); i++) {
            localCount += this.lines.get(i).getBytesCount();
        }

        return localCount;
    }

    public Line getLine(int index) {
        return this.lines.get(index);
    }
}
