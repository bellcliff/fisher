package org.free.ui;

import org.free.buffer.Buffer;
import org.free.config.Conf;
import org.free.graph.GraphHelper;
import org.free.MyAction;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Fisher extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static Fisher fisher;

    public static void main(String[] args) throws InterruptedException, AWTException {
        fisher = new Fisher();
    }

    private GraphHelper graphHelper;
    private boolean running = true;
    private final JPanel dispPanel = new JPanel();
    private final JPanel potPanel = new JPanel();
    private final JLabel scanImgLabel = new JLabel();
    private final JLabel potImgLabel = new JLabel();

    private Fisher() throws AWTException {
        graphHelper = new GraphHelper();
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.init();
        this.pack();
        this.setVisible(true);
    }
    private void init() {
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        final JPanel conPanel = new JPanel();
        getContentPane().add(conPanel, c);
        addControl(conPanel);

        c.gridx = 0;
        c.gridy = 1;
        getContentPane().add(dispPanel, c);
        dispPanel.add(scanImgLabel);

        c.gridx = 0;
        c.gridy = 2;
        getContentPane().add(potPanel, c);
        potPanel.add(potImgLabel);

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
        int height = bufferedImage.getHeight() * this.getWidth() / bufferedImage.getWidth();
        scanImgLabel.setIcon(new ImageIcon(bufferedImage.getScaledInstance(this.getWidth(), height, Image.SCALE_SMOOTH)));
        scanImgLabel.repaint();
        dispPanel.repaint();
        this.revalidate();
        this.pack();
    }

    public void updatePot(BufferedImage pot){
        potImgLabel.setIcon(new ImageIcon(pot));
        potImgLabel.repaint();
        this.revalidate();
        this.pack();

    }
}
