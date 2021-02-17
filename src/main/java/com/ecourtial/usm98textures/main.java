package com.ecourtial.usm98textures;

import addboards.AddboardService;
import addboards.AddboardToBmpConverter;
import addboards.AddboardToSprConverter;
import com.ecourtial.usm98textures.tools.BinaryService;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.io.IOException;

public class main {
    
        private static final String PalettePath = "USM-Colour-Palette.csv";
        public static final String AddboardPath = "ADBOARDS.SPR";
    
        public static void main(String[] args) throws IOException, Exception {
            AddboardService addboardService = new AddboardService(
                    new PaletteExtractor(main.PalettePath),
                    new AddboardToBmpConverter(),
                    new AddboardToSprConverter(),
                    new BinaryService()
            );
          
            //addboardService.convertToBmp();
            addboardService.convertToSpr();
    }
}
