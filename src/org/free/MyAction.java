package org.free;

import java.awt.AWTException;
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
		robot.mousePress(InputEvent.BUTTON3_MASK);
		Thread.sleep(50);
		robot.mouseRelease(InputEvent.BUTTON3_MASK);
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
		robot.keyPress(KeyEvent.VK_Z);
		robot.keyRelease(KeyEvent.VK_Z);
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
		closePC();
	}
}
