package ch.metzenthin.svm;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import ch.metzenthin.svm.ui.components.*;

/*
 * SVM Applikation
 */
public class SvmApp extends JFrame implements ActionListener, InternalFrameListener {
    private static final long serialVersionUID = 1L;
    JDesktopPane desktop;

    public SvmApp() {
        super("SVM Schüler Verwaltung Metzenthin");

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
    }

    protected JMenuBar createMenuBar() {
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
            createFrame(new SchuelerErfassenPanel().$$$getRootComponent$$$(), "Schüler erfassen", new Dimension(800, 500));
        } else { // beenden
            quit();
        }
    }

    // Create a new internal frame.
    protected void createFrame(JComponent panel, String title, Dimension dimension) {
        SvmInternalFrame frame = new SvmInternalFrame(panel, title, dimension);
        frame.setVisible(true); // necessary as of 1.3
        frame.addInternalFrameListener(this);
        desktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException ignore) {
        }
    }

    // Quit the application.
    protected void quit() {
        System.exit(0);
    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        SvmApp frame = new SvmApp();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Display the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        System.out.println("Frame closed: " + e.getInternalFrame().getTitle());
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        // TODO Auto-generated method stub
        
    }

}