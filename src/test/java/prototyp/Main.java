package prototyp;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable table;

	DataAccess dataAccess;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public Main() throws Exception {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		dataAccess = new DataAccess();
		final PersonsTableModel model = new PersonsTableModel(dataAccess);
		table = new JTable(model);
		contentPane.add(table, BorderLayout.CENTER);

		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Search pressed");
				try {
					model.search();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(searchButton);

		final PersonDialog dialog = new PersonDialog(this, false);
		JButton newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.out.println("New pressed");
				dialog.setSize(250, 120);
				dialog.setVisible(true);
			}
		});
		buttonPanel.add(newButton);
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		JButton closeIt = new JButton("OK");
		closeIt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Inserting and closing dialog");
				try {
					model.create(dialog.getPerson());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				dialog.dispose();
			}
		});
		dialog.getContentPane().add(closeIt, BorderLayout.SOUTH);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				try {
					dataAccess.tearDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
