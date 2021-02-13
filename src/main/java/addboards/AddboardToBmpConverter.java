package addboards;

import com.ecourtial.usm98textures.tools.PaletteColor;
import com.sun.javafx.scene.control.skin.ColorPalette;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;

public class AddboardToBmpConverter {

    private String AddboardOriginalPath;
    private final int Width;
    private final int Heigh;
    
    public AddboardToBmpConverter(String AddboardOriginalPath, int Width, int Heigh) {
        this.AddboardOriginalPath = AddboardOriginalPath;
        this.Width = Width;
        this.Heigh = Heigh;
    }
    
    public void convert(Map<String, PaletteColor> coloursMap, byte[] fileContent) throws IOException {
        
        final BufferedImage img = new BufferedImage(this.Width, this.Heigh, BufferedImage.TYPE_INT_RGB);
        int x = 0;
        int y = 0;
        
        
        for(int i = 0; i< fileContent.length; i++){
            String hex = Integer.toHexString( fileContent[i]);
            if (hex.length() > 2) {
                hex = hex.substring(hex.length() - 2);
            }
            
            //System.out.println( hex);
            
            //img.setRGB(x, y, Color.WHITE.getRGB());
            
            Color color = new Color(0, 0, 0, 0);
            
            if (coloursMap.containsKey(hex)) {
                PaletteColor colorPalette = coloursMap.get(hex);
                color = new Color(colorPalette.getR(), colorPalette.getG(), colorPalette.getB(), 0);
            } else {
                System.out.println( "Missing color in the palette: "  + hex);
            }
            
            img.setRGB(x, y, color.getRGB());
            x++;

            
            
            if (x == this.Width) {
                x = 0;
                y++;
            }
            
            if (y == this.Heigh)
            {
            break;
           }//
        }
        
        RenderedImage rendImage = img;
        ImageIO.write(rendImage, "bmp", new File("/Users/eric/Desktop/test.bmp"));
    }
   
}
