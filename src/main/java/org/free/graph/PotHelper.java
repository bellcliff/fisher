package org.free.graph;

import org.free.config.Conf;
import org.free.ui.Fisher;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PotHelper {

    private final BufferedImage image;
    private final Map<Point, Integer[]> rgbMap;
    private final Set<Map.Entry<Point, Integer[]>> rgbSet;

    enum PotType {
        RED, BLUE
    }

    PotHelper(BufferedImage image) {
        this.image = image;
        Raster raster = image.getRaster();
        rgbMap = new HashMap<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int[] rgba = raster.getPixel(x, y, new int[4]);
                if (checkRed(rgba[0], rgba[1], rgba[2]))
                rgbMap.put(new Point(x, y), new Integer[]{rgba[0], rgba[1], rgba[2]});
            }
        }
        rgbSet = rgbMap.entrySet();
    }

    private Point getRedPoint() throws IOException {
        Optional<Map.Entry<Point, Integer[]>> option = rgbSet.stream()
                .filter(pointEntry -> checkRed(pointEntry.getKey()))
                .max((o1, o2) -> o1.getKey().x + o1.getKey().y - o2.getKey().x - o2.getKey().y);

        if (option.isPresent()) {
            Integer[] rgb = option.get().getValue();
            Fisher.fisher.scanPanel.updateRGB(rgb[0], rgb[1], rgb[2]);
            return option.get().getKey();
        }
        throw new IOException();
    }

    private boolean checkRed(int r, int g, int b) {
        return r > Conf.MIN_RED && r > g + b;
    }

    private boolean checkRed(int x, int y) {
        return x >= 0 && y >= 0 && x < image.getWidth() && y < image.getHeight() && rgbMap.containsKey(new Point(x, y));
    }

    private boolean checkRed(Point point) {
        int x = point.x, y = point.y;

        if (!checkRed(x, y)) return false;
        for (int dep = 1; dep < Conf.scanRange; dep++){
            if (!checkRed(x - dep , y) || ! checkRed(x+dep, y) || !checkRed(x, y-dep)||!checkRed(x, y+dep))
                return false;
        }
        return true;
    }

    Rectangle getPotRectangle() throws IOException {
        Point redPoint = getRedPoint();
        if (redPoint == null) throw new IOException("red point not found!");
        return new Rectangle(
                Math.max(redPoint.x + Conf.scanLeft, 0),
                Math.max(redPoint.y + Conf.scanTop, 0),
                Math.min(Conf.scanWidth, image.getWidth() - redPoint.x),
                Math.min(Conf.scanHeight, image.getHeight() - redPoint.y)
        );
    }
//
//    private Rectangle getPotRectangle(PotType potType) throws IOException {
//        Point point;
//        int offsetLeft, offsetTop;
//        switch (potType) {
//            case RED:
//                point = getRedPoint();
//                offsetLeft = Conf.scanLeft;
//                offsetTop = Conf.scanTop;
//                break;
//            default:
//                throw new IOException("unsupported pot type");
//        }
//
//        return getPotRec(point, offsetLeft, offsetTop);
//    }
//
//    BufferedImage getPotImage(PotType potType) throws IOException {
//        Rectangle redRectangle = getPotRectangle(potType);
//        return image.getSubimage(redRectangle.x, redRectangle.y, redRectangle.width, redRectangle.height);
//    }
}
