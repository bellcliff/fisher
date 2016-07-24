package org.free.ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by byang1 on 7/17/16.
 */
class ControlPanel extends JPanel {

    ControlPanel(){

        setBorder(new CompoundBorder(new EmptyBorder(0, 10, 10, 10), new TitledBorder("控制")));
        setLayout(new GridLayout(1, 3));

        final JButton btnStart = new JButton("Start");
        add(btnStart);

        final JButton showPosition = new JButton("Position");
        add(showPosition);

        final JButton btnPause = new JButton("Pause");
        add(btnPause);

    }
}
