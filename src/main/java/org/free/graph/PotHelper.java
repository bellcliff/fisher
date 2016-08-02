package org.free.graph;

import org.free.config.Conf;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PotHelper {

    private Map<Point, Integer> getDiffMap(BufferedImage img) {
        Raster raster = img.getRaster();
        Map<Point, Integer> rgbMap = new HashMap<>();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int[] rgba = raster.getPixel(x, y, new int[4]);
                rgbMap.put(new Point(x, y), rgba[0] - rgba[1] - rgba[2]);
            }
        }
        return rgbMap;
    }

    /**
     * 根据当前diff寻找合适的点,
     * 如果没有满足条件的点, 尝试降低diff
     * 如果满足条件的点多余100, 尝试扩大匹配范围
     * 如果依然不满足则放弃本次查询
     * @param image the captured image
     * @return Point
     */
    Point getRedPoint(BufferedImage image) {
        Conf.EDITABLE_CONF.resetPotColorDiff();
        Conf.EDITABLE_CONF.resetPotColorSize();
        Map<Point, Integer> rgbMap = getDiffMap(image);
        List<Point> redPoints;
        int count = 0;
        while (true) {
            redPoints = getRedPoint(rgbMap, Conf.EDITABLE_CONF.getPotColorDiff());
            if (redPoints.size() > 0)
                break;
            Conf.EDITABLE_CONF.updatePotColorDiff(-10);
            count++;
            if (count > 20) return null;
        }
        if (redPoints.size() < 100)
            return redPoints.get(redPoints.size() - 1);
        Conf.EDITABLE_CONF.updatePotColorSize(1);
        while (true) {
            redPoints = getRedPoint(rgbMap, Conf.EDITABLE_CONF.getPotColorDiff());
            if (redPoints.size() > 0)
                break;
            Conf.EDITABLE_CONF.updatePotColorSize(1);
            count++;
            if (count > 20) return null;
        }
        if (redPoints.size() < 100)
            return redPoints.get(redPoints.size() - 1);
        return null;
    }

    /**
     * 尝试获取diff情况下满足条件的点的组合
     * @param rgbMap
     * @param diff
     * @return
     */
    private List<Point> getRedPoint(Map<Point, Integer> rgbMap, int diff) {
        List<Point> redPoints = rgbMap.entrySet().stream().filter(entry -> entry.getValue() > diff)
                .filter(entry -> checkRed(rgbMap, entry.getKey(), Conf.EDITABLE_CONF.getPotColorSize()))
                .map(Map.Entry::getKey).collect(Collectors.toList());

        System.out.println("size: " + redPoints.size());
        return redPoints;
    }

    /**
     * 判定所有当前点附近的点满足颜色判定
     * @param rgbMap
     * @param p
     * @param length
     * @return
     */
    private boolean checkRed(Map<Point, Integer> rgbMap, Point p, int length) {
        if (!rgbMap.containsKey(p)) return false;
        boolean isRed = rgbMap.get(p) > Conf.EDITABLE_CONF.getPotColorDiff();
        return !isRed || length == 0 || (checkRed(rgbMap, new Point(p.x - 1, p.y), length - 1)
                && checkRed(rgbMap, new Point(p.x + 1, p.y), length - 1)
                && checkRed(rgbMap, new Point(p.x, p.y - 1), length - 1)
                && checkRed(rgbMap, new Point(p.x, p.y + 1), length - 1));
    }

    public static void main(String... args) throws IOException {
        BufferedImage image = ImageIO.read(new File("b.png"));
        PotHelper ph = new PotHelper();
        long start = System.currentTimeMillis();

        System.out.println(ph.getRedPoint(image));
        System.out.println(Conf.EDITABLE_CONF.getPotColorDiff());
        System.out.println("Time used: "+(System.currentTimeMillis() - start));
    }
}
