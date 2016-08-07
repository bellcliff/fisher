package org.free.graph;

import org.free.config.Conf;

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

    private final Map<Point, Integer[]> rgbMap;
    private final BufferedImage image;
    private final Set<Map.Entry<Point, Integer[]>> rgbSet;
    private final int maxDiff;

    enum PotType {
        RED, BLUE
    }

    PotHelper(BufferedImage image) {
        this.image = image;
        this.rgbMap = getDiffMap();
        this.rgbSet = rgbMap.entrySet();
        this.maxDiff = rgbSet.stream().max((o1, o2) -> o1.getValue()[0] - o2.getValue()[0]).get().getValue()[0];
    }

    private Map<Point, Integer[]> getDiffMap() {
        Raster raster = image.getRaster();
        Map<Point, Integer[]> rgbMap = new HashMap<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int[] rgba = raster.getPixel(x, y, new int[4]);
                rgbMap.put(new Point(x, y), new Integer[]{rgba[0] - rgba[1] - rgba[2], rgba[0], rgba[1], rgba[2]});
            }
        }
        return rgbMap;
    }

    Point getBluePoint() throws IOException {
        return rgbSet.stream().filter(pointEntry -> checkBlue(pointEntry.getValue()))
                .max((o1, o2) -> o1.getKey().x + o1.getKey().y - o2.getKey().x - o2.getKey().y).get().getKey();
    }

    private boolean checkBlue(Integer[] rgb) {
        return rgb[3] > 80 && rgb[3] > rgb[1] + rgb[2] + 20;
    }

    Point getRedPoint() throws IOException {
        Optional<Map.Entry<Point, Integer[]>> option = rgbSet.stream().filter(pointEntry -> checkRed(pointEntry.getValue()))
                .max((o1, o2) -> o1.getKey().x + o1.getKey().y - o2.getKey().x - o2.getKey().y);
        if (option.isPresent())
            return option.get().getKey();
        else return null;
    }

    private boolean checkRed(Integer[] rgb) {
        return rgb[1] > Conf.MIN_RED && rgb[1] > rgb[2] + rgb[3];
    }
//
//    Point getRedPoint() throws IOException {
//        int diff = maxDiff, size = 5;
//        List<Point> redPoints;
//        while (true) {
//            redPoints = getRedPoints(diff, size);
//            if (redPoints.size() > 500) {
//                break;
//            }
//            diff -= 10;
//        }
//        while (true) {
//            if (redPoints.size() < 200) break;
//            size += 1;
//            redPoints = getRedPoints(diff, size);
//
//        }
//        if (redPoints.size() == 0) throw new IOException();
//        redPoints.sort((o1, o2) -> o1.x + o1.y - o2.x - o2.y);
//        return redPoints.get(0);
//    }
//
//    /**
//     * 获取满足条件
//     *
//     * @param diff
//     * @param size
//     * @return
//     */
//    List<Point> getRedPoints(int diff, int size) throws IOException {
////        System.out.println("色差: " + diff + ", " + size);
//        List<Point> points = rgbSet.stream().filter(entry -> entry.getValue()[0] > diff)
//                .filter(entry -> checkRed(entry.getKey(), diff, size))
//                .map(Map.Entry::getKey).collect(Collectors.toList());
//        System.out.println("色差: " + diff + ", " + size + ", " + points.size());
//        return points;
//    }
//
//    /**
//     * 判定当前点和周围点
//     *
//     * @param p        检测点
//     * @param distance 检测距离
//     * @return
//     */
//    private boolean checkRed(Point p, int diff, int distance) {
//        Point pLeft = new Point(p.x - distance, p.y);
//        Point pRight = new Point(p.x + distance, p.y);
//        Point pUp = new Point(p.x, p.y - distance);
//        Point pDown = new Point(p.x, p.y + distance);
//        return checkRed(pLeft, diff) && checkRed(pRight, diff) && checkRed(pUp, diff) && checkRed(pDown, diff);
//    }
//
//    private boolean checkRed(Point p, int diff) {
//        return rgbMap.containsKey(p) && rgbMap.get(p)[0] > diff;
//    }

    private Rectangle getPotRec(Point point, int offsetLeft, int offsetTop) throws IOException {
        return new Rectangle(point.x + offsetLeft, point.y + offsetTop, Conf.scanWidth, Conf.scanHeight);
    }

    public Rectangle getPotRectangle(PotType potType) throws IOException {
        Point point;
        int offsetLeft, offsetTop;
        switch(potType){
            case RED:
                point = getRedPoint();
                offsetLeft = Conf.scanLeft;
                offsetTop = Conf.scanTop;
                break;
            case BLUE:
                point = getBluePoint();
                offsetLeft = Conf.scanLeftBlue;
                offsetTop = Conf.scanTopBlue;
                break;
            default:
                throw new IOException("unsupported pot type");
        }

        return getPotRec(point, offsetLeft, offsetTop);
    }

    BufferedImage getPotImage(PotType potType) throws IOException {
        Rectangle redRectangle = getPotRectangle(potType);
        return image.getSubimage(redRectangle.x, redRectangle.y, redRectangle.width, redRectangle.height);
    }

    BufferedImage markRed(){
        BufferedImage copyImage = deepCopy(image);
        rgbSet.stream().filter(o->checkRed(rgbMap.get(o.getKey())))
                .forEach(o-> copyImage.setRGB(o.getKey().x, o.getKey().y, 0));
        return copyImage;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
