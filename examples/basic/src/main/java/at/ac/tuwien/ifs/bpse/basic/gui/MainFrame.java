package at.ac.tuwien.ifs.bpse.basic.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.ifs.bpse.basic.dao.IStudentDAO;
import at.ac.tuwien.ifs.bpse.basic.dao.IStudentDAO.SortOrder;
import at.ac.tuwien.ifs.bpse.basic.domain.Student;
import at.ac.tuwien.ifs.bpse.basic.export_import.Export;
import at.ac.tuwien.ifs.bpse.basic.helper.Constants;

/**
 * The MainFrame is, as the name says, the main frame of this application. It
 * utilizes a JPanel with a ContentPane and a BorderLayout.
 * 
 * @author The SE-Team
 * @version 1.2
 */
public class MainFrame extends JFrame implements ActionListener {
	/**
	 * serialVersionUID, generated by eclipse.
	 */
	private static final long serialVersionUID = -7167629968906183715L;

	/**
	 * Retrieves the logger for this class.
	 */
	private static Log log = LogFactory.getLog(MainFrame.class);
	
	/**
	 * ResourceBundle to externalize the Strings used for the GUI components.
	 */
	private ResourceBundle messageBundle;

	/**
	 * The Student Table Model, default order is Matrikel Number.
	 * 
	 * @see #initModels()
	 */
	private StudentenTableModel studentenTM;

	/**
	 * The Table to display the Students from the Database.
	 */
	private JTable table;

	/**
	 * Holds the menu entries for exporting data.
	 */
	private ExportMenuModel exportMenuModel;

	private IStudentDAO studentDAO = null;

	/**
	 * The XML Bean Factory from Spring.
	 * 
	 * @see #initDAO()
	 */
	private XmlBeanFactory xbf;

	/**
	 * The Buttons for editing and deleting Students.
	 */
	private JButton editButton, deleteButton;
	
	/**
	 * The Labels for further information of Students.
	 */
	private JLabel id, name, surname, email, studentId;

