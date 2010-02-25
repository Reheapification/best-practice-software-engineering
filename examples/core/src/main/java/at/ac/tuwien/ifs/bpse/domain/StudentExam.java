package at.ac.tuwien.ifs.bpse.domain;


/**
 * This class holds the data of an examn a student has done.
 * Thus there is a reference to the Student object and to the Exam object
 */
public class StudentExam {

    //private int id;

    private Exam exam;

    private Student student;

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
