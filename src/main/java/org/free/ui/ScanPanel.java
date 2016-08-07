package org.free.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by byang1 on 7/31/16.
 */
public class ScanPanel extends JPanel {

    private JLabel fishLabel;
    private JLabel lightLabel;
    private JLabel scanImgLabel;
    private JLabel potImgLabel;

    ScanPanel() {
        init();
    }

    private void init() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "扫描"));
        this.setLayout(new FlowLayout());
        fishLabel = new JLabel("0/0");
        lightLabel = new JLabel("0/0");
        scanImgLabel = new JLabel();
        potImgLabel = new JLabel();

        add(fishLabel);
        add(lightLabel);
        add(scanImgLabel);
        add(potImgLabel);
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
        lightLabel.setText(light+"/"+base);
        updateImage(pot, 1);
    }

    void updateScanPot(BufferedImage pot){
        updateImage(pot, 0);
    }

    public void updateFish(int all, int succ){
        fishLabel.setText(succ+"/"+all);
        SwingUtilities.invokeLater(()->{
            validate();
            repaint();
        });
    }
}
