package org.free.ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by byang1 on 7/17/16.
 */
class ConfigPanel extends JPanel {
    ConfigPanel(){
        setBorder(new CompoundBorder(new EmptyBorder(0, 10, 10, 10), new TitledBorder("配置")));
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        final JLabel lureKeyLabel = new JLabel("诱饵");
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1;
        add(lureKeyLabel, c);

        final JCheckBox lureCheckBox = new JCheckBox();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.1;
        add(lureCheckBox, c);

        final JTextField lureKeyEdit = new JTextField("");
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.4;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(lureKeyEdit, c);

        final JTextField lureTimeEdit = new JTextField("");
        c.gridx = 3;
        c.gridy = 0;
        c.weightx = 0.4;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(lureTimeEdit, c);
    }


}
