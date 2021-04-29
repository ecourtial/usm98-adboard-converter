package com.ecourtial.usm98textures.tools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import tools.ValueCounter;

public class ValueCounterTest {
    @Test
    public void testBehavior() {
        ValueCounter counter = new ValueCounter();
        
        counter.setValue("Chou");
        counter.increment();
        assertEquals(1, counter.getCount());
        
        counter.increment();
        assertEquals(2, counter.getCount());
        
        counter.setCount(6);
        assertEquals(6, counter.getCount());
        
        counter.setValue("ChouChou");
        assertEquals("ChouChou", counter.getValue());
    }
}
