/*
 * A sequence is just a collection of colors (bytes as string). This is used 
 * when converting from BMP to SPR.
 */
package pitch.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ColorSequence  implements Iterable<String> {
    private boolean uniqueColor = true;
    private int currentIndex = -1;
    private int currentFirstIndex = -1;
    private String firstAddedColor = null;
    private Map < Integer, String > sequence = new HashMap < > ();

    public void ColorSequence() {
        this.sequence = new HashMap < > ();
    }

    public int getSize() {
        return this.sequence.size();
    }

    public void add(String value) {
        if (this.firstAddedColor == null) {
            this.firstAddedColor = value;
            this.currentFirstIndex = 0;
        } else if (!value.equals(this.firstAddedColor)) {
            this.uniqueColor = false;
        }

        this.currentIndex++;
        this.sequence.put(this.currentIndex, value);
    }

    public String getFirstElement() throws Exception {
        if (this.sequence.containsKey(this.currentFirstIndex) == false) {
            throw new Exception("There is no first element in the sequence!");
        }
        return this.sequence.get(this.currentFirstIndex);
    }

    public String getLastElement() throws Exception {
        if (this.sequence.containsKey(this.currentIndex) == false) {
            throw new Exception("There is no last element in the sequence!");
        }
        return this.sequence.get(this.currentIndex);
    }

    public void removeFirstElement() throws Exception {
        if (this.sequence.containsKey(this.currentFirstIndex) == false) {
            throw new Exception("There is no first element in the sequence!");
        }
        this.sequence.remove(this.currentFirstIndex);
        this.currentFirstIndex++;

        if (this.sequence.isEmpty()) {
            this.reinit();
        }
    }

    public void removeLastElement() throws Exception {
        if (this.sequence.containsKey(this.currentIndex) == false) {
            throw new Exception("There is no last element in the sequence!");
        }
        this.sequence.remove(this.currentIndex);
        this.currentIndex--;

        if (this.sequence.isEmpty()) {
            this.reinit();
        }
    }

    public String getElement(int index) throws Exception {
        if (this.sequence.containsKey(index) == false) {
            throw new Exception("There is no such element in the sequence! Requested key was: " + index);
        }
        return this.sequence.get(index);
    }
    
    public boolean isUniqueColor() {
        return this.uniqueColor;
    }

    public void clear() {
        this.sequence.clear();
        this.reinit();
    }

    private void reinit() {
        this.currentFirstIndex = -1;
        this.currentIndex = -1;
        this.uniqueColor = true;
        this.firstAddedColor = null;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String> () {
            int iteratorIndex = currentFirstIndex;
            
            @Override
            public boolean hasNext() {
                return sequence.containsKey(this.iteratorIndex);
            }

            @Override
            public String next() {
                String element = sequence.get(this.iteratorIndex);
                this.iteratorIndex++;
                
                return element;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("no changes allowed");
            }
        };
    }
}
