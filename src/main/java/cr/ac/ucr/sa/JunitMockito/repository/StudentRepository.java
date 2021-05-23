package cr.ac.ucr.sa.JunitMockito.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import cr.ac.ucr.sa.JunitMockito.models.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	@Query(value = "SELECT * FROM student WHERE identification_card = ?")
	Optional<Student> findStudentByIdentificationCard(String identificationCard);
	
}
