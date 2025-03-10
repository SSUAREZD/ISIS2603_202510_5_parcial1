package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.StudentService;
import uniandes.dse.examen1.services.RecordService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class })
public class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private String login;
    private String courseCode;
    private String semester;

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
        CourseEntity newCourse = factory.manufacturePojo(CourseEntity.class);
        newCourse = courseService.createCourse(newCourse);
        courseCode = newCourse.getCourseCode();

        StudentEntity newStudent = factory.manufacturePojo(StudentEntity.class);
        newStudent = studentService.createStudent(newStudent);
        login = newStudent.getLogin();
    }

    /**
     * Tests the normal creation of a record for a student in a course
     */
    @Test
    void testCreateRecord() {
        try {
            RecordEntity record = recordService.createRecord(login, courseCode, 4.0, semester);
            assertEquals(4.0, record.getFinalGrade());
            assertEquals(login, record.getStudent().getLogin());
            assertEquals(courseCode, record.getCurso().getCourseCode());
        } catch (InvalidRecordException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the creation of a record when the login of the student is wrong
     */
    @Test
    void testCreateRecordMissingStudent() {
        assertThrows(InvalidRecordException.class, () -> {
            recordService.createRecord("wrongLogin", courseCode, 4.0, semester);
        });
    }

    /**
     * Tests the creation of a record when the course code is wrong
     */
    @Test
    void testCreateRecordMissingCourse() {
        assertThrows(InvalidRecordException.class, () -> {
            recordService.createRecord(login, "wrongCourseCode", 4.0, semester);
        });
    }

    /**
     * Tests the creation of a record when the grade is not valid
     */
    @Test
    void testCreateRecordWrongGrade() {
        assertThrows(InvalidRecordException.class, () -> {
            recordService.createRecord(login, courseCode, 6.0, semester);
        });
    }

    /**
     * Tests the creation of a record when the student already has a passing grade
     * for the course
     */
    @Test
    void testCreateRecordRepetida1() {
        try {
            recordService.createRecord(login, courseCode, 4.0, semester);
            assertThrows(InvalidRecordException.class, () -> {
                recordService.createRecord(login, courseCode, 3.0, semester);
            });
        } catch (InvalidRecordException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Tests the creation of a record when the student already has a record for the
     * course, but he has not passed the course yet.
     */
    @Test
    void testCreateRecordRepetida2() {
        try {
            recordService.createRecord(login, courseCode, 2.0,  semester);
            RecordEntity record = recordService.createRecord(login, courseCode, 3.0, semester);
            assertEquals(3.0, record.getFinalGrade());
        } catch (InvalidRecordException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
