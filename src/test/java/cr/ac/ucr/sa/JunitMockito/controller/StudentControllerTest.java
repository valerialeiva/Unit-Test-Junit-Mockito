package cr.ac.ucr.sa.JunitMockito.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import cr.ac.ucr.sa.JunitMockito.models.Student;
import cr.ac.ucr.sa.JunitMockito.service.StudentService;
import static org.hamcrest.Matchers.*;

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
    @DisplayName("GET /student/{identificationCard} - return Object and OK Status")
    void get_student_by_identification_card() throws Exception {
    	
    	//give
    	Student mockStudent = new Student("B64219","Maikel",23);
    	when(studentService.findStudentByIdentificationCard("B64219")).thenReturn(Optional.of(mockStudent));

        //when
        mockMvc.perform(get("/student/{identificationCard}","B64219"))
        
        //then
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.identificationCard", is("B64219")))
               .andExpect(jsonPath("$.studentName", is("Maikel")))
               .andExpect(jsonPath("$.studentAge", is(23)));
    }
	
    @Test
    @DisplayName("GET /student/{identificationCard} - return Not Found Status ")
    void get_studentById_ThrowsStudentNotFoundException() throws Exception {
    	//give
    	when(studentService.findStudentByIdentificationCard("B64219")).thenReturn(Optional.empty());

    	//when
        mockMvc.perform(get("/student/{identificationCard}","B64219"))
                
        	//them
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("GET /student/students - return all student and OK")
    void get_allStudents_returnsOkWithListOfStudents() throws Exception {
        //Give
        Student mockStudent1 = new Student("B64219", "Maikel", 23);
        Student mockStudent2 = new Student("B65722", "Jordan", 22);
        Student mockStudent3 = new Student("B58421", "Juanito", 24);
        Mockito.when(studentService.findAll()).thenReturn(Lists.newArrayList(mockStudent1, mockStudent2,mockStudent3));

        //when
        mockMvc.perform(get("/student/students"))
                
        //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                
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
    @DisplayName("POST /student/save - return Created Status")
	public void post_creates_new_student_andReturnsObjWith201() throws Exception {
		//give
    	Student mockStudent = new Student("B55555", "Pepe", 25);
		Mockito.when(studentService.createStudent(Mockito.any(Student.class)))
		.thenReturn(mockStudent);
		
		//when
		MockHttpServletRequestBuilder builder = 
				MockMvcRequestBuilders.post("/student/save")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.characterEncoding("UTF-8")
				.content(this.mapper.writeValueAsBytes(mockStudent));
		mockMvc.perform(builder)
		
		//then
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.identificationCard", is("B55555")))
		.andExpect(MockMvcResultMatchers.content()
				.string(this.mapper.writeValueAsString(mockStudent)));
	}

    @Test
    @DisplayName("PUT /student/update - return Accepted Status")
    public void put_updatesAndReturnsUpdatedObjWith202() throws Exception {
    	//give
    	Student mockStudent = new Student("B63817", "Pepe", 25);
    	
    	when(studentService.findStudentByIdentificationCard("B63817"))
    		.thenReturn(Optional.of(mockStudent));
    	
    	when(studentService.updateStudent(Mockito.anyString(), Mockito.any(Student.class)))
			.thenReturn(mockStudent);

    	//when
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.put("/student/update/B63817").contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
				.content(this.mapper.writeValueAsBytes(mockStudent));
		
		mockMvc.perform(builder)
		//then
		.andExpect(status().isAccepted())
		.andExpect(jsonPath("$.identificationCard", is("B63817")))
		.andExpect(MockMvcResultMatchers.content()
				.string(this.mapper.writeValueAsString(mockStudent)));
	}


	@Test
	@DisplayName("DELETE /student/delete/{id} - return NoContent Status")
	public void delete_deleteStudent_Returns204Status() throws Exception {
		//give
		Long id = (long) 1;
		StudentService serviceSpy = Mockito.spy(studentService);
		Mockito.doNothing().when(serviceSpy).deleteStudent(id);

		//when
		mockMvc.perform(MockMvcRequestBuilders.delete("/student/delete/1")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
		//then
		verify(studentService, times(1)).deleteStudent(id);
	}

}
