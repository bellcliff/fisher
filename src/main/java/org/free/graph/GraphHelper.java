package org.free.graph;

import org.free.config.Conf;
import org.free.MyAction;
import org.free.ui.Fisher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

public class GraphHelper {
    //	static boolean debug = true;
    public Rectangle fishRectangle;
    private int baseLight;
    private int all, succeed;
    private int dirIndex = 0, fileIndex = 0;
    private final Robot robot = new Robot();
    private boolean running = false;
    private File imgFolder = new File("img");

    public GraphHelper() throws AWTException {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        updateFishRectangle(new Rectangle(500, 100, dim.width - 1000, 200));
    }

    public void start() {
        running = true;
    }

    public void updateFishRectangle(Rectangle rec) {
        fishRectangle = rec;
    }

    public void stop(){
        running = false;
    }

    public void run() throws Exception {
        long start = new Date().getTime();
        Thread.sleep(4000);
        dirIndex = 0;
        fileIndex = 0;
        System.out.println("=====");
        try {

            BufferedImage img = robot.createScreenCapture(fishRectangle);
            PotHelper ph = new PotHelper(img);
            Rectangle potRect = ph.getPotRectangle(PotHelper.PotType.RED);
            if (potRect == null)
                return;
            baseLight = getRectangleLight(img, potRect);
            Rectangle fisherPotRectangle = getScreenPotRectangle(potRect);
            Fisher.fisher.updateFishPanel(img, ph.getPotImage(PotHelper.PotType.RED));
            while (new Date().getTime() - start < Conf.interval * 1000) {
                Thread.sleep(Conf.scanInterval);
                if (!running) {
                    break;
                }

                if (checkFloat(robot.createScreenCapture(fisherPotRectangle))) {
                    MyAction.rightClick(fisherPotRectangle.x + Conf.scanWidth / 2, fisherPotRectangle.y + Conf.scanHeight / 2);
                    succeed++;
                    break;
                }
            }
        } finally {
            all++;
            Fisher.fisher.scanPanel.updateFish(all, succeed);
        }
    }

    private Rectangle getScreenPotRectangle(Rectangle rect){
        if (rect == null) return null;
        return new Rectangle(rect.x + fishRectangle.x, rect.y + fishRectangle.y, rect.width, rect.height);
    }

    private int getRectangleLight(BufferedImage img) throws IOException {
        return getRectangleLight(img, new Rectangle(img.getWidth(), img.getHeight()));
    }

    private int getRectangleLight(BufferedImage img, Rectangle rec)
            throws IOException {
        int light = 0;
        for (int x = rec.x; x < rec.x + rec.width; x++)
            for (int y = rec.y; y < rec.y + rec.height; y++)
                light += getRGB(img, x, y);

        return light / (rec.width * rec.height);
    }

    private boolean checkFloat(BufferedImage img) throws IOException {
        int light = getRectangleLight(img);
        System.out.println((light - baseLight) + " " + light + " " + baseLight);
        if (light >= baseLight + Conf.EDITABLE_CONF.getScanLight()){
            Fisher.fisher.scanPanel.updatePot(light, baseLight, img);
            return true;
        }
        return false;
    }

    private int getRGB(BufferedImage img, int x, int y) {
        Color c = new Color(img.getRGB(x, y));
        return c.getRed() + c.getBlue() + c.getGreen();
    }
}
