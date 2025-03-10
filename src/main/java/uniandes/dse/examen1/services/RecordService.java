package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public RecordEntity createRecord(String loginStudent, String courseCode, Double grade, String semester)
            throws InvalidRecordException {

            java.util.Optional<uniandes.dse.examen1.entities.StudentEntity> estudiante= studentRepository.findByLogin(loginStudent);
            java.util.Optional<uniandes.dse.examen1.entities.CourseEntity> curso= courseRepository.findByCourseCode(courseCode);
            RecordEntity newRecord = new RecordEntity();
            
            if(grade<=3){
                throw new InvalidRecordException("El estudiante ya pasó la materia");
            }
            if(grade<=1.5 && grade>=5){
                throw new InvalidRecordException("La nota no es valida");
            }
            if(estudiante== null){
                throw new InvalidRecordException("El estudiante no existe");
            }
            if(curso == null){
                throw new InvalidRecordException("El curso no existe");
            }

        return recordRepository.save(newRecord);
    }
}
