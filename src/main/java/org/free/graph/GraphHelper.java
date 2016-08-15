package org.free.graph;

import org.free.MyAction;
import org.free.config.Conf;
import org.free.ui.Fisher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Formatter;

public class GraphHelper {
    private Rectangle fishRectangle;
    private Rectangle fisherPotRectangle;
    private Robot robot;
    private int baseLight;
    private int all, succeed;
    private boolean running = false;

    public GraphHelper() throws AWTException {
        robot = new Robot();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        fishRectangle = new Rectangle(500, 100, dim.width - 1000, 120);
    }

    public void start() {
        running = true;
    }

    public void updateFishRectangle(Rectangle rec) {
        fishRectangle = rec;
    }

    public Rectangle getFishRectangle() {
        return fishRectangle;
    }

    public void stop() {
        running = false;
    }

    public void run() throws Exception {
        long start = new Date().getTime();
        Thread.sleep(2000);
        try {
            initPot();
            while (new Date().getTime() - start < Conf.interval * 1000) {
                if (!loopPot()) break;
            }
        } finally {
            all++;
            Fisher.fisher.scanPanel.updateFish(all, succeed);
        }
    }

    private void initPot() throws IOException {
        BufferedImage img = robot.createScreenCapture(fishRectangle);
        PotHelper ph = new PotHelper(img);
        Rectangle potRect;
        try {
            potRect = ph.getPotRectangle();
            updatePotLight(img, potRect);
            fisherPotRectangle = getScreenPotRectangle(potRect);
            Fisher.fisher.updateFishPanel(img, img.getSubimage(potRect.x, potRect.y, potRect.width, potRect.height));
        } catch (IOException ex) {
            saveImage(img);
            throw ex;
        }
    }

    private boolean loopPot() throws InterruptedException, IOException {
        Thread.sleep(Conf.scanInterval);
        boolean finish = true;
        if (!running) {
            finish = false;
        }

        if (checkFloat(robot.createScreenCapture(fisherPotRectangle))) {
            Thread.sleep(500);
            MyAction.rightClick(fisherPotRectangle.x + 25, fisherPotRectangle.y + 25);
            succeed++;
            finish = false;
        }
        return finish;
    }

    private Rectangle getScreenPotRectangle(Rectangle rect) {
        return new Rectangle(rect.x + fishRectangle.x, rect.y + fishRectangle.y, rect.width, rect.height);
    }

    private void updatePotLight(BufferedImage img, Rectangle potRectangle) throws IOException {
        baseLight = getRectangleLight(img, potRectangle);
    }

    private int getRectangleLight(BufferedImage img, Rectangle rec)
            throws IOException {
        int light = 0;
        for (int x = Math.max(rec.x, 0); x < Math.min(rec.x + rec.width, img.getWidth()); x++)
            for (int y = Math.max(rec.y, 0); y < Math.min(rec.y + rec.height, img.getHeight()); y++)
                light += getRGB(img, x, y);

        return light / (rec.width * rec.height);
    }

    private boolean checkFloat(BufferedImage img) throws IOException {
        int light = getRectangleLight(img, new Rectangle(img.getWidth(), img.getHeight()));
        Fisher.fisher.scanPanel.updatePot(light, baseLight, img);
        return light >= baseLight + Conf.EDITABLE_CONF.getScanLight();
    }

    private int getRGB(BufferedImage img, int x, int y) {
        Color c = new Color(img.getRGB(x, y));
        return c.getRed() + c.getBlue() + c.getGreen();
    }

    private void saveImage(BufferedImage image) throws IOException {
        File imageFolder = new File(System.getProperty("user.home") + "/fisher");
        if (!imageFolder.exists() && !imageFolder.mkdirs()) throw new IOException("fail create image folder" + imageFolder);
        LocalDateTime date = LocalDateTime.now();
        String fileName = date.format(DateTimeFormatter.ISO_LOCAL_TIME).replace(':', '-') + ".png";
        File imgFile = new File(imageFolder, fileName);
        System.out.println(imgFile.getAbsolutePath());
        ImageIO.write(image, "PNG", imgFile);
    }
}
