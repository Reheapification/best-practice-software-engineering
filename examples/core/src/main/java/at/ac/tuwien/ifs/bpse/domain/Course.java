package at.ac.tuwien.ifs.bpse.domain;

import java.util.List;

/**
 * 
 * This class holds the data for one university course. 
 * Course has a connection to Professor, as each course is held by a professors
 * 
 * Several special type of courses can be inherited from this object (e.g., Lab and Lecture in this example)
 * 
 */
public class Course implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4547204730854205742L;

	/* Primary key */
    private int id;

    /* Title of the course */
    private String title;
    /* ECTS points of the course */
    private float ects;
    
    /*
     * A professors can give more than one course, but each course is assigned to precisely one professors
     */
    private List<Professor> professors;

    public List<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public void addProfessor(Professor p) {
    	this.professors.add(p);
    }
    
    public float getEcts() {
        return ects;
    }

    public void setEcts(float ects) {
        this.ects = ects;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
