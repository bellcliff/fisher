package org.free.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by byang1 on 7/31/16.
 */
public class ScanPanel extends JPanel {

    private JLabel fishAllLabel;
    private JLabel fishSuccLabel;

    private JLabel lightHighLabel;
    private JLabel lightBaseLabel;

    private JLabel redLabel;
    private JLabel greenLabel;
    private JLabel blueLabel;

    private JLabel scanImgLabel;
    private JLabel potImgLabel;

    ScanPanel() {
        init();
    }

    private void init() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "扫描"));
        setLayout(new GridLayout(3, 1));
        setPreferredSize(new Dimension(240, 120));

        fishAllLabel = new JLabel();
        fishSuccLabel = new JLabel();
        lightBaseLabel = new JLabel();
        lightHighLabel = new JLabel();

        redLabel = new JLabel();
        greenLabel = new JLabel();
        blueLabel = new JLabel();

        scanImgLabel = new JLabel();
        potImgLabel = new JLabel();

        JPanel fishPanel = new JPanel(new GridLayout(1, 4));
        fishPanel.add(fishAllLabel);
        fishPanel.add(new JSeparator(SwingConstants.VERTICAL));
        fishPanel.add(fishSuccLabel);
        fishPanel.add(new JSeparator(SwingConstants.VERTICAL));
        fishPanel.add(lightHighLabel);
        fishPanel.add(new JSeparator(SwingConstants.VERTICAL));
        fishPanel.add(lightBaseLabel);
        add(fishPanel);

        JPanel rgbPanel = new JPanel(new GridLayout(1, 3));
        rgbPanel.add(redLabel);
        rgbPanel.add(new JSeparator(SwingConstants.VERTICAL));
        rgbPanel.add(greenLabel);
        rgbPanel.add(new JSeparator(SwingConstants.VERTICAL));
        rgbPanel.add(blueLabel);
        add(rgbPanel);

        JPanel imgPanel = new JPanel(new GridLayout(1, 2));
        imgPanel.add(scanImgLabel);
        rgbPanel.add(new JSeparator(SwingConstants.VERTICAL));
        imgPanel.add(potImgLabel);
        add(imgPanel);
    }

    private void updateImage(BufferedImage bufferedImage, int type) {
        JLabel label = type == 0 ? scanImgLabel : potImgLabel;
        int height = 30;
        int width = bufferedImage.getWidth() * height / bufferedImage.getHeight();
        label.setIcon(new ImageIcon(bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));

        SwingUtilities.invokeLater(() -> {
            validate();
            repaint();
        });
    }

    public void updatePot(int light, int base, BufferedImage pot) {
        lightHighLabel.setText("" + light);
        lightBaseLabel.setText("" + base);
        updateImage(pot, 1);
    }

    public void updateRGB(int r, int g, int b) {
        redLabel.setText("R:" + r);
        greenLabel.setText("G:" + g);
        blueLabel.setText("B:" + b);
        SwingUtilities.invokeLater(() -> {
            validate();
            repaint();
        });
    }

    void updateScanPot(BufferedImage pot) {
        updateImage(pot, 0);
    }

    public void updateFish(int all, int succ) {
        fishSuccLabel.setText("" + succ);
        fishAllLabel.setText("" + all);
        SwingUtilities.invokeLater(() -> {
            validate();
            repaint();
        });
    }
}
