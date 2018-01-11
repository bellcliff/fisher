package org.free.ui;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.IRow;
import net.java.dev.designgridlayout.IRowCreator;
import org.free.MyAction;
import org.free.buffer.Buffer;
import org.free.config.Conf;
import org.free.graph.GraphHelper;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class ControllerPanel extends JPanel {

    private boolean running = false;

    private final JButton startButton = new JButton("开始");
    private final JButton posButton = new JButton("位置");
    private final JButton stopButton = new JButton("停止");
    private final JTextField lightEdit = new JTextField(""+Conf.scanLight);
    private final DesignGridLayout layout;

    private int fail = 0;
    private GraphHelper graphHelper;

    ControllerPanel() {
        layout = new DesignGridLayout(this);
        graphHelper = new GraphHelper();
        init();
    }

    private void init() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "控制台"));
        initEdit();
        initAction();
        layout.row().grid().add(startButton).add(stopButton);
        layout.row().grid(new JLabel("亮度")).add(lightEdit).add(posButton);
    }

    private void initAction() {

        startButton.addActionListener(e -> {
            running = true;
            startButton.setEnabled(false);
            graphHelper.start();
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(3000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    final Buffer.BufferManagement buffer = new Buffer.BufferManagement(
                            Buffer.FishBuffer.Bait,
                            Buffer.FishBuffer.Knife,
                            Buffer.FishBuffer.Special
                    );
                    while (true) {
                        buffer.check();
                        MyAction.keyPress();
                        try {
                            graphHelper.scan();
                            fail = 0;
                        } catch (Exception e) {
                            e.printStackTrace();
                            fail++;
                        }
                        if (!running || fail > 20) {
                            break;
                        }
                    }
                }
            }.start();
        });

        final JFrame f1 = getJFrame();
        posButton.addActionListener(e -> {
            f1.setVisible(!f1.isVisible());
        });

        stopButton.addActionListener(e -> {
            running = false;
            startButton.setEnabled(true);
            graphHelper.stop();
        });
    }

    private JFrame getJFrame() {
        JFrame f1 = new JFrame();
        f1.setBounds(graphHelper.getFishRectangle());
        f1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f1.getRootPane().putClientProperty("Window.alpha", 0.05f);
        f1.setUndecorated(true);
        f1.setAlwaysOnTop(true);
        f1.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                graphHelper.updateFishRectangle(new Rectangle(f1.getBounds()));
            }
        });
        return f1;
    }


    private void initEdit() {
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
    }
}
