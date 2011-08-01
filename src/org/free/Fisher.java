package org.free;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Fisher extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Fisher instance;
	static boolean autoClose = false;
	static int autoCloseTime = 1000 * 60 * 6 * 25;

	public static void main(String[] args) throws InterruptedException {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new Fisher();
		// f.startFish();
	}

	public static Grapher g;
	public static JButton stop, start, position;
	static boolean running = true;

	public Fisher() {
		this.setAlwaysOnTop(true);
		this.setBounds(10, 10, 240, 80);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1, 2));
		this.init();
		this.setVisible(true);
		g = new Grapher();
		instance = this;
	}

	private void init() {
		final Fisher f = this;
		final JButton startButton = new JButton("start");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				running = true;
				startButton.setEnabled(false);
				final long _start = new Date().getTime();
				new Thread() {
					@Override
					public void run() {						
						long start = new Date().getTime();

						//自动关机指令
						if(autoClose && start - _start > autoCloseTime)
							MyAction.closePC();
						
						//启动时上鱼饵
						MyAction.keyPress1();
						
						while (true) {
							long now = new Date().getTime();
							if (now - start > 10 * 60 * 1000) {
								//
								start = now;
								MyAction.keyPress1();
							}
							MyAction.keyPress();
							try {
								g.run();
							} catch (Exception e) {
								e.printStackTrace();
								// System.exit(0);
							}
							f.setTitle(Grapher.all + " - " + Grapher.succeed);
							if (!running)
								return;
						}
					}
				}.start();
			}
		});

		JButton positionButton = new JButton("pos");
		positionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame f = new JFrame();
				com.sun.awt.AWTUtilities.setWindowOpacity(f, 0.5f);
				f.setBounds(Grapher.left, Grapher.top, Grapher.width,
						Grapher.height);
				f.setAlwaysOnTop(true);
				f.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						Grapher.left = f.getX();
						Grapher.top = f.getY();
						Grapher.width = f.getWidth();
						Grapher.height = f.getHeight();
					}
				});
				f.setVisible(true);
			}

		});

		JButton stopButton = new JButton("stop");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				running = false;
				startButton.setEnabled(true);
			}
		});

		this.getContentPane().add(startButton);
		this.getContentPane().add(positionButton);
		this.getContentPane().add(stopButton);
	}

}
