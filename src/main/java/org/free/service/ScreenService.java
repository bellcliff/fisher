package org.free.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by byang1 on 7/17/16.
 */
@Service
public class ScreenService {
    private final Robot robot;
    private final Toolkit toolkit;
    private Rectangle defaultScreenRectangle;

    ScreenService() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }

        toolkit = Toolkit.getDefaultToolkit();

        Dimension dimension = getScreenSize();
        int x = dimension.width / 4;
        int y = dimension.height / 4;
        this.defaultScreenRectangle = new Rectangle(x, y, dimension.width / 2, 100 + dimension.height / 2);
    }

    @Cacheable
    public Dimension getScreenSize() {
        return toolkit.getScreenSize();
    }

    @Cacheable
    public Rectangle getScreenRectangle() {
        Dimension screenSize = getScreenSize();
        return new Rectangle(screenSize.width, screenSize.height);
    }

    public void updateDefaultScreenRectangle(Rectangle rectangle) {
        this.defaultScreenRectangle = rectangle;
    }

    public Image getScreenImage(Rectangle rec) {
        return robot.createScreenCapture(rec);
    }

    public Image getScreenImage() {
        return getScreenImage(this.defaultScreenRectangle);
    }

    public Rectangle getFishFloats() {
        Image screenImage = getScreenImage();
        return new Rectangle();
    }

    public Dimension getRedBlock(BufferedImage image){
        final int w = image.getWidth();
        final int h = image.getHeight();
        return null;
    }

    public Dimension getBlueBlock(Image image){
        final int w = image.getWidth();
        final int h = image.getHeight();
        return null;
    }

//    public int getLightHeight(Image image){
//
//    }
}
