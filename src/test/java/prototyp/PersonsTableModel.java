package prototyp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;


public class PersonsTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private DataAccess dataAccess;

	PersonsTableModel(DataAccess dataAccess) {
		super();
		this.dataAccess = dataAccess;
	}

	List<Person> persons = new ArrayList<Person>();
	String[] columns = {"personid", "lastname", "firstname", "birth"};
	
	@Override
	public int getRowCount() {
		return persons.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Person person = persons.get(rowIndex);
		if (person != null) {
			Object columnValue = null;
			switch (columnIndex) {
			case 0:
				columnValue = person.getPersonid();
				break;
			case 1:
				columnValue = person.getLastname();
				break;
			case 2:
				columnValue = person.getFirstname();
				break;
			case 3:
				columnValue = person.getBirth();
				break;

			default:
				break;
			}
			return columnValue;
		}
		return null;
	}
	
	public void search() throws Exception {
		this.persons = dataAccess.query();
		fireTableDataChanged();
	}

	public void create(String[] personAttributes) throws Exception {
		Person person = new Person(personAttributes[0], personAttributes[1], new Date());
		dataAccess.persist(person);
		search();
	}
}
