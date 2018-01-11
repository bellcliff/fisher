package org.free.graph;

import org.free.config.Conf;
import org.free.ui.Fisher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PotHelper {

    private final BufferedImage image;
    private final Map<Point, RGB> rgbMap;
    private final Set<Map.Entry<Point, RGB>> rgbSet;

    PotHelper(BufferedImage image) {
        this.image = image;
        Raster raster = image.getRaster();
        rgbMap = new HashMap<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int[] rgba = raster.getPixel(x, y, new int[4]);
                rgbMap.put(
                        new Point(x, y),
                        new RGB(rgba)
                );
            }
        }
        rgbSet = rgbMap.entrySet();
    }

    Rectangle getPotRectangle() throws IOException {
        Point redPoint = getRedPoint();
        RGB rgb = rgbMap.get(redPoint);
        if (redPoint == null) throw new IOException("red point not found!");
        Fisher.fisher.scanPanel.updateRGB(rgb.r, rgb.b, rgb.g);
        return new Rectangle(
                Math.max(redPoint.x + Conf.scanLeft, 0),
                Math.max(redPoint.y + Conf.scanTop, 0),
                Math.min(Conf.scanWidth, image.getWidth() - redPoint.x),
                Math.min(Conf.scanHeight, image.getHeight() - redPoint.y)
        );
    }

    private Point getRedPoint() throws IOException {
        // find red point which in range, point are all red
        return rgbSet.stream()
                .filter(entry->isRed(entry.getKey()))
                .max((o1, o2) -> o1.getValue().d - o2.getValue().d)
                .get().getKey();
    }

    private boolean isRed(Point p){
        List<RGB> rgbs = new ArrayList<>();
        rgbs.add(rgbMap.get(p));
        rgbs.add(rgbMap.get(new Point(p.x - Conf.scanRange, p.y)));
        rgbs.add(rgbMap.get(new Point(p.x + Conf.scanRange, p.y)));
        rgbs.add(rgbMap.get(new Point(p.x, p.y- Conf.scanRange)));
        rgbs.add(rgbMap.get(new Point(p.x, p.y+ Conf.scanRange)));
        return rgbs.stream().filter(rgb -> rgb!= null && rgb.isRed()).count() == 5;
    }

    private static class RGB {
        int r, g, b, d;

        RGB(int[] rgba) {
            this.r = rgba[0];
            this.g = rgba[1];
            this.b = rgba[2];
            this.d = this.r - this.g - this.b;
        }

        boolean isRed() {
            return d > 0;
        }

        @Override
        public String toString() {
            return "" + r + "|" + g + "|" + b;
        }
    }

//    public static void main(String... argv) throws IOException {
//
//        PotHelper ph = new PotHelper(ImageIO.read(new File("/Users/yangbo/fisher/0103/00-12-00.png")));
//        System.out.println(ph.getPotRectangle());
//    }
}
