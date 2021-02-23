package com.ecourtial.usm98textures;

import addboards.AddboardService;
import addboards.AddboardToBmpConverter;
import addboards.AddboardToSprConverter;
import com.ecourtial.usm98textures.tools.BinaryService;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.io.IOException;

public class Kernel extends Thread {
    
        private static final String PalettePath = "USM-Colour-Palette.csv";
        public static final String AddboardPath = "ADBOARDS.SPR";
        
        private AddboardService addboardService;
        private String action;
        
        private AddboardService getService() throws IOException, Exception {
            
            if (this.addboardService == null) {
                this.addboardService = new AddboardService(
                    new PaletteExtractor(Kernel.PalettePath),
                    new AddboardToBmpConverter(),
                    new AddboardToSprConverter(),
                    new BinaryService()
            );
            }
            
            return this.addboardService;
    }
        
        public void setAction(String action) {
            this.action = action; 
        }
        
        @Override
        public void run(){
       System.out.println("MyThread running");
    }
        

        
    public void toBmp() throws Exception
    {
        this.getService().convertToBmp();
    }
    
    public void toSpr() throws Exception
    {
        this.getService().convertToSpr();
    }
}
