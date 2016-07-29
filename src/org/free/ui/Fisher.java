package org.free.ui;

import org.free.buffer.Buffer;
import org.free.graph.GraphHelper;
import org.free.MyAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import javax.swing.*;

public class Fisher extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) throws InterruptedException, AWTException {
        new Fisher();
    }

    private GraphHelper graphHelper;
    public static boolean running = true;

    private Fisher() throws AWTException {
        graphHelper = new GraphHelper();
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setBounds(0, 0, 200, 600);
        this.init();
        this.setVisible(true);
    }
    private final JPanel dispPanel = new JPanel();
    private void init() {
        final JPanel conPanel = new JPanel(new FlowLayout());

        getContentPane().add(conPanel);
        getContentPane().add(dispPanel);
        addControl(conPanel);
    }

    private void addControl(JPanel conPanel) {
        final Fisher f = this;
        final JButton startButton = new JButton("start");
        startButton.addActionListener(e -> {
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
        });

        JButton positionButton = new JButton("pos");
        positionButton.addActionListener(e -> {
            final JFrame f1 = new JFrame();
            f1.setBounds(graphHelper.fishRectangle);
            f1.setAlwaysOnTop(true);
            f1.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    graphHelper.fishRectangle = new Rectangle(f1.getBounds());
                }
            });
            f1.setVisible(true);
        });

        JButton stopButton = new JButton("stop");
        stopButton.addActionListener(e -> {
            running = false;
            startButton.setEnabled(true);
            graphHelper.stop();
        });

        conPanel.add(startButton);
        conPanel.add(positionButton);
        conPanel.add(stopButton);
    }

    public void updateImage(BufferedImage bufferedImage) {
        JLabel wIcon = new JLabel(new ImageIcon(bufferedImage));
        int h = bufferedImage.getHeight() * this.getWidth() / bufferedImage.getWidth();
        wIcon.setBounds(new Rectangle(0, 0, this.getWidth(), h));
        dispPanel.removeAll();
        dispPanel.add(wIcon);
    }
}
