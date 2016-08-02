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
    public int all, succeed;
    private int dirIndex = 0, fileIndex = 0;
    private int baseLight;
    private List<BufferedImage> images = new ArrayList<>();
    private final Robot robot = new Robot();
    private boolean running = false;
    private File imgFolder = new File("img");

    public GraphHelper() throws AWTException {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        updateFishRectangle(new Rectangle(400, 50, dim.width - 800, 100));
    }

    public void start() {
        running = true;
        if (imgFolder.exists()){
            deleteFileOrFolder(imgFolder.toPath());
        }
    }

    public void updateFishRectangle(Rectangle rec) {
        System.out.println(rec);
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
            Rectangle fisherPotRectangle = getFisherPot(img);
            if (fisherPotRectangle == null)
                return;
            Fisher.fisher.updateFishPanel(img);
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
            saveFile();
            all++;
            Fisher.fisher.scanPanel.updateFish(all, succeed);
        }
    }

    private Rectangle getFisherPot(BufferedImage img) throws IOException {
        // images.add(img);
        // find a block with 4 * 4, which all full fil red check
        PotHelper ph = new PotHelper();
        Point p = ph.getRedPoint(img);
        if (p == null) return null;
        int left = p.x + Conf.scanLeft, top = p.y + Conf.scanTop, w = Conf.scanWidth, h = Conf.scanHeight;
        System.out.println(left + "-" + top + "-" + w + "-" + h+","+img.getWidth() + "," + img.getHeight());
        BufferedImage potImage = img.getSubimage(left, top, w, h);
        Fisher.fisher.scanPanel.updateImage(potImage, 0);
        baseLight = getRectangleLight(potImage);
        return new Rectangle(fishRectangle.x + left, fishRectangle.y + top, w, h);
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
        images.add(img);
        int light = getRectangleLight(img);
        System.out.println(light + "-" + baseLight + "-" + (light - baseLight));
        if (light > baseLight + Conf.EDITABLE_CONF.getScanLight()){
            Fisher.fisher.scanPanel.updatePot(light, baseLight, img);
            return true;
        }
        return false;
    }

    private boolean checkRed(BufferedImage img, int x, int y) {
        for (int x0 = x; x0 < x + Conf.scanBlock; x0++) {
            for (int y0 = y; y0 < y + Conf.scanBlock; y0++) {
                Color c = new Color(img.getRGB(x0, y0));
                if (c.getRed() < c.getGreen() + c.getBlue() + Conf.POT_COLOR_DIFF) {
                    return false;
                }
            }
        }
        return true;
    }

    private int getRGB(BufferedImage img, int x, int y) {
        Color c = new Color(img.getRGB(x, y));
        return c.getRed() + c.getBlue() + c.getGreen();
    }

    private boolean saveFile(BufferedImage img, int dir, int file) throws IOException {
        File imgFolder = new File("img");
        if (!imgFolder.exists() && !imgFolder.mkdir()) {
            System.out.println("img folder create fail:" + imgFolder);
            return false;
        }
        File fileFolder = new File(imgFolder, "" + dir);
        if (!fileFolder.exists() && fileFolder.mkdir()) {
            System.out.println("file folder create fail: " + fileFolder);
            return false;
        }
        File f = new File(fileFolder, file + ".jpg");
        ImageIO.write(img, "jpg", f);
        return true;
    }

    private void saveFile() throws IOException {
        if (!Conf.DEBUG)
            return;
        for (BufferedImage bi : images) {
            if (!saveFile(bi, dirIndex, fileIndex++)) break;
        }
        dirIndex++;
        fileIndex = 0;
        images.clear();
    }

    static void deleteFileOrFolder(final Path path) {
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
                        throws IOException {
                    Files.delete(file);
                    return CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(final Path file, final IOException e) {
                    return handleException(e);
                }

                private FileVisitResult handleException(final IOException e) {
                    e.printStackTrace(); // replace with more robust error handling
                    return TERMINATE;
                }

                @Override
                public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
                        throws IOException {
                    if (e != null) return handleException(e);
                    Files.delete(dir);
                    return CONTINUE;
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    ;
}
