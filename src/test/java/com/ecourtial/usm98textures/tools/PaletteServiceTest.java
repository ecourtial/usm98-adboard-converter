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
       
        Map<String, PaletteColor> expResult = new HashMap < > ();
        expResult.put("00", new PaletteColor(180, 81, 25));
        expResult.put("01", new PaletteColor(215, 11, 28));
        expResult.put("02", new PaletteColor(255, 111, 33));

         PaletteService instance = new PaletteService("src/test/assets/USM-Colour-Palette-test.csv", loggerService, false);        
                
        assertEquals(180, instance.getByHexValue("00").getRed());
        assertEquals(81, instance.getByHexValue("00").getGreen());
        assertEquals(25, instance.getByHexValue("00").getBlue());
        
        assertEquals(215, instance.getByHexValue("01").getRed());
        assertEquals(11, instance.getByHexValue("01").getGreen());
        assertEquals(28, instance.getByHexValue("01").getBlue());
        
        assertEquals(255, instance.getByHexValue("02").getRed());
        assertEquals(111, instance.getByHexValue("02").getGreen());
        assertEquals(33, instance.getByHexValue("02").getBlue());
        
        assertEquals(181, instance.getByHexValue("WHATEVER").getRed());
        assertEquals(4, instance.getByHexValue("WHATEVER").getGreen());
        assertEquals(2, instance.getByHexValue("WHATEVER").getBlue());
    }

    @Test
    public void testExtractForConversionToSpr() throws Exception {
        LoggerService loggerService = Mockito.mock(LoggerService.class);
        
        Color color1 = new Color (180, 81, 25, 1);
        Color color2 = new Color (215, 11, 28, 1);
        Color color3 = new Color(255, 111, 33);
        Color defaultColor = new Color (250, 114, 32, 1); // These values do not repesents anything in the palette
        
        // Test with no color substitution
        PaletteService instance = new PaletteService("src/test/assets/USM-Colour-Palette-test.csv", loggerService, false);
        
        assertEquals("00", instance.getByColor(color1));
        assertEquals("01", instance.getByColor(color2));
        assertEquals("02", instance.getByColor(color3));
        assertEquals("38", instance.getByColor(defaultColor));
        
         // Test with color substitution
        instance = new PaletteService("src/test/assets/USM-Colour-Palette-test.csv", loggerService, true);
        
        assertEquals("00", instance.getByColor(color1));
        assertEquals("01", instance.getByColor(color2));
        assertEquals("02", instance.getByColor(color3));
        assertEquals("03", instance.getByColor(defaultColor));
    }    
}
