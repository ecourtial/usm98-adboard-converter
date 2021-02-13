package com.ecourtial.usm98textures;

import addboards.AdboardService;
import addboards.AddboardToBmpConverter;
import com.ecourtial.usm98textures.tools.BinaryExtractor;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.io.IOException;

public class main {
    
        private static final String PalettePath = "USM-Colour-Palette.csv";
        public static final String AddboardPath = "ADBOARDS.SPR";
    
        public static void main(String[] args) throws IOException {
            AdboardService addboardService = new AdboardService(
                    new PaletteExtractor(main.PalettePath),
                    new AddboardToBmpConverter(),
                    new BinaryExtractor()
            );
          
            addboardService.convertToBmp();
    }
}
