package sm.central.model.student;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.ToString;
import sm.central.model.result.StudentResult;


@Entity
@Data
@ToString(exclude = {"contact_details", "academic_info", "userPass", "fees_details"})
public class Student implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("deprecation")
	@Id
	@GenericGenerator(name = "studentIdGenerator",strategy = "sm.central.idgenerator.StudentIdGenerator")
	@GeneratedValue(generator = "studentIdGenerator")
	private String studentId;
	private String firstName;
	private String lastName;
	
	private LocalDate DOB;
	private String gender;
	private String admissionId;
	@Lob
	private byte [] image;
	
	@CurrentTimestamp
	private Date admissionDate;
	
	@OneToOne(mappedBy = "student",cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
	private Contact_Details contact_details;
	
	@OneToOne(mappedBy = "student",cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
	private Academic_Info academic_info;
	
	@OneToOne(mappedBy = "student",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	private StudentUserPassword userPass;
	
	@OneToMany(mappedBy = "student" ,cascade = CascadeType.ALL,fetch = FetchType.LAZY ,orphanRemoval = true)
	private List<Fees_Details> fees_details;
	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<StudentResult> results;
	
	
	


	


	public void setUserPass(StudentUserPassword userPass) {
		if (userPass==null) {
			if (this.userPass!=null) {
				this.userPass.setStudent(null);
			}
		}
		else {
			userPass.setStudent(this);
		}
		this.userPass = userPass;
	}


	public void setAcademic_info(Academic_Info academic_info) {
		if (academic_info==null) {
			if(this.academic_info!=null) {
				this.academic_info.setStudent(null);
			}
			
		} else {
			academic_info.setStudent(this);
		}
		this.academic_info = academic_info;
	}


	public void setContact_Details(Contact_Details contact_details) {
			if (contact_details==null) {
				if (this.contact_details!=null) {
					this.contact_details.setStudent(null);
				}
			} else {
				contact_details.setStudent(this);
			}
			this.contact_details=contact_details;
	}
	
}
