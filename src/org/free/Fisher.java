package org.free;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Fisher extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    static Fisher instance;
    static boolean autoClose = false;
    static int autoCloseTime = 1000 * 60 * 6 * 25;

    public static void main(String[] args) throws InterruptedException {
//		JFrame.setDefaultLookAndFeelDecorated(true);
        new Fisher();
        // f.startFish();
    }

    public static Grapher g;
    public static JButton stop, start, position;
    static boolean running = true;

    public Fisher() {
        this.setAlwaysOnTop(true);
        this.setBounds(10, 10, 240, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2, 1));
        this.init();
        this.setVisible(true);
        g = new Grapher();
        instance = this;
    }

    private void init() {
        final Fisher f = this;

        final JPanel conPanel = new JPanel(new FlowLayout());
        f.getContentPane().add(conPanel);
        final JPanel confPanel = new JPanel(new FlowLayout());
        f.getContentPane().add(confPanel);

        addControl(conPanel);
        addConf(confPanel);
    }

    private void addControl(JPanel conPanel) {
        final Fisher f = this;
        final JButton startButton = new JButton("start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (new File("img").exists())
                        Grapher.deleteFileOrFolder(new File("img").toPath());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                running = true;
                startButton.setEnabled(false);

                new Thread() {
                    @Override
                    public void run() {
                        final Buffer.BufferManagement buffer = new Buffer.BufferManagement(Buffer.FishBuffer.Bait, Buffer.FishBuffer.Knife);
                        while (true) {
                            buffer.check();
                            MyAction.keyPress();
                            try {
                                g.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            f.setTitle(Grapher.all + " - " + Grapher.succeed);
                            if (!running)
                                break;
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
//				com.sun.awt.AWTUtilities.setWindowOpacity(f, 0.5f);
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

        conPanel.add(startButton);
        conPanel.add(positionButton);
        conPanel.add(stopButton);
    }

    private void addConf(JPanel confPanel) {
        final JCheckBox enBait = new JCheckBox("鱼饵");
        final JTextField inBait = new JTextField(Conf.inBait);
        enBait.setSelected(Conf.enBait);
        inBait.setPreferredSize(new Dimension(50, 20));
        if (Conf.enBait)
            inBait.setText("" + Conf.inBait);
        enBait.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Conf.enBait = enBait.isSelected();
                if (Conf.enBait)
                    Conf.inBait = Integer.parseInt(inBait.getText().trim());
            }
        });

        confPanel.add(enBait);
        confPanel.add(inBait);
    }
}
