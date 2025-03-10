package uniandes.dse.examen1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository estudianteRepository;

    @Autowired
    CourseRepository cursoRepository;

    @Autowired
    RecordRepository inscripcionRepository;

    public Double calculateStudentAverage(String login) {
        List<RecordEntity> inscripciones = inscripcionRepository.findByStudentLogin(login);
        double promedio = 0.0;
        for (RecordEntity inscripcion : inscripciones) {
            promedio += inscripcion.getFinalGrade();
        }
        return promedio / inscripciones.size();
    }

    public Double calculateCourseAverage(String courseCode) {
        Optional<CourseEntity> optionalCourse = cursoRepository.findByCourseCode(courseCode);
        CourseEntity curso = optionalCourse.get();
        
        if (!optionalCourse.isPresent()) {
            throw new IllegalArgumentException("Curso no encontrado");
        }
        CourseEntity course = optionalCourse.get();
        List<StudentEntity> estudiantes = course.getStudentEntities();
        double promedio = 0.0;
        int count = 0;
        
        for (StudentEntity estudiante : estudiantes) {
            List<RecordEntity> inscripciones = inscripcionRepository.findByStudentLoginAndCurso(estudiante.getLogin(), curso);
            for (RecordEntity inscripcion : inscripciones) {
                promedio += inscripcion.getFinalGrade();
                count++;
            }
        }
        
        if (count == 0) {
            return 0.0;
        }
        
        return promedio / count;
    }

}
