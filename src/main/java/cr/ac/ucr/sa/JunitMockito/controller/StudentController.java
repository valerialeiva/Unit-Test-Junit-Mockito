package cr.ac.ucr.sa.JunitMockito.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import cr.ac.ucr.sa.JunitMockito.models.Student;
import cr.ac.ucr.sa.JunitMockito.service.StudentService;
import javassist.NotFoundException;

@RestController()
public class StudentController {

	Optional<Student> student;
	StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping("/student/students")
	public ResponseEntity<List<Student>> getAllStudents() throws URISyntaxException {
		return ResponseEntity.ok().body(studentService.findAll());
	}

	@GetMapping(path = "/student/{identificationCard}")
	public ResponseEntity<?> getStudentByIdentificationCard(
			@PathVariable("identificationCard") String identificationCard) {
		return studentService.findStudentByIdentificationCard(identificationCard).map(student -> {
			return ResponseEntity.ok().body(student);
		}).orElse(ResponseEntity.notFound().build());
	}

	@PostMapping(value = "/student/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) throws Exception {

		Optional<Student> studentR = studentService.findStudentByIdentificationCard(student.getIdentificationCard());
		if (studentR.isPresent()) {
			throw new HttpClientErrorException(HttpStatus.CONFLICT,
					"Student with DNI" + "(" + student.getIdentificationCard() + ") already exists");
		}
		return new ResponseEntity<Student>(studentService.createStudent(student), HttpStatus.CREATED);
	}

	@PutMapping(value = "/student/update/{identificationCard}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> updateStudent(@PathVariable String identificationCard,
			@RequestBody Student student) {

		Optional<Student> studentR = studentService.findStudentByIdentificationCard(identificationCard);
		if (!studentR.isPresent()) {
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND,
					"Student with ID" + "(" + identificationCard + ") not exist");
		}

		return new ResponseEntity<Student>(studentService.updateStudent(identificationCard, student),
				HttpStatus.ACCEPTED);

	}

	@DeleteMapping("/student/delete/{id}")
	public ResponseEntity<Object> deleteStudent(@PathVariable(value = "id") Long id) {

		studentService.deleteStudent(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}

}
