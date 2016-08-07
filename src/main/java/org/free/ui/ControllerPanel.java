package org.free.ui;

import org.free.MyAction;
import org.free.buffer.Buffer;
import org.free.config.Conf;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
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
                    try {
                        sleep(5000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
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
        final JFrame f1 = getJFrame();
        positionButton.addActionListener(e -> {
            f1.setVisible(!f1.isVisible());
        });
        return positionButton;
    }

    private JFrame getJFrame(){
        JFrame f1 = new JFrame();
        f1.setBounds(fisher.graphHelper.fishRectangle);
        f1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f1.getRootPane().putClientProperty("Window.alpha", 0.2f);
        f1.setUndecorated(true);
        f1.setAlwaysOnTop(true);
        f1.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fisher.graphHelper.updateFishRectangle(new Rectangle(f1.getBounds()));
            }
        });
        return f1;
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
