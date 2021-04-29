package tools;

public class ValueCounter {
    private String value;
    private int count = 0;

    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public void increment() {
        this.count++;
    }
}
