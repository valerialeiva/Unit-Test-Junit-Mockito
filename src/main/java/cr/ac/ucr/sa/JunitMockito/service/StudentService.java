package cr.ac.ucr.sa.JunitMockito.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cr.ac.ucr.sa.JunitMockito.models.Student;
import cr.ac.ucr.sa.JunitMockito.repository.StudentRepository;

@Service
@Transactional
public class StudentService {

	@Autowired
    private StudentRepository repository;
	
	
	public Optional<Student> findStudentByIdentificationCard(String identificationCard) {
        return repository.findStudentByIdentificationCard(identificationCard);
    }
	
	public Optional<Student> getStudentById(long id) {
		return repository.findById(id);
	}
	
	public Student createStudent(Student student){
		return repository.save(student);
	}
	
	public List<Student> findAll() {
        return repository.findAll();
    }
	
	public Student updateStudent(String identificationCard , Student studentUpdate) {

		Optional<Student> studentOp = repository.findStudentByIdentificationCard(identificationCard);

			Student orginalStudent = studentOp.get();
			BeanUtils.copyProperties(studentUpdate, orginalStudent);		

		return repository.save(orginalStudent);
	}
	
	public void deleteStudent(long id) {


		repository.deleteById(id);
	}
	
}
