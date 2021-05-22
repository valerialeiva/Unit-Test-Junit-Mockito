package cr.ac.ucr.sa.JunitMockito.controller;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import cr.ac.ucr.sa.JunitMockito.models.Student;
import cr.ac.ucr.sa.JunitMockito.models.StudentResponse;
import cr.ac.ucr.sa.JunitMockito.repository.StudentRepository;

public class StudentControllerTest {

	@Autowired
	StudentResponse studentResponse;

	@Autowired
	Optional<Student> student;

	StudentRepository studentRepositoryMock = Mockito.mock(StudentRepository.class);

	/*@Autowired
	DiferenciaEntreFechas diferenciaEntreFechas = new DiferenciaEntreFechas();*/

	@Autowired
	StudentController studentController = new StudentController(studentRepositoryMock);

	@BeforeEach
	void setUp() {
		Student mockStudent = new Student();
		mockStudent.setIdentificationCard("B63817");
		mockStudent.setStudentName("Valeria");
		mockStudent.setStudentId((long) 1);
		mockStudent.setStudentAge(24);

		Mockito.when(studentRepositoryMock.findStudentByIdentificationCard("B63817")).thenReturn(mockStudent);

	}

	@Test
	void getCountryDetailsWithValidCountryCode() {
		ResponseEntity<StudentResponse> respuestaServicio;
		respuestaServicio = studentController.getStudentDetails("B63817");
		Assertions.assertEquals("Valeria", respuestaServicio.getBody().getStudentName());
	}

	@Test
	void getCountryDetailsWithInvalidCountryCode() {
		ResponseEntity<StudentResponse> respuestaServicio;
		respuestaServicio = studentController.getStudentDetails("B6");
		Assertions.assertNull(respuestaServicio.getBody().getStudentName());
	}

}
