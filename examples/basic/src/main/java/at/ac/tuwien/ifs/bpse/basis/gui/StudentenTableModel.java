package at.ac.tuwien.ifs.bpse.basis.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.ifs.bpse.basis.helper.Constants;
import at.ac.tuwien.ifs.bpse.dao.IStudentDAO;
import at.ac.tuwien.ifs.bpse.domain.Student;

/**
 * 
 * The Table model encapsulates the complete data needed for the table in the
 * GUI form. This includes reading the data from the database using the data
 * access object (DAO).
 * 
 * @author The SE-Team
 * @version 1.0
 * 
 */
public class StudentenTableModel extends AbstractTableModel {

	/**
	 * serialVersionUID, generated by eclipse.
	 */
	private static final long serialVersionUID = 8737097029189851737L;

	/**
	 * Retrieves the logger for this class.
	 */
	private static Log log = LogFactory.getLog(StudentenTableModel.class);

	/**
	 * The Data Access Object for Students.
	 */
	private IStudentDAO studentDAO = null;

	/**
	 * Spring Framework XML Bean Factory.
	 */
	private XmlBeanFactory xbf;

	/**
	 * Defines the Names of the Columns of the TableModel.
	 */
	private final String[] columnName = { "ID", "Matrikelnummer", "Vorname",
			"Nachname", "Email" };

	/**
	 * The list of Students will be filled by StudentDAO.
	 */
	private List<Student> studenten = null;

	/**
	 * Order of the TableModel, must be "Matrikelnummer" or "Nachname".
	 */
	private String order = "";

	/**
	 * Creates a new StudentenTableModel with default ordering.
	 * 
	 * @param order
	 *            the default Order
	 * @see #order
	 */
	public StudentenTableModel(String order) {
		super();
		ClassPathResource res = new ClassPathResource(Constants.SPRINGBEANS);
		xbf = new XmlBeanFactory(res);
		studentDAO = (IStudentDAO) xbf.getBean("StudentDAO");
		this.order = order;
		readData();
	}

	/**
	 * Get a List of Students represented in this TableModel.
	 * 
	 * @return the Students represented in the TableModel
	 * @see Student
	 * @see List
	 */
	public List<Student> getStudenten() {
		return studenten;
	}

	/**
	 * Returns a single Student out of this TableModel.
	 * 
	 * @param i
	 *            The index of the desired Student.
	 * @return The Student at position <c>i</c>.
	 * @see #getStudenten()
	 */
	public Student getStudentAt(int i) {
		return studenten.get(i);
	}

	/**
	 * Read the Students from the Database with given order.
	 * 
	 * @see StudentDAO#getStudents(String)
	 */
	private void readData() {
		studenten = studentDAO.getStudents(order);
	}

	/**
	 * Reload the Data.
	 */
	public void reload() {
		readData();
		fireTableDataChanged();
	}

	/**
	 * Change the Order and reload the Data.
	 * 
	 * @param order
	 *            the new Order
	 * @see #order
	 */
	public void setOrder(String order) {
		log.info("Order Changed: reading data...");
		this.order = order;
		reload();
	}

	/**
	 * Get the number of rows in this TableModel.
	 * 
	 * @return number of rows
	 */
	public int getRowCount() {
		log.debug("rowcount = " + studenten.size());
		return studenten.size();
	}

	/**
	 * Get the number of columns in this TableModel.
	 * 
	 * @return number of colums
	 */
	public int getColumnCount() {
		log.debug("colCount = " + columnName.length);
		return columnName.length;
	}

	/**
	 * Get the title of a column.
	 * 
	 * @param c
	 *            the column-index
	 * @return the title
	 */
	public String getColumnName(int c) {
		log.debug("colName = " + columnName[c]);
		return columnName[c];
	}

	/**
	 * Get the Class of the data saved in a colum.
	 * 
	 * @param c
	 *            the column-index
	 * @return String.class
	 */
	public Class<?> getColumnClass(int c) {
		return String.class;
	}

	/**
	 * Checks whether a cell is editable or not.
	 * 
	 * @param row
	 *            the Row
	 * @param col
	 *            the Column
	 * @return false in any case.
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * Get the Value from a given Cell, denoted by row and col.
	 * 
	 * @param row
	 *            the Row
	 * @param col
	 *            the Column
	 * @return String containing the value of the cell.
	 */
	public Object getValueAt(int row, int col) {
		log.debug("getValue row = " + row + " col = " + col);
		Student s = studenten.get(row);
		switch (col) {
		case 0:
			return "" + s.getId();
		case 1:
			return s.getMatnr();
		case 2:
			return s.getFirstname();
		case 3:
			return s.getLastname();
		case 4:
			return s.getEmail();
		default:
			return null;
		}
	}

}
