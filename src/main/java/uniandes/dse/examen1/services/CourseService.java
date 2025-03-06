package uniandes.dse.examen1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.repositories.CourseRepository;

@Slf4j
@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    public CourseEntity createCourse(CourseEntity newCourse) throws Exception {

        CourseEntity curso = CourseRepository.findCoursebyCode();
        if (newCourse == null){
            throw new Exception("El curso no puede ser nulo");
        }
        if (newCourse == null){}
        return courseRepository.save(newCourse);
    }

}

