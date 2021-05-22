package cr.ac.ucr.sa.JunitMockito.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cr.ac.ucr.sa.JunitMockito.models.Student;
import cr.ac.ucr.sa.JunitMockito.models.StudentResponse;
import cr.ac.ucr.sa.JunitMockito.repository.StudentRepository;

@RestController()
public class StudentController {

	StudentResponse studentResponse;
	Optional<Student> student;
	StudentRepository studentRepository;

	public StudentController(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@GetMapping(path = "/student/{studentId}")
	public ResponseEntity<StudentResponse> getStudentDetails(@PathVariable("studentId") String studentId) {
		student = Optional.of(new Student());
		studentResponse = new StudentResponse();

		student = Optional.ofNullable(studentRepository.findStudentByIdentificationCard(studentId.toUpperCase()));

		if (student.isPresent()) {
			studentResponse.setIdentificationCard(student.get().getIdentificationCard());
			studentResponse.setStudentName(student.get().getStudentName());
			studentResponse.setStudentAge(student.get().getStudentAge());

		}
		return ResponseEntity.ok(studentResponse);
	}

}
