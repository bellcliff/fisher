package org.free.ui;

import org.free.graph.GraphHelper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Fisher extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static Fisher fisher;

    public static void main(String[] args) throws InterruptedException, AWTException {
        fisher = new Fisher();
    }

    GraphHelper graphHelper;
    public ScanPanel scanPanel = new ScanPanel();

    private JLabel fishLabel = new JLabel();
    private Fisher() throws AWTException {
        graphHelper = new GraphHelper();
        init();
    }

    private void init() {
        this.setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");
        setPreferredSize(new Dimension(240, 260));
        setLayout(new GridBagLayout());

        initPanel();

        Fisher.this.pack();
        Fisher.this.setVisible(true);
    }

    private void initPanel() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(new ControllerPanel(this), c);

        c.gridy = 1;
        c.weightx = 1.0;
        add(scanPanel, c);

        c.gridy = 2;
        JPanel fishPanel = new JPanel();
        fishPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()));
        fishPanel.setPreferredSize(new Dimension(200, 60));
        fishPanel.add(fishLabel);
        add(fishPanel, c);
    }

    public void updateFishPanel(BufferedImage img){
        int w = 200;
        int h = img.getHeight() * w / img.getWidth();
        fishLabel.setIcon(new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_SMOOTH)));
        getParent().setPreferredSize(new Dimension(w+10, h+10));
        validate();
        pack();
        repaint();
    }
}