	/**
	 * The default Constructor for the MainFrame, initiating the DAO, the Models
	 * and the Components. Additionally it defines a ActionListener to clean up
	 * before exit.
	 * 
	 * @see #terminateApplication()
	 */
	public MainFrame() {
		super();
		initDAO();
		initMessageBundle();
		log.info("Initialising MainFrame");
		// Window Listener for Closing Frame
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				super.windowClosing(arg0);
				terminateApplication();
			}
		});
		initModels();
		initComponents();
	}

	/**
	 * Initializes the DAOs. Loads the XmlBean.
	 */
	private void initDAO() {
		ClassPathResource res = new ClassPathResource(Constants.SPRINGBEANS);
		xbf = new XmlBeanFactory(res);
		studentDAO = (IStudentDAO) xbf.getBean("StudentDAO");
	}
	
	/**
	 * Initializes the ResourceBundle to externalize the Strings for the
	 * components.
	 * 
	 * @see ResourceBundle
	 */
	private void initMessageBundle() {
		messageBundle = (ResourceBundle) xbf.getBean("resourceBundle");
	}

	/**
	 * Initializes the Models. Creates the StudentenTableModel (for the Table)
	 * and loads the ExportMenuModel from the XmlBean.
	 * 
	 * @see StudentenTableModel
	 * @see ExportMenuModel
	 */
	private void initModels() {
		studentenTM = new StudentenTableModel(SortOrder.StudentId);
		exportMenuModel = (ExportMenuModel) xbf.getBean("ExportMenuModel");
	}
	
	/**
	 * Initializes all the components of the GUI.
	 */
	private void initComponents() {
		setTitle(messageBundle.getString("app.title"));
		// define menu bar and menus
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu(messageBundle.getString("menu.lbl.file"));
		menuBar.add(fileMenu);
		JMenuItem exitMenuItem = new JMenuItem(messageBundle.getString("menu.lbl.exit"));
		exitMenuItem.setActionCommand(messageBundle.getString("menu.lbl.exit"));
		exitMenuItem.addActionListener(this);
		fileMenu.add(exitMenuItem);
		JMenu exportMenu = new JMenu(messageBundle.getString("menu.lbl.export"));
		menuBar.add(exportMenu);
		// retrieve list of export filters and define corresponding menu items and actions
		List<Export> exports = exportMenuModel.getExportFilter();
		for (Export export : exports) {
			exportMenu.add(new JMenuItem(new ExportAction(export.toString())));
		}
		setJMenuBar(menuBar);
		// define main panel
		JPanel mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		// main panel is divided into two columns via GridLayout, 
		// the two columns themselves are divided into several areas via BorderLayout
		mainPanel.setLayout(new GridLayout(1, 2));
		// define left (sorting order box, information table, action buttons) and right (information box) panel
		JPanel leftColumnPanel = new JPanel();
		leftColumnPanel.setLayout(new BorderLayout());
		leftColumnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		JPanel leftColumnTopPanel = new JPanel();
		leftColumnTopPanel.setLayout(new GridLayout(0, 2, 5, 5));
		leftColumnTopPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		leftColumnPanel.add(leftColumnTopPanel, BorderLayout.NORTH);
		JPanel leftColumnBottomPanel = new JPanel();
		leftColumnBottomPanel.setLayout(new GridLayout(0, 3, 5, 5));
		leftColumnBottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
		leftColumnPanel.add(leftColumnBottomPanel, BorderLayout.SOUTH);
		JPanel rightColumnPanel = new JPanel(new BorderLayout());
		rightColumnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JPanel rightColumnTopPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		rightColumnPanel.add(rightColumnTopPanel, BorderLayout.NORTH);
		mainPanel.add(leftColumnPanel);
		mainPanel.add(rightColumnPanel);
		// define Label for Drop Down Field in North Panel
		leftColumnTopPanel.add(new JLabel(messageBundle.getString("cb.lbl.sort")));
		// define Drop Down Field in North Panel
		JComboBox selectOrderCB = new JComboBox();
		selectOrderCB.setEditable(false);
		selectOrderCB.addItem(messageBundle.getString("cb.option.studentid"));
		selectOrderCB.addItem(messageBundle.getString("cb.option.lastname"));
		leftColumnTopPanel.add(selectOrderCB);
		selectOrderCB.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.SELECTED) {
					log.debug("Order Combo State Changed: " + ie.getItem());
					sortOrderChanged(ie.getItem().toString());
				}
			}
		});
		// define table in center
		table = new JTable();
		table.setModel(studentenTM);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setShowGrid(false);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				updateButtonStatus();
				updateInfoBoxPanel();
				if (e.getClickCount() >= 2) {
					int row = table.getSelectedRow();
					if (row > -1) {
						editStudent();
					}
				}
			}
		});
		table.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				updateButtonStatus();
				updateInfoBoxPanel();
			}

			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_DELETE) {
					deleteStudent();
				}
			}
		});

		JScrollPane tableScrollPane = new JScrollPane(table);
		tableScrollPane.getViewport().setPreferredSize(table.getPreferredSize());
		tableScrollPane.setWheelScrollingEnabled(true);
		leftColumnPanel.add(tableScrollPane, BorderLayout.CENTER);
		// define buttons to manipulate students 
		editButton = new JButton(messageBundle.getString("btn.lbl.edit"));
		editButton.setEnabled(false);
		editButton.addActionListener(this);
		leftColumnBottomPanel.add(editButton);
		JButton createButton = new JButton(messageBundle.getString("btn.lbl.new"));
		createButton.addActionListener(this);
		leftColumnBottomPanel.add(createButton);
		deleteButton = new JButton(messageBundle.getString("btn.lbl.delete"));
		deleteButton.setEnabled(false);
		deleteButton.addActionListener(this);
		leftColumnBottomPanel.add(deleteButton);
		// define panel for info box
		JPanel infoBoxPanel = new JPanel();
		infoBoxPanel.setLayout(new GridLayout(0, 2, 5, 5));
		infoBoxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JScrollPane infoBoxScrollPane = new JScrollPane(infoBoxPanel);
		rightColumnTopPanel.add(infoBoxScrollPane, BorderLayout.CENTER);
		// define labels for info box
		infoBoxPanel.add(new JLabel(messageBundle.getString("lbl.id")));
		id = new JLabel();
		infoBoxPanel.add(id);
		infoBoxPanel.add(new JLabel(messageBundle.getString("lbl.firstname")));
		name = new JLabel();
		infoBoxPanel.add(name);
		infoBoxPanel.add(new JLabel(messageBundle.getString("lbl.lastname")));
		surname = new JLabel();
		infoBoxPanel.add(surname);
		infoBoxPanel.add(new JLabel(messageBundle.getString("lbl.studentid")));
		studentId = new JLabel();
		infoBoxPanel.add(studentId);
		infoBoxPanel.add(new JLabel(messageBundle.getString("lbl.email")));
		email = new JLabel();
		infoBoxPanel.add(email);
	}

	private void updateButtonStatus() {
		if (table.getSelectedRowCount() == 1) {
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
		} else {
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}
	
	/**
	 * Handle changes of information in the InfoBoxPanel
	 */
	private void updateInfoBoxPanel() {
		int row = table.getSelectedRow();
		if (row > -1) {
			Student student = studentenTM.getStudentAt(row);
			id.setText( Integer.toString(student.getId()));
			name.setText(student.getFirstname());
			surname.setText(student.getLastname());
			studentId.setText(student.getMatnr());
			email.setText(student.getEmail());
		}
	}

	private void placeNewFrame(JFrame frame) {
		frame.pack();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Point p = this.getLocation();
		// First try:
		// new Window right next to this one:
		if (screen.width > (this.getLocation().x + this.getWidth() + frame
				.getWidth())) {
			p.x += this.getWidth();
		}
		// Second try:
		// new Window left of this one:
		else if (this.getLocation().x - frame.getWidth() >= 0) {
			p.x -= frame.getWidth();
		}
		// Fallthrough:
		// Place centered over this frame
		else {
			p.x += (this.getWidth() - frame.getWidth()) / 2;
			p.y += (this.getHeight() - frame.getHeight()) / 2;
		}
		frame.setLocation(p);
	}

	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		log.debug("Action Performed \"" + cmd + "\"");
		if (cmd.equals(messageBundle.getString("menu.lbl.exit"))) {
			terminateApplication();
		} else if (cmd.equals(messageBundle.getString("btn.lbl.new"))) {
			createStudent();
		} else if (cmd.equals(messageBundle.getString("btn.lbl.edit"))) {
			editStudent();
		} else if (cmd.equals(messageBundle.getString("btn.lbl.delete"))) {
			deleteStudent();
		}
	}

	// Business Methods

	/**
	 * Handle a create-request from the GUI
	 */
	private void createStudent() {
		EditStudentFrame esf = new EditStudentFrame();
		placeNewFrame(esf);
		esf.pack();
		esf.addUpdateListener(new UpdateListener());
		esf.setVisible(true);
	}

	/**
	 * Handle a edit-request from the GUI
	 *
	 */
	private void editStudent() {
		int row = table.getSelectedRow();
		if (row > -1) {
			EditStudentFrame esf = new EditStudentFrame(studentenTM
					.getStudentAt(row), EditStudentFrame.Mode.Update);
			placeNewFrame(esf);
			esf.pack();
			esf.addUpdateListener(new UpdateListener());
			esf.setVisible(true);
		}
	}

	/**
	 * Handle a delete-request from the GUI
	 *
	 */
	private void deleteStudent() {
		int row = table.getSelectedRow();
		if (row > -1) {
			Student victim = studentenTM.getStudentAt(row);
			// support full I18n with patterns 
			Object [] messageArgs = {victim.getFullname(), victim.getMatnr()};
			MessageFormat formatter = new MessageFormat("");
			formatter.applyPattern(messageBundle.getString("pattern"));
			int yesno = JOptionPane.showConfirmDialog(this, formatter.format(messageArgs), messageBundle.getString("delete.lbl.title"),
					JOptionPane.YES_NO_OPTION);
			if (yesno == 0) {
				if (studentDAO.deleteStudent(victim.getId())) {
					studentenTM.reload();
					JOptionPane.showMessageDialog(this, messageBundle.getString("delete.lbl.success"));
				} else {
					JOptionPane.showMessageDialog(this, messageBundle.getString("delete.lbl.error"));
				}
			}
		}
	}

	/**
	 * Clean up and exit
	 */
	private void terminateApplication() {
		log.info("Closing MainFrame and Exit");
		System.exit(0);
	}

	/**
	 * Change order and reload
	 * 
	 * @param order
	 *            the new order
	 * @see StudentenTableModel#setSortOrder(String)
	 */
	private void sortOrderChanged(String order) {
		log.info("Sort Order Changed to \"" + order + "\"");
		if(order.equals(messageBundle.getString("cb.option.studentid"))) {
			studentenTM.setSortOrder(SortOrder.StudentId);
		} else if (order.equals(messageBundle.getString("cb.option.lastname"))) {
			studentenTM.setSortOrder(SortOrder.LastName);
		}
	}

	/**
	 * Export current view to XML or HTML file. Before saving a
	 * FileSelect-Dialog is shown to choose a filename and a location to save.
	 */
	private void export() {
		Export export = (Export) exportMenuModel.getSelectedItem();
		// Create a file dialog to choose filename for export
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle(messageBundle.getString("export.lbl.filename"));
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnVal = jfc.showSaveDialog(this);
		// check if Export was confirmed or canceled by user
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filename = jfc.getSelectedFile().getPath();
			String extension = export.getExtension();
			if (!extension.startsWith(".")) {
				extension = "." + extension;
			}
			if (!filename.endsWith(extension)) {
				filename += extension;
			}
			log.info("Filename for export = \"" + filename + "\"");
			try {
				export.write(studentenTM.getStudenten(), filename);
				JOptionPane.showMessageDialog(this,
						messageBundle.getString("export.lbl.success"), messageBundle.getString("export.lbl.title"),
						JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this,
						messageBundle.getString("export.lbl.error"),
						messageBundle.getString("export.lbl.title"), JOptionPane.WARNING_MESSAGE);
				log.error("File Writing Error: " + e);
			}
		}
	}

	/**
	 * UpdateListener waits for database-changes and udates the table.
	 * 
	 * @author The SE-Team
	 * @see EditStudentFrame#addUpdateListener(ActionListener)
	 * @since 1.1
	 */
	class UpdateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			log.info("Update studentTM");
			studentenTM.reload();
		}
	}
	
	/**
	 * ExportAction provides the dynamic actions for the export menu.
	 * 
	 * @author The SE-Team
	 * @since 1.2
	 *
	 */
	@SuppressWarnings("serial")
	class ExportAction extends AbstractAction {
		public ExportAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			log.info("ExportAction for menu item " + e.getActionCommand() + " called.");
			exportMenuModel.setSelectedItem(e.getActionCommand());
			export();
		}
	}
}
