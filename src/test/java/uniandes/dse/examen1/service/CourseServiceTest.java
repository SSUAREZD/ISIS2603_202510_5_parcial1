package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.services.CourseService;

@DataJpaTest
@Transactional
@Import(CourseService.class)
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateRecordMissingCourse() {
        CourseEntity course = factory.manufacturePojo(CourseEntity.class);
        course.setCode("CS101");
        try {
            CourseEntity savedCourse = courseService.createCourse(course);
            CourseEntity foundCourse = entityManager.find(CourseEntity.class, savedCourse.getId());
            assertEquals(course.getCode(), foundCourse.getCode());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
    }

    @Test
    void testCreateRepeatedCourse() {
        CourseEntity course1 = factory.manufacturePojo(CourseEntity.class);
        course1.setCode("CS101");
        entityManager.persist(course1);

        CourseEntity course2 = factory.manufacturePojo(CourseEntity.class);
        course2.setCode("CS101");

        assertThrows(RepeatedCourseException.class, () -> {
            courseService.createCourse(course2);
        });
    }
}