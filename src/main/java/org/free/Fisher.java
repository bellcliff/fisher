package org.free;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Fisher extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static Fisher instance;
    static boolean autoClose = false;
    static int autoCloseTime = 1000 * 60 * 6 * 25;

    public static void main(String[] args) throws InterruptedException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new Fisher();
        // f.startFish();
    }

    public static Grapher g;
    public static JButton stop, start, position;
    static boolean running = true;

    boolean isPositionFrameHide = true;

    public Fisher() {
        this.setAlwaysOnTop(true);
        this.setBounds(10, 10, 240, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2, 1));
        this.init();
        this.setVisible(true);
        g = new Grapher();
        instance = this;
    }

    private void init() {
        final Fisher f = this;

        final JPanel conPanel = new JPanel(new FlowLayout());
        f.getContentPane().add(conPanel);
        final JPanel confPanel = new JPanel(new FlowLayout());
        f.getContentPane().add(confPanel);

        addControl(conPanel);
        addConf(confPanel);
    }

    private void addControl(JPanel conPanel) {
        final Fisher f = this;
        final JButton startButton = new JButton("start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = true;
                startButton.setEnabled(false);
                final long _start = new Date().getTime();
                MyAction.keyPress1();
                new Thread() {
                    @Override
            public void run() {
                long start = new Date().getTime();

                // 自动关机指令
                if (autoClose && start - _start > autoCloseTime){
                    MyAction.closePC();
                }

                // 启动时上鱼饵
                // MyAction.keyPress1();

                while (true) {
                    long now = new Date().getTime();
                    if (Conf.enBait && (now - start > Conf.inBait * 60 * 1000)) {
                        start = now;
                        MyAction.keyPress1();
                    }
                    MyAction.keyPress();
                    try {
                        g.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // System.exit(0);
                    }
                    f.setTitle(Grapher.all + " - " + Grapher.succeed);
                    if (!running){
                        return;
                    }
                }
            }
            }.start();
            }
        });

        JButton positionButton = new JButton("pos");
        final JFrame positionFrame = initFrame();
        positionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Fisher.this.isPositionFrameHide = ! Fisher.this.isPositionFrameHide;
                positionFrame.setVisible(Fisher.this.isPositionFrameHide);
            }

        });

        JButton stopButton = new JButton("stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = false;
                startButton.setEnabled(true);
            }
        });

        conPanel.add(startButton);
        conPanel.add(positionButton);
        conPanel.add(stopButton);
    }

    private void addConf(JPanel confPanel) {
        final JCheckBox enBait = new JCheckBox("鱼饵");
        final JTextField inBait = new JTextField(Conf.inBait);
        enBait.setSelected(Conf.enBait);
        inBait.setPreferredSize(new Dimension(50, 20));
        if (Conf.enBait)
            inBait.setText("" + Conf.inBait);
        enBait.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Conf.enBait = enBait.isSelected();
                if (Conf.enBait)
            Conf.inBait = Integer.parseInt(inBait.getText().trim());
            }
        });

        confPanel.add(enBait);
        confPanel.add(inBait);
    }

    private JFrame initFrame(){
        final JFrame positionFrame = new JFrame();
        positionFrame.setUndecorated(true);
        positionFrame.getRootPane().putClientProperty("Window.alpha", new Float(0.8f));
        //com.sun.awt.AWTUtilities.setWindowOpacity(f, 0.5f);
        positionFrame.setBounds(Grapher.left, Grapher.top, Grapher.width,
                Grapher.height);
        positionFrame.setAlwaysOnTop(true);
        positionFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Grapher.left = positionFrame.getX();
                Grapher.top = positionFrame.getY();
                Grapher.width = positionFrame.getWidth();
                Grapher.height = positionFrame.getHeight();
            }
        });
        return positionFrame;
    }
}
