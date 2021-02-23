package com.ecourtial.usm98textures;

import addboards.AddboardService;
import addboards.AddboardToBmpConverter;
import addboards.AddboardToSprConverter;
import com.ecourtial.usm98textures.tools.BinaryService;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.io.IOException;

public class Kernel extends Thread {

    private static final String PALETTE_PATH = "USM-Colour-Palette.csv";
    public static final String ADDBOARD_PATH = "ADBOARDS.SPR";

    private AddboardService addboardService;
    private String action = "noDefinedActionSofar";

    // Set the action to run in the thread
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void run() {
        switch (this.action) {
            case "addboardsToBmp": this.getAddboardsService().convertToBmp(); break;
            case "addboardsToSpr": this.getAddboardsService().convertToSpr(); break;
            default: throw new Exception("Unknown action: '" + this.action +  "'");
        }
    }
    
    // Used to generate the service, once, with DI.
    private AddboardService getAddboardsService() throws IOException, Exception {

        if (this.addboardService == null) {
            this.addboardService = new AddboardService(
                new PaletteExtractor(Kernel.PALETTE_PATH),
                new AddboardToBmpConverter(),
                new AddboardToSprConverter(),
                new BinaryService()
            );
        }

        return this.addboardService;
    }
}