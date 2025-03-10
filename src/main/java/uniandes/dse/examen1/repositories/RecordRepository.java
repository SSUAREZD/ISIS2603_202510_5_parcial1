package uniandes.dse.examen1.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    Optional<RecordEntity> findById(Long id);
    List<RecordEntity> findByStudentLogin(String login);
    List<RecordEntity> findByStudentLoginAndCurso(String login, CourseEntity courseCode); // Corrected method name
}
