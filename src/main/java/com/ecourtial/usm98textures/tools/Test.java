/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecourtial.usm98textures.tools;

import com.twelvemonkeys.imageio.plugins.bmp.BMPImageReader;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author ECO
 */
public class Test {

    public void chou() {
        System.out.println("AHAH CHOUUUUUUU");

        
        
        
        //BufferedImage img = map(320, 160);
        //savePNG(img, "/Users/eric/Desktop/test.bmp");

        //BMPImageReader reader = new BMPImageReader();
        //File file = new File("test2.bmp");
        //BufferedImage image = ImageIO.read(file);
        //System.out.println("Voici la hauteur de l'image : " + image.getHeight() + " et la première couleur est : " + image.getRGB(1, 1));
        //System.out.println(image.getType());
    }

    private static BufferedImage map(int sizeX, int sizeY) {
        final BufferedImage res = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                res.setRGB(x, y, Color.WHITE.getRGB());
            }
        }
        return res;
    }

    private static void savePNG(final BufferedImage bi, final String path) {
        try {
            RenderedImage rendImage = bi;
            ImageIO.write(rendImage, "bmp", new File(path));
            //ImageIO.write(rendImage, "PNG", new File(path));
            //ImageIO.write(rendImage, "jpeg", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
