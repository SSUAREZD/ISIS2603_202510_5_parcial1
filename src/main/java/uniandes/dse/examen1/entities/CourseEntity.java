package uniandes.dse.examen1.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class CourseEntity {

    @PodamExclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique code for the course
     */
    private String courseCode;

    /**
     * The standard name for the course
     */
    private String name;

    /**
     * The number of credits for the course: a number between 1 and 9
     */
    private Integer credits;

    /**
     * A list with the students that have been enrolled in this course.
     * No student should appear more than once in this list
     */
    @ManyToMany(mappedBy = "cursos")
    private List<StudentEntity> estudiantes = new ArrayList<>();

    /**
     * A list of the records of courses that the student has taken so far.
     * Each record indicates the semester, the course, and the final grade of the
     * student in the course.
     */
    @OneToMany(mappedBy = "curso")
    private List<RecordEntity> records = new ArrayList<>();

    public String getCode() {
        return courseCode;
    }

    public List<StudentEntity> getStudentEntities() {
        return estudiantes;
    }

    public void setCode(String courseCode) {
        this.courseCode = courseCode;
    }

}
