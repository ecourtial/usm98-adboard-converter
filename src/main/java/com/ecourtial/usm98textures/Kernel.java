package com.ecourtial.usm98textures;

import addboards.AdboardsService;
import addboards.AdboardsToBmpConverter;
import addboards.AdboardToSprConverter;
import com.ecourtial.usm98textures.tools.BinaryService;
import com.ecourtial.usm98textures.tools.PaletteExtractor;
import java.io.IOException;

public class Kernel extends Thread {

    private static final String PALETTE_PATH = "USM-Colour-Palette.csv";

    private AdboardsService addboardService;
    private String action = "noDefinedActionSofar";
    private final USMTextureManager ui;

    Kernel(USMTextureManager userInterface) {
        this.ui = userInterface;
    }

    // Set the action to run in the thread
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void run() {
        try {
            switch (this.action) {
                case "addboardsToBmp":
                    this.getAddboardsService().convertToBmp();
                    break;
                case "addboardsToSpr":
                    this.getAddboardsService().convertToSpr();
                    break;
                default:
                    throw new Exception("Unknown action: '" + this.action + "'");
            }

            this.ui.log("Done!");
        } catch (Throwable throwable) {
            this.ui.log("An error occured:");
            this.ui.log(throwable.getMessage());
        }

        this.ui.enableUi(true);
    }

    // Used to generate the service, once, with DI.
    private AdboardsService getAddboardsService() throws IOException, Exception {

        if (this.addboardService == null) {
            this.addboardService = new AdboardsService(
                new PaletteExtractor(Kernel.PALETTE_PATH),
                new AdboardsToBmpConverter(),
                new AdboardToSprConverter(),
                new BinaryService()
            );
        }

        return this.addboardService;
    }
}