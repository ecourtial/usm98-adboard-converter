package main;

import adboards.AdboardsService;
import adboards.AdboardsToBmpConverter;
import adboards.AdboardToSprConverter;
import tools.BinaryService;
import tools.PaletteExtractor;
import java.io.IOException;
import java.util.Arrays;
import pitch.PitchService;
import pitch.PitchToBmpConverter;
import pitch.PitchToSprConverter;
import pitch.tools.FileSuffix;
import tools.Logger;

public class Kernel extends Thread {

    private static final String PALETTE_PATH = "USM-Colour-Palette.csv";

    private AdboardsService addboardService;
    private PitchService pitchService;
    private String action = "noDefinedActionSofar";
    private final USMTextureManager ui;
    private boolean loggerEnabled = false;
    
    private int Param1FromGui;
    private final Logger logger;

    public Kernel(USMTextureManager userInterface, Logger logger) {
        this.ui = userInterface;
        this.logger = logger;
    }

    // Set the action to run in the thread
    public void setAction(String action) {
        this.action = action;
    }
    
    // Set the Param 1 from Gui
    public void setParam1(int Param) {
        this.Param1FromGui = Param;
    }

    // Enable or disable logs
    public void enableLog(boolean enabled) {
        this.loggerEnabled = enabled;
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
               case "pitchToBmp":
                    this.getPitchService().convertToBmp(this.Param1FromGui);
                    break;
               case "pitchToSpr":
                    this.getPitchService().convertToSpr(this.Param1FromGui);
                    break;
                default:
                    throw new Exception("Unknown action: '" + this.action + "'");
            }

            this.ui.log("Done!");
        } catch (Exception throwable) {
            this.ui.log("An error occured:");
            this.ui.log(throwable.getMessage());
            System.out.println(throwable.getMessage());
            System.out.println(Arrays.toString(throwable.getStackTrace()));
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
    
    // Used to generate the service, once, with DI.
    private PitchService getPitchService() {
        if (this.pitchService == null){
            this.pitchService = new PitchService(
               new PaletteExtractor(Kernel.PALETTE_PATH),
               new PitchToBmpConverter(this.logger, this.loggerEnabled),
               new PitchToSprConverter(this.logger, this.loggerEnabled),
               new BinaryService(),
               new FileSuffix()
            );
        }
        
        return this.pitchService;
    }
}
