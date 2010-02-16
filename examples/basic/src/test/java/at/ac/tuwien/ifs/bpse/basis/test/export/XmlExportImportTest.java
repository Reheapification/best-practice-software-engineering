package at.ac.tuwien.ifs.bpse.basis.test.export;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.ifs.bpse.basis.domain.Student;
import at.ac.tuwien.ifs.bpse.basis.export_import.XmlExportImport;
import at.ac.tuwien.ifs.bpse.basis.helper.Constants;

/**
 * Class containing the TestCases for the XML-Importer and Exporter. Testing an
 * Importer combined with an Exporter ist rather easy: At first we generate some
 * TestData, then the TestData is exported and imported again. Finally we
 * compare the Imported data with the data we have first exported.
 * 
 * @author The SE-Team
 * @version 1.0
 */
public class XmlExportImportTest extends TestCase {

	/**
	 * The Spring Bean Factory, it is recreated for each new TestCase.
	 */
	private XmlBeanFactory xbf;

	/**
	 * The list of Students used as testdata, also reinitialized for every new
	 * TestCase.
	 */
	private List<Student> studenten;

	/**
	 * This method is invoked before each TestCase.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		ClassPathResource res = new ClassPathResource(Constants.SPRINGBEANS_TEST);
		xbf = new XmlBeanFactory(res);
		studenten = generateStudentList();
	}

	/**
	 * This method is executed after every TestCase.
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		studenten = null;
		xbf.destroySingletons();
	}

	/**
	 * Generate some testdata, this data is generated by the BeanFactory.
	 * 
	 * @return Testdata
	 */
	private List<Student> generateStudentList() {
		List<Student> studenten = new ArrayList<Student>();
		Student s1 = (Student) xbf.getBean("StudentAlexanderSchatten");
		s1.setId(new Integer(1));
		Student s2 = (Student) xbf.getBean("StudentHubertMeixner");
		s2.setId(new Integer(2));
		studenten.add(s1);
		studenten.add(s2);
		return studenten;
	}

	/**
	 * This method checks an XML-Document. It also checks for valid XML.
	 * 
	 * @param doc
	 *            The Document to check
	 */
	private void checkXml(Document doc) {
		// check root element
		Element rootEl = doc.getRootElement();
		assertEquals("students", rootEl.getName());
		// check two children
		List studentenEl = rootEl.elements();
		assertEquals(2, studentenEl.size());
		// check one Student Element
		Element sEl = (Element) studentenEl.get(0);
		assertEquals("1", sEl.attributeValue("id"));
		assertEquals("Alexander", sEl.elementText("firstname"));
		assertEquals("Schatten", sEl.elementText("lastname"));
		assertEquals("8925164", sEl.elementText("matnr"));
		assertEquals("alexander@schatten.info", sEl.elementText("email"));
	}

	/**
	 * TestCase for the generation of a XML file.
	 */
	public void testGenerateXML() {
		XmlExportImport xexp = new XmlExportImport();
		xexp.generateXML(studenten);
		checkXml(xexp.getDocument());
	}

	/**
	 * TestCase for saving an XML file.
	 */
	public void testSave() {
		final String filename = "target/test-classes/test/studenten.xml";
		XmlExportImport xexp = new XmlExportImport();
		xexp.generateXML(studenten);
		// save and re-read document in XML
		try {
			xexp.save(filename);
			xexp = null;
			xexp = new XmlExportImport();
			xexp.readXml(filename);
			checkXml(xexp.getDocument());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// read data as List object
		List<Student> impStud = null;
		try {
			impStud = xexp.read(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		impStud.equals(studenten);
		assertNotSame(studenten, impStud);
		assertEquals(studenten.size(), impStud.size());
		// Cycle through the imported students and compare them to the exported ones
		int i = 0;
		for (Student stud: impStud) {
			assertEquals(studenten.get(i).getId(), stud.getId());
			assertEquals(studenten.get(i).getMatnr(), stud.getMatnr());
			assertEquals(studenten.get(i).getFirstname(), stud.getFirstname());
			assertEquals(studenten.get(i).getLastname(), stud.getLastname());
			assertEquals(studenten.get(i).getEmail(), stud.getEmail());
			i++;
		}
		
		File f = new File("target/test-classes/test/studenten.xml");
		f.delete();
	}

}
