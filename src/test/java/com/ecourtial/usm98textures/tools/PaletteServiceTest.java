package com.ecourtial.usm98textures.tools;

import java.awt.Color;
import tools.PaletteService;
import tools.PaletteColor;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import tools.LoggerService;

public class PaletteServiceTest {
    @Test
    public void testExtractForConversionToBmp() throws Exception {
        LoggerService loggerService = Mockito.mock(LoggerService.class);
        PaletteService instance = new PaletteService("src/test/assets/USM-Colour-Palette-test.csv", loggerService);
        
        Map<String, PaletteColor> expResult = new HashMap < > ();
        expResult.put("00", new PaletteColor(255, 0, 255));
        expResult.put("01", new PaletteColor(255, 111, 111));
                
        assertEquals(255, instance.getByHexValue("00").getRed());
        assertEquals(0, instance.getByHexValue("00").getGreen());
        assertEquals(255, instance.getByHexValue("00").getBlue());
        
        assertEquals(255, instance.getByHexValue("01").getRed());
        assertEquals(111, instance.getByHexValue("01").getGreen());
        assertEquals(111, instance.getByHexValue("01").getBlue());
    }

    @Test
    public void testExtractForConversionToSpr() throws Exception {
        LoggerService loggerService = Mockito.mock(LoggerService.class);
        PaletteService instance = new PaletteService("src/test/assets/USM-Colour-Palette-test.csv", loggerService);

        Color color1 = new Color (255, 0, 255, 1);
        Color color2 = new Color (255, 111, 111, 1);
        
        assertEquals("00", instance.getByColor(color1));
        assertEquals("01", instance.getByColor(color2));
    }    
}
