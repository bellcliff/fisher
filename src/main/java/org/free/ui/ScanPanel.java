package org.free.ui;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.IRowCreator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by byang1 on 7/31/16.
 */
public class ScanPanel extends JPanel {

    private JLabel fishAllLabel;
    private JLabel lightHighLabel;
    private JLabel redLabel;
    private JLabel scanImgLabel;
    private JLabel potImgLabel;

    ScanPanel() {
        init();
    }

    private void init() {
        DesignGridLayout layout = new DesignGridLayout(this);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "扫描"));
        setLayout(new GridLayout(3, 1));
        setPreferredSize(new Dimension(220, 120));

        fishAllLabel = new JLabel("A|S");
        redLabel = new JLabel("R|G|B");
        lightHighLabel = new JLabel("H|D");

        scanImgLabel = new JLabel();
        potImgLabel = new JLabel();


        IRowCreator row = layout.row();
        row.grid().add(fishAllLabel);
        row.grid().add(lightHighLabel);
        row = layout.row();
        row.grid().add(scanImgLabel);
        row.grid().add(potImgLabel);
        row = layout.row();
        row.grid().add(redLabel);
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
        lightHighLabel.setText(light + "|" + (light - base));
        updateImage(pot, 1);
    }

    public void updateRGB(int r, int g, int b) {
        redLabel.setText(r + "|" + g + "|" + b);
        SwingUtilities.invokeLater(() -> {
            validate();
            repaint();
        });
    }

    void updateScanPot(BufferedImage pot) {
        updateImage(pot, 0);
    }

    public void updateFish(int all, int success) {
        fishAllLabel.setText(all + "|" + success);
        SwingUtilities.invokeLater(() -> {
            validate();
            repaint();
        });
    }
}
