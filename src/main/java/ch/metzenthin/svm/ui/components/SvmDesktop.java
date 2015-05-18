package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/*
 * SVM Applikation
 */
public class SvmDesktop extends JFrame implements ActionListener, InternalFrameListener {

    private static final long serialVersionUID = 1L;

    private final JDesktopPane desktop;
    private final SvmContext svmContext;
    private SvmInternalFrame schuelerErfassenFrame;

    public SvmDesktop(SvmContext svmContext) {
        super("SVM Schüler Verwaltung Metzenthin");
        this.svmContext = svmContext;

        // Make the big window be indented 50 pixels from each edge
        // of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        // Set up the GUI.
        desktop = new JDesktopPane(); // a specialized layered pane
        // createFrame(); // create first "window"
        setContentPane(desktop);
        setJMenuBar(createMenuBar());

        // Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Display the window.
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Set up the lone menu.
        JMenu menuDatei = new JMenu("Datei");
        menuDatei.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menuDatei);

        JMenu menuSchueler = new JMenu("Schüler");
        menuSchueler.setMnemonic(KeyEvent.VK_S);
        menuBar.add(menuSchueler);

        // Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("Schüler erfassen");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_MASK));
        menuItem.setActionCommand("schuelerErfassen");
        menuItem.addActionListener(this);
        menuSchueler.add(menuItem);

        // Set up the second menu item.
        menuItem = new JMenuItem("Beenden");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_MASK));
        menuItem.setActionCommand("beenden");
        menuItem.addActionListener(this);
        menuDatei.add(menuItem);

        return menuBar;
    }

    // React to menu selections.
    public void actionPerformed(ActionEvent e) {
        if ("schuelerErfassen".equals(e.getActionCommand())) {
            SchuelerErfassenPanel schuelerErfassenPanel = new SchuelerErfassenPanel(svmContext);
            schuelerErfassenPanel.addCloseListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onSchuelerErfassenFrameAbbrechen();
                }
            });
            schuelerErfassenFrame = createFrame(schuelerErfassenPanel.$$$getRootComponent$$$(), "Schüler erfassen", new Dimension(1000, 800));
        } else { // beenden
            quit();
        }
    }

    private void onSchuelerErfassenFrameAbbrechen() {
        schuelerErfassenFrame.dispose();
    }

    // Create a new internal frame.
    private SvmInternalFrame createFrame(JComponent panel, String title, Dimension dimension) {
        SvmInternalFrame frame = new SvmInternalFrame(panel, title, dimension);
        frame.setVisible(true); // necessary as of 1.3
        frame.addInternalFrameListener(this);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ignore) {
        }
        return frame;
    }

    // Quit the application.
    private void quit() {
        System.exit(0);
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent ignore) {
        // empty
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent ignore) {
        // empty
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        System.out.println("Frame closed: " + e.getInternalFrame().getTitle());
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent ignore) {
        // empty
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent ignore) {
        // empty
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent ignore) {
        // empty
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent ignore) {
        // empty
    }

}