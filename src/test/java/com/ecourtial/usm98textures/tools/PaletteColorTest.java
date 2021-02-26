package com.ecourtial.usm98textures.tools;

import tools.PaletteColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaletteColorTest {

    @Test
    public void testGetR() {
        System.out.println("Palette Color");
        PaletteColor instance = new PaletteColor(255, 0, 250);
        assertEquals(instance.getR(), 255);
        assertEquals(instance.getG(), 0);
        assertEquals(instance.getB(), 250);
    }
}
