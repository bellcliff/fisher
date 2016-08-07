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
        ClassLoader classLoader = getClass().getClassLoader();
        System.out.println("file resource: " + classLoader.getResource("blue.png"));
        blueImage = ImageIO.read(new File(classLoader.getResource("blue.png").getFile()));
        redImage = ImageIO.read(new File(classLoader.getResource("red.png").getFile()));
//        int w = blueImage.getWidth(), h = blueImage.getHeight();
//        IntStream.range(0, w).boxed()
//                .flatMap(x -> IntStream.range(0, h).mapToObj(y -> new Point(x, y)))
//                .map(p -> new Color(blueImage.getRGB(p.x, p.y)))
//                .collect(Collectors.toList());
    }

    @org.junit.Test
    public void getBluePoint() throws Exception {
        assertEquals(new PotHelper(blueImage).getBluePoint().getX(), 701.0);
    }

    @org.junit.Test
    public void getRedPoint() throws Exception {
        assertEquals(new PotHelper(redImage).getRedPoint().getX(), 467.0);
    }

    @org.junit.Test
    public void getRedPot() throws Exception {
        ImageIO.write(new PotHelper(blueImage).getPotImage(PotHelper.PotType.BLUE), "png", new File("build/blue.png"));
        ImageIO.write(new PotHelper(redImage).getPotImage(PotHelper.PotType.RED), "png", new File("build/red.png"));
    }

    @Test
    public void markRed() throws Exception {
        ImageIO.write(new PotHelper(getImage("1")).markRed(), "png", new File("1-r.png"));
        ImageIO.write(new PotHelper(getImage("2")).markRed(), "png", new File("2-r.png"));
        ImageIO.write(new PotHelper(getImage("3")).markRed(), "png", new File("3-r.png"));
    }

}