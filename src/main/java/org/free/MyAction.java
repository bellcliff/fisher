package org.free;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class MyAction {
	static Robot robot;
	static Toolkit toolkit;
	static {
		try {
			robot = new Robot();
			toolkit = Toolkit.getDefaultToolkit();
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
		Thread.sleep(2000);
		robot.mouseMove(300, 10);
	}

	public static void keyPress() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			return;
		}
		robot.keyPress(KeyEvent.VK_E);
		robot.keyRelease(KeyEvent.VK_E);
	}
	public static void keyPress1() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			return;
		}
		robot.keyPress(KeyEvent.VK_X);
		robot.keyRelease(KeyEvent.VK_X);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void closePC(){
		try {
			Runtime.getRuntime().exec("shutdown /i");
		} catch (IOException e) {
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		System.out.println(width + "-" + height);
	}
}
