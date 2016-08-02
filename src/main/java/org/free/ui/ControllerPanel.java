package org.free.ui;

import org.free.MyAction;
import org.free.buffer.Buffer;
import org.free.config.Conf;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class ControllerPanel extends JPanel {

    private final Fisher fisher;
    private boolean running = false;

    private final JButton startButton = new JButton("start");

    ControllerPanel(Fisher fisher) {
        this.fisher = fisher;
        init();
    }

    private void init() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "控制台"));
        setLayout(new GridLayout(2,2));

        add(getStartButton());
        add(getPositionButton());
        add(getStopButton());
        add(getLightText());
    }

    private JButton getStartButton() {
        startButton.addActionListener(e -> {
            running = true;
            startButton.setEnabled(false);
            fisher.graphHelper.start();

            new Thread() {
                @Override
                public void run() {
                    final Buffer.BufferManagement buffer = new Buffer.BufferManagement(Buffer.FishBuffer.Bait, Buffer.FishBuffer.Knife, Buffer.FishBuffer.Special);
                    while (true) {
                        buffer.check();
                        MyAction.keyPress();
                        try {
                            fisher.graphHelper.run();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (!running) {
                            break;
                        }
                    }
                }
            }.start();
        });
        return startButton;
    }

    private JButton getPositionButton() {

        final JButton positionButton = new JButton("position");
        positionButton.addActionListener(e -> {
            final JFrame f1 = new JFrame();
            f1.setBounds(fisher.graphHelper.fishRectangle);
            f1.setAlwaysOnTop(true);
            f1.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    fisher.graphHelper.updateFishRectangle(new Rectangle(f1.getBounds()));
                }
            });
            f1.setVisible(true);
        });
        return positionButton;
    }

    private JButton getStopButton() {
        final JButton stopButton = new JButton("stop");
        stopButton.addActionListener(e -> {
            running = false;
            startButton.setEnabled(true);
            fisher.graphHelper.stop();
        });
        return stopButton;
    }

    private JTextField getLightText() {
        final JTextField lightEdit = new JTextField(""+Conf.scanLight);
        lightEdit.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            void update() {
                if (lightEdit.getText().isEmpty()) {
                    return;
                }
                int light = Integer.parseInt(lightEdit.getText());
                Conf.EDITABLE_CONF.updateScanLight(light);
            }
        });
        return lightEdit;
    }
}
