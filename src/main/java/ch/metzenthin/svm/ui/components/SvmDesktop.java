package ch.metzenthin.svm.ui.components;

import ch.metzenthin.svm.common.SvmContext;
import ch.metzenthin.svm.common.datatypes.Codetyp;
import ch.metzenthin.svm.persistence.DB;
import ch.metzenthin.svm.persistence.DBFactory;
import ch.metzenthin.svm.ui.control.KursorteController;
import com.apple.eawt.Application;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;
import java.net.URL;
import javax.swing.*;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * SVM Applikation
 */
public class SvmDesktop extends JFrame implements ActionListener {

  @Serial private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LogManager.getLogger(SvmDesktop.class);
  private static final String SCHUELER_SUCHEN = "Schüler suchen";

  private final transient DB db = DBFactory.getInstance();
  @Getter private final transient SvmContext svmContext;
  private JComponent activeComponent;

  public SvmDesktop(SvmContext svmContext) {
    this.svmContext = svmContext;

    // Make the big window be indented 50 pixels from each edge
    // of the screen.
    int inset = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

    // Set up the GUI.
    setJMenuBar(createMenuBar());

    // call quit() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            quit();
          }
        });

    URL iconURL = getClass().getResource("/images/buehne.gif");
    if (iconURL != null) {
      setIconImage(new ImageIcon(iconURL).getImage());
      // Mac Dock-Icon
      if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
        Application.getApplication().setDockIconImage(new ImageIcon(iconURL).getImage());
      }
    }

    // RootPane für Setzen des Default-Buttons in den UIs
    svmContext.setRootPaneJFrame(getRootPane());

    setAndShowActivePanel(createSchuelerSuchenPanel().$$$getRootComponent$$$(), SCHUELER_SUCHEN);

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
    menuSchueler.setMnemonic(KeyEvent.VK_C);
    menuBar.add(menuSchueler);

    JMenu menuMitarbeiter = new JMenu("Mitarbeiter");
    menuMitarbeiter.setMnemonic(KeyEvent.VK_M);
    menuBar.add(menuMitarbeiter);

    JMenu menuSemester = new JMenu("Semester");

    menuBar.add(menuSemester);

    JMenu menuKurse = new JMenu("Kurse");
    menuKurse.setMnemonic(KeyEvent.VK_U);
    menuBar.add(menuKurse);

    JMenu menuMaerchen = new JMenu("Märchen");
    menuBar.add(menuMaerchen);

    JMenu menuLektionsgebuehren = new JMenu("Lektionsgebühren");
    menuBar.add(menuLektionsgebuehren);

    JMenu menuSemesterrechnungen = new JMenu("Rechnungen");
    menuSemesterrechnungen.setMnemonic(KeyEvent.VK_R);
    menuBar.add(menuSemesterrechnungen);

    // Set up the first menu item.
    JMenuItem menuItem = new JMenuItem("Schüler suchen / bearbeiten");
    menuItem.setMnemonic(KeyEvent.VK_C);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("schuelerSuchen");
    menuItem.addActionListener(this);
    menuSchueler.add(menuItem);

    menuItem = new JMenuItem("Neuen Schüler erfassen");
    menuItem.setMnemonic(KeyEvent.VK_N);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("schuelerErfassen");
    menuItem.addActionListener(this);
    menuSchueler.add(menuItem);

    menuItem = new JMenuItem("Schüler-Codes verwalten");
    menuItem.setMnemonic(KeyEvent.VK_O);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("schuelerCodesVerwalten");
    menuItem.addActionListener(this);
    menuSchueler.add(menuItem);

    menuItem = new JMenuItem("Monatsstatistik");
    menuItem.setMnemonic(KeyEvent.VK_M);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("monatsstatistikSchueler");
    menuItem.addActionListener(this);
    menuSchueler.add(menuItem);

    menuItem = new JMenuItem("Mitarbeiter suchen / bearbeiten");
    menuItem.setMnemonic(KeyEvent.VK_M);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("mitarbeiterSuchen");
    menuItem.addActionListener(this);
    menuMitarbeiter.add(menuItem);

    menuItem = new JMenuItem("Mitarbeiter-Codes verwalten");
    menuItem.setMnemonic(KeyEvent.VK_O);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("mitarbeiterCodesVerwalten");
    menuItem.addActionListener(this);
    menuMitarbeiter.add(menuItem);

    menuItem = new JMenuItem("Semester verwalten");
    menuItem.setActionCommand("semesterVerwalten");
    menuItem.addActionListener(this);
    menuSemester.add(menuItem);

    menuItem = new JMenuItem("Kurse verwalten");
    menuItem.setMnemonic(KeyEvent.VK_U);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("kurseVerwalten");
    menuItem.addActionListener(this);
    menuKurse.add(menuItem);

    menuItem = new JMenuItem("Kurstypen verwalten");
    menuItem.setMnemonic(KeyEvent.VK_T);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("kurstypenVerwalten");
    menuItem.addActionListener(this);
    menuKurse.add(menuItem);

    menuItem = new JMenuItem("Kursorte verwalten");
    menuItem.setMnemonic(KeyEvent.VK_O);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("kursorteVerwalten");
    menuItem.addActionListener(this);
    menuKurse.add(menuItem);

    menuItem = new JMenuItem("Monatsstatistik");
    menuItem.setMnemonic(KeyEvent.VK_M);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("monatsstatistikKurse");
    menuItem.addActionListener(this);
    menuKurse.add(menuItem);

    menuItem = new JMenuItem("Märchen verwalten");
    menuItem.setActionCommand("maerchenVerwalten");
    menuItem.addActionListener(this);
    menuMaerchen.add(menuItem);

    menuItem = new JMenuItem("Eltern-Mithilfe-Codes verwalten");
    menuItem.setActionCommand("elternmithilfeCodesVerwalten");
    menuItem.addActionListener(this);
    menuMaerchen.add(menuItem);

    menuItem = new JMenuItem("Semesterrechnungen suchen / bearbeiten");
    menuItem.setMnemonic(KeyEvent.VK_R);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("semesterrechnungenSuchen");
    menuItem.addActionListener(this);
    menuSemesterrechnungen.add(menuItem);

    menuItem = new JMenuItem("Semesterrechnung-Codes verwalten");
    menuItem.setMnemonic(KeyEvent.VK_O);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("semesterrechnungCodesVerwalten");
    menuItem.addActionListener(this);
    menuSemesterrechnungen.add(menuItem);

    menuItem = new JMenuItem("Lektionsgebühren verwalten");
    menuItem.setActionCommand("lektionsgebuehrenVerwalten");
    menuItem.addActionListener(this);
    menuLektionsgebuehren.add(menuItem);

    // Set up the second menu item.
    menuItem = new JMenuItem("Beenden");
    menuItem.setMnemonic(KeyEvent.VK_Q);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.ALT_DOWN_MASK));
    menuItem.setActionCommand("beenden");
    menuItem.addActionListener(this);
    menuDatei.add(menuItem);

    return menuBar;
  }

  // React to menu selections.
  @SuppressWarnings("java:S3776")
  @Override
  public void actionPerformed(ActionEvent e) {
    startWaitCursor();
    if ((activeComponent != null) && !"beenden".equals(e.getActionCommand())) {
      db.closeSession();
      activeComponent.setVisible(false);
    }

    if ("schuelerSuchen".equals(e.getActionCommand())) {
      setAndShowActivePanel(createSchuelerSuchenPanel().$$$getRootComponent$$$(), SCHUELER_SUCHEN);

    } else if ("schuelerErfassen".equals(e.getActionCommand())) {
      SchuelerErfassenPanel schuelerErfassenPanel = new SchuelerErfassenPanel(svmContext);
      schuelerErfassenPanel.addCloseListener(e1 -> onFrameAbbrechen());
      schuelerErfassenPanel.addNextPanelListener(e1 -> onNextPanelAvailable(e1.getSource()));
      setAndShowActivePanel(
          schuelerErfassenPanel.$$$getRootComponent$$$(), "Neuen Schüler erfassen");

    } else if ("schuelerCodesVerwalten".equals(e.getActionCommand())) {
      CodesPanel codesPanel =
          new CodesPanel(svmContext, null, null, null, null, 0, false, false, Codetyp.SCHUELER);
      codesPanel.addCloseListener(e2 -> onFrameAbbrechen());
      setAndShowActivePanel(codesPanel.$$$getRootComponent$$$(), "Schüler-Codes verwalten");

    } else if ("monatsstatistikSchueler".equals(e.getActionCommand())) {
      MonatsstatistikSchuelerPanel anAbmeldestatistikPanel =
          new MonatsstatistikSchuelerPanel(svmContext);
      anAbmeldestatistikPanel.addCloseListener(e3 -> onFrameAbbrechen());
      anAbmeldestatistikPanel.addNextPanelListener(e3 -> onNextPanelAvailable(e3.getSource()));
      setAndShowActivePanel(
          anAbmeldestatistikPanel.$$$getRootComponent$$$(), "Monatsstatistik Schüler");

    } else if ("mitarbeiterSuchen".equals(e.getActionCommand())) {
      MitarbeiterSuchenPanel mitarbeiterSuchenPanel = new MitarbeiterSuchenPanel(svmContext);
      mitarbeiterSuchenPanel.addCloseListener(e4 -> onFrameAbbrechen());
      mitarbeiterSuchenPanel.addNextPanelListener(e4 -> onNextPanelAvailable(e4.getSource()));
      setAndShowActivePanel(mitarbeiterSuchenPanel.$$$getRootComponent$$$(), "Mitarbeiter suchen");

    } else if ("mitarbeiterCodesVerwalten".equals(e.getActionCommand())) {
      CodesPanel codesPanel =
          new CodesPanel(svmContext, null, null, null, null, 0, false, false, Codetyp.MITARBEITER);
      codesPanel.addCloseListener(e5 -> onFrameAbbrechen());
      setAndShowActivePanel(codesPanel.$$$getRootComponent$$$(), "Mitarbeiter-Codes verwalten");

    } else if ("kurseVerwalten".equals(e.getActionCommand())) {
      KurseSemesterwahlPanel kurseSemesterwahlPanel = new KurseSemesterwahlPanel(svmContext);
      kurseSemesterwahlPanel.addCloseListener(e6 -> onFrameAbbrechen());
      kurseSemesterwahlPanel.addNextPanelListener(e6 -> onNextPanelAvailable(e6.getSource()));
      setAndShowActivePanel(
          kurseSemesterwahlPanel.$$$getRootComponent$$$(),
          "Kurse verwalten: Schuljahr / Semester wählen");

    } else if ("kurstypenVerwalten".equals(e.getActionCommand())) {
      KurstypenPanel kurstypenPanel = new KurstypenPanel(svmContext);
      kurstypenPanel.addCloseListener(e7 -> onFrameAbbrechen());
      setAndShowActivePanel(kurstypenPanel.$$$getRootComponent$$$(), "Kurstypen verwalten");

    } else if ("kursorteVerwalten".equals(e.getActionCommand())) {
      KursorteController kursorteController =
          new KursorteController(svmContext, e8 -> onFrameAbbrechen());
      setAndShowActivePanel(kursorteController.getPanelRootComponent(), "Kursorte verwalten");

    } else if ("monatsstatistikKurse".equals(e.getActionCommand())) {
      MonatsstatistikKursePanel monatsstatistikKursePanel =
          new MonatsstatistikKursePanel(svmContext);
      monatsstatistikKursePanel.addCloseListener(e9 -> onFrameAbbrechen());
      setAndShowActivePanel(
          monatsstatistikKursePanel.$$$getRootComponent$$$(), "Monatsstatistik Kurse");

    } else if ("semesterVerwalten".equals(e.getActionCommand())) {
      SemestersPanel semestersPanel = new SemestersPanel(svmContext);
      semestersPanel.addCloseListener(e10 -> onFrameAbbrechen());
      setAndShowActivePanel(semestersPanel.$$$getRootComponent$$$(), "Semester verwalten");

    } else if ("elternmithilfeCodesVerwalten".equals(e.getActionCommand())) {
      CodesPanel codesPanel =
          new CodesPanel(
              svmContext, null, null, null, null, 0, false, false, Codetyp.ELTERNMITHILFE);
      codesPanel.addCloseListener(e11 -> onFrameAbbrechen());
      setAndShowActivePanel(codesPanel.$$$getRootComponent$$$(), "Eltern-Mithilfe-Codes verwalten");

    } else if ("maerchenVerwalten".equals(e.getActionCommand())) {
      MaerchensPanel maerchensPanel = new MaerchensPanel(svmContext);
      maerchensPanel.addCloseListener(e12 -> onFrameAbbrechen());
      setAndShowActivePanel(maerchensPanel.$$$getRootComponent$$$(), "Märchen verwalten");

    } else if ("semesterrechnungenSuchen".equals(e.getActionCommand())) {
      SemesterrechnungenSuchenPanel semesterrechnungenSuchenPanel =
          new SemesterrechnungenSuchenPanel(svmContext);
      semesterrechnungenSuchenPanel.addCloseListener(e13 -> onFrameAbbrechen());
      semesterrechnungenSuchenPanel.addNextPanelListener(
          e13 -> onNextPanelAvailable(e13.getSource()));
      setAndShowActivePanel(
          semesterrechnungenSuchenPanel.$$$getRootComponent$$$(), "Semesterrechnungen suchen");

    } else if ("semesterrechnungCodesVerwalten".equals(e.getActionCommand())) {
      CodesPanel codesPanel =
          new CodesPanel(
              svmContext, null, null, null, null, 0, false, false, Codetyp.SEMESTERRECHNUNG);
      codesPanel.addCloseListener(e14 -> onFrameAbbrechen());
      setAndShowActivePanel(
          codesPanel.$$$getRootComponent$$$(), "Semesterrechnung-Codes verwalten");

    } else if ("lektionsgebuehrenVerwalten".equals(e.getActionCommand())) {
      LektionsgebuehrenPanel lektionsgebuehrenPanel = new LektionsgebuehrenPanel(svmContext);
      lektionsgebuehrenPanel.addCloseListener(e15 -> onFrameAbbrechen());
      setAndShowActivePanel(
          lektionsgebuehrenPanel.$$$getRootComponent$$$(), "Lektionsgebühren verwalten");

    } else { // beenden
      quit();
    }
    stopWaitCursor();
  }

  public void initialize() {
    onFrameAbbrechen();
  }

  private SchuelerSuchenPanel createSchuelerSuchenPanel() {
    SchuelerSuchenPanel schuelerSuchenPanel = new SchuelerSuchenPanel(svmContext);
    schuelerSuchenPanel.addCloseListener(e -> onFrameAbbrechen());
    schuelerSuchenPanel.addNextPanelListener(e -> onNextPanelAvailable(e.getSource()));
    return schuelerSuchenPanel;
  }

  private void setAndShowActivePanel(JComponent component, String title) {
    activeComponent = component;
    getContentPane().add(activeComponent);
    activeComponent.setVisible(true);
    setTitle(title);
    revalidate();
  }

  @SuppressWarnings("java:S5411")
  private void onNextPanelAvailable(Object eventSource) {
    startWaitCursor();
    Object[] eventSourceArray = (Object[]) eventSource;
    LOGGER.trace("Next panel {} aufgerufen", eventSourceArray[1]);
    if (eventSourceArray.length > 2) {
      Object thirdElement = eventSourceArray[2];
      if (thirdElement instanceof Boolean thirdElementBoolean && thirdElementBoolean) {
        LOGGER.trace("Next panel mit close session aufgerufen");
        db.closeSession();
      }
    }
    JComponent nextComponent = (JComponent) eventSourceArray[0];
    activeComponent.setVisible(false);
    getContentPane().remove(activeComponent);
    setAndShowActivePanel(nextComponent, (String) eventSourceArray[1]);
    stopWaitCursor();
  }

  private void onFrameAbbrechen() {
    startWaitCursor();
    db.closeSession();
    if (activeComponent != null) {
      activeComponent.setVisible(false);
      getContentPane().remove(activeComponent);
    }
    setAndShowActivePanel(createSchuelerSuchenPanel().$$$getRootComponent$$$(), SCHUELER_SUCHEN);
    stopWaitCursor();
  }

  // Quit the application.
  private void quit() {
    int n = 0; // default button title
    if (activeComponent != null) {
      Object[] options = {"Ja", "Nein"};
      n =
          JOptionPane.showOptionDialog(
              this,
              "Soll die Applikation wirklich beendet werden?",
              "Applikation beenden",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              options, // the titles of buttons
              options[0]);
    }
    if (n == 0) {
      db.closeSession();
      LOGGER.info("Svm wird beendet.");
      System.exit(0);
    }
  }

  private void startWaitCursor() {
    RootPaneContainer root = (RootPaneContainer) getRootPane().getTopLevelAncestor();
    root.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    root.getGlassPane().setVisible(true);
  }

  private void stopWaitCursor() {
    RootPaneContainer root = (RootPaneContainer) getRootPane().getTopLevelAncestor();
    root.getGlassPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    root.getGlassPane().setVisible(false);
  }
}
