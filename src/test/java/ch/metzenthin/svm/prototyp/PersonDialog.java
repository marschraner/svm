package ch.metzenthin.svm.prototyp;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public 	class PersonDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	JLabel label1 = new JLabel("Lastname");
	JLabel label2 = new JLabel("Firstname");
	JLabel label3 = new JLabel("Birth");

	JTextField lastnameField = new JTextField();
	JTextField firstnameField = new JTextField();
	JTextField birthField = new JTextField();

	String[] person = new String[4];

	public PersonDialog(JFrame owner, boolean modal) {
		super(owner, modal);
		init();
	}

	private void init() {
		this.setTitle("Person Dialog");
		this.setLayout(new BorderLayout());
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2));
		panel.add(label1);
		panel.add(lastnameField);
		panel.add(label2);
		panel.add(firstnameField);
		panel.add(label3);
		panel.add(birthField);
		this.add(panel, BorderLayout.CENTER);
	}

	public String[] getPerson() {
		person[0] = lastnameField.getText();
		person[1] = firstnameField.getText();
		person[2] = birthField.getText();
		return person;
	}
}
