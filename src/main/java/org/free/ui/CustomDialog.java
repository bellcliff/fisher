package org.free.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by byang1 on 7/28/16.
 */
public class CustomDialog extends JDialog {
    private JPanel myPanel = null;
    private JButton yesButton = null;
    private JButton noButton = null;

    public CustomDialog(JFrame frame, boolean modal, Rectangle rec) {
        super(frame, modal);
        setBounds(rec);
        setVisible(true);
    }
}
