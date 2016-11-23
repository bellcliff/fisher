package org.free;


import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


public class MyAction {
    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static void rightClick(int x, int y) throws InterruptedException {
        robot.mouseMove(x, y);
        Thread.sleep(100);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        Thread.sleep(50);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(500);
        robot.mouseMove(x, 30);
    }

    public static void keyPress() {
        keyPress(KeyEvent.VK_E);
    }

    public static void keyPress(int key) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return;
        }
        robot.keyPress(key);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return;
        }
        robot.keyRelease(key);
    }
}
