package com.ecourtial.usm98textures.tools;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaletteExtractorTest {
    @Test
    public void testExtractForConversionToBmp() throws Exception {
        System.out.println("extractForConversionToBmp");
        PaletteExtractor instance = new PaletteExtractor("src/test/assets/USM-Colour-Palette-test.csv");
        
        Map<String, PaletteColor> expResult = new HashMap < > ();
        expResult.put("00", new PaletteColor(255, 0, 255));
        expResult.put("01", new PaletteColor(255, 111, 111));
        
        Map<String, PaletteColor> result = instance.extractForConversionToBmp();
        
        assertEquals(255, result.get("00").getR());
        assertEquals(0, result.get("00").getG());
        assertEquals(255, result.get("00").getB());
        
        assertEquals(255, result.get("01").getR());
        assertEquals(111, result.get("01").getG());
        assertEquals(111, result.get("01").getB());
    }

    @Test
    public void testExtractForConversionToSpr() throws Exception {
        System.out.println("extractForConversionToSpr");
        PaletteExtractor instance = new PaletteExtractor("src/test/assets/USM-Colour-Palette-test.csv");

        Map<String, String> result = instance.extractForConversionToSpr();
        assertEquals("00", result.get("255-0-255"));
        assertEquals("01", result.get("255-111-111"));
    }    
}
