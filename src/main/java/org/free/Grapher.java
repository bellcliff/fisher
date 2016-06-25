package org.free;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Grapher {
//	static boolean debug = true;
	static int left, top, width, height, base, size, currX, currY;
	private static int offset_left = -35, offset_top = -10, offset_right = 0,
			offset_bottom = 20, lightThreshold = 10, _lightThreshold = 10;
	static int file_index = 0, dir_index = 0;
	static int fail_count = 0;
	static int all, succeed;
	static double per;
	static ArrayList<BufferedImage> img_list = new ArrayList<BufferedImage>();
	static {
		Dimension dim = MyAction.toolkit.getScreenSize();
		width = dim.width - 800;
		height = dim.height / 2 - 300;
		top = 50;
		left = (dim.width - width) / 2;

		size = 4;
		System.out.println(left + " - " + top + ", " + width + " - " + height);
	}

	public void run() throws Exception {
		currX = 0;
		base = 0;
		lightThreshold = _lightThreshold;
		long start = new Date().getTime();
		Thread.sleep(4000);
		System.out.println("=====");
		boolean succ = false;
		try {
			Rectangle fisherPotRectangle = getFisherPot(MyAction.robot
					.createScreenCapture(new Rectangle(left, top, width, height)));
			if (fisherPotRectangle == null)
				return;
			while (new Date().getTime() - start < Conf.interval * 1000) {
				Thread.sleep(200);
				if (!Fisher.running)
					break;
				if (analysis1(
						MyAction.robot.createScreenCapture(fisherPotRectangle),
						fisherPotRectangle)) {
					MyAction.rightClick(currX, currY);
					succeed++;
					succ = true;
					break;
				}
			}
		} finally {
			if (!succ)
				saveFile();
			if (img_list.size() > 10)
				img_list.clear();
			all++;
		}
	}

	private static Rectangle getFisherPot(BufferedImage img) throws IOException {
		img_list.add(img);
		// long start = new Date().getTime();
		ArrayList<Point> list = new ArrayList<Point>();
		for (int x = 0; x < img.getWidth() - size; x++)
			for (int y = 0; y < img.getHeight() - size; y++) {
				if (checkRed(img, x, y)) {
					list.add(new Point(x, y));
				}
			}
		if (list.size() == 0) {
			// saveFile(img);
			if (fail_count++ > 100)
				System.exit(0);
			return null;
		}
		int minx = 1440, maxx = 0, miny = 900, maxy = 0;
		for (Point p : list) {
			if (minx > p.x)
				minx = p.x;
			if (maxx < p.x)
				maxx = p.x;
			if (miny > p.y)
				miny = p.y;
			if (maxy < p.y)
				maxy = p.y;
		}
		int _left = minx + offset_left;
		if (_left < 0)
			_left = 0;
		int _top = miny + offset_top;
		if (_top < 0)
			_top = 0;
		int _width = maxx - minx - offset_left + offset_right;
		if (_width + _left > img.getWidth())
			_width = img.getWidth() - _left;
		int _height = maxy - miny - offset_top + offset_bottom;
		if (_height + _top > img.getHeight())
			_height = img.getHeight() - _top;
		Rectangle rec = new Rectangle(_left, _top, _width, _height);
		base = getRectangleLight(img, rec);

		rec.x += left;
		rec.y += top;
		// System.out.println((new Date().getTime() - start) + " - " + base);

		if (Conf.isDouble && _top > height / 2)
			lightThreshold = _lightThreshold * 2;
		return rec;
	}

	private static int getRectangleLight(BufferedImage img) throws IOException {
		return getRectangleLight(img,
				new Rectangle(img.getWidth(), img.getHeight()));
	}

	private static int getRectangleLight(BufferedImage img, Rectangle rec)
			throws IOException {
		int light = 0;
		for (int x = rec.x; x < rec.x + rec.width; x++)
			for (int y = rec.y; y < rec.y + rec.height; y++)
				light += getRGB(img, x, y);

		// if (rec.x == 0)
		// saveFile(img);
		// else
		// saveFile(img, rec);
		return light / (rec.width * rec.height);
	}

	static boolean analysis1(BufferedImage img, Rectangle rec)
			throws IOException {
		img_list.add(img);
		int light = getRectangleLight(img);
		if (light - base > 10)
			System.out.println(light + "-" + base + "-" + (light - base));
		if (light > base + lightThreshold) {
			currX = rec.x + rec.width / 2;
			currY = rec.y + rec.height / 2;
			return true;
		}
		return false;
	}

	private static boolean checkRed(BufferedImage img, int x, int y) {
		for (int x0 = x; x0 < x + size; x0++)
			for (int y0 = y; y0 < y + size; y0++) {
				Color c = new Color(img.getRGB(x0, y0));
				if (c.getRed() < c.getGreen() + c.getBlue()) {
					return false;
				}
			}
		return true;
	}

	private static int getRGB(BufferedImage img, int x, int y) {
		Color c = new Color(img.getRGB(x, y));
		return c.getRed() + c.getBlue() + c.getGreen();
	}

	static void saveFile(BufferedImage img) throws IOException {

		// fail_count++;
		// // if(true)
		// // return;
		// // file_index=0;
		// if (!new File("img").exists())
		// new File("img").mkdir();
		// if (!new File("img/" + dir_index).exists())
		// new File("img/" + dir_index).mkdir();
		// while (true) {
		// File f = new File("img/" + dir_index + "/" + file_index++ + ".jpg");
		// if (!f.exists()) {
		// ImageIO.write(img, "jpg", f);
		// break;
		// }
		// }
	}

	static void saveFile(BufferedImage img, Rectangle rec) throws IOException {
		for (int i = rec.x; i < rec.x + rec.width; i++)
			for (int j = rec.y; j < rec.y + rec.height; j++)
				img.setRGB(i, j, 0);
		saveFile(img);
	}

	static void saveFile() throws IOException {
		if (!Conf.DEBUG)
			return;
		dir_index++;
		for (BufferedImage bi : img_list) {
			saveFile(bi);
		}
		img_list.clear();
	}
}
