package cr.ac.ucr.sa.JunitMockito.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import org.springframework.lang.NonNull;


@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Long studentId;
	
	@Column(name = "identification_card", nullable = false)
	@NonNull
	@NotEmpty(message = "'identification_card' field was empty")
	private String identificationCard;
	
	@Column(name = "student_name")
	private String studentName;
	@Column(name = "student_age")
	private int studentAge;
	
	
	
	public Student(String identificationCard, String studentName, int studentAge) {
		super();
		this.identificationCard = identificationCard;
		this.studentName = studentName;
		this.studentAge = studentAge;
	}
	

	public Long getStudentId() {
		return studentId;
	}
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	public String getIdentificationCard() {
		return identificationCard;
	}
	public void setIdentificationCard(String identificationCard) {
		this.identificationCard = identificationCard;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public int getStudentAge() {
		return studentAge;
	}
	public void setStudentAge(int studentAge) {
		this.studentAge = studentAge;
	}
	
}
