package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.RecordService;
import uniandes.dse.examen1.services.StatsService;
import uniandes.dse.examen1.services.StudentService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class, StatsService.class })
public class StatServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StatsService statService;

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
        semester = "2022-10";
    }

    @Test
    void testCalculateStudentGrade() {
        try {
            recordService.createRecord(login, courseCode, 4.0, semester);
            recordService.createRecord(login, courseCode, 3.0, semester);
            double averageGrade = statService.calculateStudentAverage(login);
            assertEquals(3.5, averageGrade);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void testCalculateCourseAverage() {
        try {
            double averageGrade = statService.calculateCourseAverage(login);
            assertEquals(0.0, averageGrade);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
