package org.free.ui;

import org.free.buffer.Buffer;
import org.free.graph.GraphHelper;
import org.free.MyAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class Fisher extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    static Fisher instance;
    static boolean autoClose = false;
    static int autoCloseTime = 1000 * 60 * 6 * 25;

    public static void main(String[] args) throws InterruptedException, AWTException {
        new Fisher();
    }

    private GraphHelper graphHelper;
    public static boolean running = true;

    Fisher() throws AWTException {
        graphHelper = new GraphHelper();
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.init();
        this.pack();
        this.setVisible(true);
        instance = this;
    }

    private void init() {
        final Fisher f = this;

        final JPanel conPanel = new JPanel(new FlowLayout());
        f.getContentPane().add(conPanel);

        addControl(conPanel);
    }

    private void addControl(JPanel conPanel) {
        final Fisher f = this;
        final JButton startButton = new JButton("start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                running = true;
                startButton.setEnabled(false);
                graphHelper.start();

                new Thread() {
                    @Override
                    public void run() {
                        final Buffer.BufferManagement buffer = new Buffer.BufferManagement(Buffer.FishBuffer.Bait, Buffer.FishBuffer.Knife, Buffer.FishBuffer.Special);
                        while (true) {
                            buffer.check();
                            MyAction.keyPress();
                            try {
                                graphHelper.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            f.setTitle(graphHelper.all + " - " + graphHelper.succeed);
                            if (!running) {
                                break;
                            }
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
                f.setBounds(graphHelper.fishRectangle);
                f.setAlwaysOnTop(true);
                f.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        graphHelper.fishRectangle = new Rectangle(f.getBounds());
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
                graphHelper.stop();
            }
        });

        conPanel.add(startButton);
        conPanel.add(positionButton);
        conPanel.add(stopButton);
    }
}
