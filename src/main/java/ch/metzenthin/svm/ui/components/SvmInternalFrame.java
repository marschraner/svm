package ch.metzenthin.svm.ui.components;

import java.awt.Dimension;

import javax.swing.*;

public class SvmInternalFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    public SvmInternalFrame(JComponent panel, String title, Dimension dimension) {
        super("#" + (++openFrameCount + " " + title),
                true, // resizable
                true, // closable
                true, // maximizable
                true);// iconifiable

        // ...Create the GUI and put it in the window...
        add(panel);

        // ...Then set the window size or call pack...
        setSize(dimension);

        // Set the window's location.
        setLocation(xOffset * openFrameCount, yOffset * openFrameCount);
    }
}
