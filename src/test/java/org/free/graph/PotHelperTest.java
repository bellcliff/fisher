package org.free.graph;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;


/**
 * Created by byang1 on 8/4/16.
 */
public class PotHelperTest {
    private BufferedImage blueImage, redImage;

    BufferedImage getImage(String name) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        return ImageIO.read(new File(classLoader.getResource(name + ".png").getFile()));
    }

    @org.junit.Before
    public void setUp() throws Exception {
        redImage = getImage("R");
    }

    @org.junit.Test
    public void getRedPoint() throws Exception {
        try {
            new PotHelper(redImage).getPotRectangle().getX();
        } catch (IOException ex){

        }
    }
//
//    @org.junit.Test
//    public void getRedPot() throws Exception {
//        ImageIO.write(new PotHelper(blueImage).getPotImage(PotHelper.PotType.BLUE), "png", new File("build/blue.png"));
//        ImageIO.write(new PotHelper(redImage).getPotImage(PotHelper.PotType.RED), "png", new File("build/red.png"));
//    }
//
//    @Test
//    public void markRed() throws Exception {
//        ImageIO.write(new PotHelper(getImage("1")).markRed(), "png", new File("1-r.png"));
//        ImageIO.write(new PotHelper(getImage("2")).markRed(), "png", new File("2-r.png"));
//        ImageIO.write(new PotHelper(getImage("3")).markRed(), "png", new File("3-r.png"));
//    }

}