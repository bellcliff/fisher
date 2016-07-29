package org.free;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

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
        robot.mousePress(InputEvent.BUTTON3_MASK);
        Thread.sleep(50);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
        Thread.sleep(2000);
        robot.mouseMove(300, 10);
    }

    public static void keyPress() {
        keyPress(KeyEvent.VK_E);
    }

    public static void keyPress(int key) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return;
        }
        robot.keyPress(key);
        robot.keyRelease(key);
    }
}
