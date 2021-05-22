package cr.ac.ucr.sa.JunitMockito.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cr.ac.ucr.sa.JunitMockito.models.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
	Student findStudentByIdentificationCard(String identificationCard);
}
