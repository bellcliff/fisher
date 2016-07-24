package org.free.ui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Created by byang1 on 7/17/16.
 */
class StatPanel extends JPanel{

    StatPanel(){
        setBorder(new CompoundBorder(new EmptyBorder(0, 10, 10, 10), new TitledBorder("Stat")));


    }
}
