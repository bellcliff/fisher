package org.free.ui;

import org.free.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    @Autowired
    ScreenService service;

    public void init() {

        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(getFrameSize());
        setState(Frame.NORMAL);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1));

        mainPanel.add(new ControlPanel());
        mainPanel.add(new ConfigPanel());

        add(mainPanel, BorderLayout.PAGE_START);

        add(new StatPanel(), BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private Dimension getFrameSize(){
        Dimension screenSize = service.getScreenSize();
        return new Dimension(screenSize.width / 5, 200);
    }
}
