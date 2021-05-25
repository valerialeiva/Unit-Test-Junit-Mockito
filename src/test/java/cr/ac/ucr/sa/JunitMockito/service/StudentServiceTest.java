package cr.ac.ucr.sa.JunitMockito.service;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aopalliance.intercept.Invocation;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import cr.ac.ucr.sa.JunitMockito.models.Student;
import cr.ac.ucr.sa.JunitMockito.repository.StudentRepository;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StudentServiceTest {

	@Mock
	private StudentRepository studentRepository;

	@InjectMocks
	private StudentService studentService;

	@Test
	void return_all_students() throws Exception {
		// give
		List<Student> mockStudent = new ArrayList<>();
		mockStudent.add(new Student("B64219", "Maikel", 23));
		mockStudent.add(new Student("B65722", "Jordan", 22));
		mockStudent.add(new Student("B58421", "Juanito", 24));

		given(studentRepository.findAll()).willReturn(mockStudent);

		// when
		List<Student> expected = studentService.findAll();

		// then
		assertAll("Should return address of Oracle's headquarter", () -> assertEquals(expected, mockStudent),
				() -> assertEquals(expected.size(), 3));
	}

	@Test
	void creates_new_student_successfully() throws Exception {
		// give
		Student mockStudent = new Student("B55555", "Daniela", 25);

		given(studentRepository.findStudentByIdentificationCard(mockStudent.getIdentificationCard()))
				.willReturn(Optional.empty());
		given(studentRepository.save(mockStudent)).will(invocation -> invocation.getArgument(0));

		// when
		Student savedStudent = studentService.createStudent(mockStudent);

		// then
		assertThat(savedStudent).isNotNull();
		assertEquals(savedStudent.getIdentificationCard(), "B55555");
	}

	@Test
	void creates_new_student_with_existing_identification_card() throws Exception {

		// give
		Student mockStudent = new Student("B55555", "Daniela", 25);

		given(studentRepository.findStudentByIdentificationCard(mockStudent.getIdentificationCard()))
				.willReturn(Optional.of(mockStudent));

		// when
		Exception exception = assertThrows(Exception.class, () -> {
			studentService.createStudent(mockStudent);
		});

		// then
		String expectedMessage = "User with IdentificationCard " + mockStudent.getIdentificationCard()
				+ " already exists";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void update_student_successfully() {
		// give
		Student mockStudent = new Student("B55555", "Daniela", 25);

		given(studentRepository.findStudentByIdentificationCard(mockStudent.getIdentificationCard()))
				.willReturn(Optional.of(mockStudent));

		given(studentRepository.save(mockStudent)).willReturn(mockStudent);

		// when
		Student updateStudent = studentService.updateStudent(mockStudent.getIdentificationCard(), mockStudent);

		// then
		assertThat(updateStudent).isNotNull();
		assertEquals(updateStudent.getIdentificationCard(), "B55555");

	}

	@Test
	void find_User_by_identification_card() {
		// give
		Student mockStudent = new Student("B55555", "Daniela", 25);

		given(studentRepository.findStudentByIdentificationCard("B55555")).willReturn(Optional.of(mockStudent));
		// when
		Optional<Student> expected = studentService.findStudentByIdentificationCard("B55555");

		// then
		assertThat(expected).isNotNull();
	}

	@Test
	void delete_student() {
		// give
		Long id = (long) 1;
		// when
		studentService.deleteStudent(id);

		// then
		verify(studentRepository, times(1)).deleteById(id);

	}

}
