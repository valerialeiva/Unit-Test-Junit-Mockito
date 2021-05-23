package cr.ac.ucr.sa.JunitMockito.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.databind.ObjectMapper;

import cr.ac.ucr.sa.JunitMockito.models.Student;
import cr.ac.ucr.sa.JunitMockito.service.StudentService;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc
public class StudentControllerTest {
	
	@MockBean
	private StudentService studentService;
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;

    @Test
    @DisplayName("GET /student/{identificationCard} - Success Status")
    void get_student_by_identification_card_if_Success_response_test() throws Exception {
    	
    	//give the Mock Object to return in the test
    	Student mockStudent = new Student("B64219","Maikel",23);
        
    	//Prepare Mockito to intercept the request
        when(studentService.findStudentByIdentificationCard("B64219")).thenReturn(Optional.of(mockStudent));

        // Execute the GET request of the EndPoint
        mockMvc.perform(get("/student/{identificationCard}","B64219"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$.identificationCard", is("B64219")))
                .andExpect(jsonPath("$.studentName", is("Maikel")))
                .andExpect(jsonPath("$.studentAge", is(23)));
    }
	
    @Test
    @DisplayName("GET /student/{identificationCard} - Not Found Status")
    void get_student_by_identification_card_if_not_found_response_test() throws Exception {
    	//Prepare Mockito to intercept the request
        when(studentService.findStudentByIdentificationCard("B64219")).thenReturn(Optional.empty());

     // Execute the GET request of the EndPoint
        mockMvc.perform(get("/student/{identificationCard}","B64219"))
                // Validate the response code
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Call endpoint GET/student/students and then return all student success")
    void get_all_students_with_success_response() throws Exception {
        // Setup our mocked service
        Student mockStudent1 = new Student("B64219", "Maikel", 23);
        Student mockStudent2 = new Student("B65722", "Jordan", 22);
        Student mockStudent3 = new Student("B58421", "Juanito", 24);
        
        when(studentService.findAll()).thenReturn(Lists.newArrayList(mockStudent1, mockStudent2,mockStudent3));

        // Execute the GET request
        mockMvc.perform(get("/student/students"))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                // Validate the returned fields
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].identificationCard", is("B64219")))
                .andExpect(jsonPath("$[0].studentName", is("Maikel")))
                .andExpect(jsonPath("$[0].studentAge", is(23)))
                
                .andExpect(jsonPath("$[1].identificationCard", is("B65722")))
                .andExpect(jsonPath("$[1].studentName", is("Jordan")))
                .andExpect(jsonPath("$[1].studentAge", is(22)))
                
                .andExpect(jsonPath("$[2].identificationCard", is("B58421")))
                .andExpect(jsonPath("$[2].studentName", is("Juanito")))
                .andExpect(jsonPath("$[2].studentAge", is(24)));
    }
    
    @Test
	public void post_creates_new_student_andReturnsObjWith201() throws Exception {
		Student mockStudent = new Student("B55555", "Pepe", 25);
		

		Mockito.when(studentService.createStudent(Mockito.any(Student.class)))
		.thenReturn(mockStudent);

		// Build post request with student object payload
		MockHttpServletRequestBuilder builder = 
				MockMvcRequestBuilders.post("/student/save")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8").content(this.mapper.writeValueAsBytes(mockStudent));

		mockMvc.perform(builder)
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.identificationCard", is("B55555")))
		.andExpect(MockMvcResultMatchers.content().string(this.mapper.writeValueAsString(mockStudent)));
	}



	@Test
	public void delete_deleteStudent_Returns204Status() throws Exception {
		Long id = (long) 1;

		StudentService serviceSpy = Mockito.spy(studentService);
		Mockito.doNothing().when(serviceSpy).deleteStudent(id);

		mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/1")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

		verify(studentService, times(1)).deleteStudent(id);
	}

}
