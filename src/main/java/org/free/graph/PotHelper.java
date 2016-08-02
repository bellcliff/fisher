package org.free.graph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PotHelper {

    private final Map<Point, Integer> rgbMap;
    private final BufferedImage image;
    private final Set<Map.Entry<Point, Integer>> rgbSet;
    private final int maxDiff;

    PotHelper(BufferedImage image) {
        this.image = image;
        this.rgbMap = getDiffMap();
        this.rgbSet = rgbMap.entrySet();
        this.maxDiff = rgbSet.stream().max((o1, o2) -> o1.getValue() - o2.getValue()).get().getValue();
    }

    private Map<Point, Integer> getDiffMap() {
        Raster raster = image.getRaster();
        Map<Point, Integer> rgbMap = new HashMap<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int[] rgba = raster.getPixel(x, y, new int[4]);
                rgbMap.put(new Point(x, y), rgba[0] - rgba[1] - rgba[2]);
            }
        }
        return rgbMap;
    }

    Point getRedPoint() throws IOException {
        int diff = maxDiff, size = 4;
        List<Point> redPoints;
        while (true) {
            redPoints = getRedPoints(diff, size);
            if (redPoints.size() > 200) {
                break;
            }
            diff -= 10;
        }
        while (true) {
            if (redPoints.size() < 100) break;
            size += 1;
            redPoints = getRedPoints(diff, size);

        }
        if (redPoints.size() == 0) throw new IOException();
        redPoints.sort((o1, o2) -> o1.x + o1.y - o2.x - o2.y);
        return redPoints.get(0);
    }

    /**
     * 获取满足条件
     *
     * @param diff
     * @param size
     * @return
     */
    List<Point> getRedPoints(int diff, int size) throws IOException {
        System.out.println("色差: " + diff + ", " + size);
        return rgbSet.stream().filter(entry -> entry.getValue() > diff)
                .filter(entry -> checkRed(entry.getKey(), diff, size))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * 判定当前点和周围点
     *
     * @param p        检测点
     * @param distance 检测距离
     * @return
     */
    private boolean checkRed(Point p, int diff, int distance) {
        Point pLeft = new Point(p.x - distance, p.y);
        Point pRight = new Point(p.x + distance, p.y);
        Point pUp = new Point(p.x, p.y - distance);
        Point pDown = new Point(p.x, p.y + distance);
        return checkRed(pLeft, diff) && checkRed(pRight, diff) && checkRed(pUp, diff) && checkRed(pDown, diff);
    }

    private boolean checkRed(Point p, int diff) {
        return rgbMap.containsKey(p) && rgbMap.get(p) > diff;
    }

    private void drawSquare(File file, Point redPoint) throws IOException {
        int diff = rgbMap.get(redPoint);
        for (Point p : rgbSet.stream().filter(pointIntegerEntry -> pointIntegerEntry.getValue() >= diff).map(Map.Entry::getKey).collect(Collectors.toList()))
            image.setRGB(p.x, p.y, 0);
        ImageIO.write(image, "PNG", file);
    }
}
