package sm.central.service.admin;



import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sm.central.dto.FeesDetailsDTO;
import sm.central.model.classes.Class;

import sm.central.model.content.Announcement;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.model.staff.Teacher;
import sm.central.model.staff.Teacher_Contact_Details;
import sm.central.model.staff.Teacher_User_Pass;
import sm.central.model.student.Contact_Details;
import sm.central.model.student.Fees_Details;
import sm.central.model.student.Student;
import sm.central.model.student.StudentUserPassword;
import sm.central.repository.UserRepository;
import sm.central.repository.classes.ClassRepository;
import sm.central.repository.content.IAnnoucementRepo;
import sm.central.repository.content.IHallOfFameRepo;
import sm.central.repository.content.TimetableRepository;
import sm.central.repository.staff.ITeacherRepo;
import sm.central.repository.student.IContactDetailsRepo;
import sm.central.repository.student.IFeesRepo;
import sm.central.repository.student.IStudentRepo;
import sm.central.security.model.UserEntity;

@Service
public class AdminServiceImpl implements IAdminService {
	@Autowired
	private ITeacherRepo teaRepo;
	@Autowired
    private TimetableRepository repository;
	@Autowired
	private IStudentRepo sturepo;
	@Autowired
	private IContactDetailsRepo contrepo;
	@Autowired
	private ClassRepository classRepo;
	@Autowired
	private IFeesRepo feerepo;
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private IHallOfFameRepo hallOfFameRepo;
	@Autowired
	private IAnnoucementRepo annoucRepo;
	@Override
	public String enrollStudent(Student student) {
	    String msg = null;
	    try {
	        if (student == null) {
	            throw new IllegalArgumentException("Student cannot be null");
	        }

	        // Set bidirectional relationship for userPass
	        if (student.getUserPass() != null) {
	            student.getUserPass().setStudent(student);
	        } else {
	            throw new IllegalArgumentException("StudentUserPassword is required");
	        }

	        // Set bidirectional relationship for contact_details
	        if (student.getContact_details() != null) {
	            student.getContact_details().setStudent(student);
	        } else {
	            throw new IllegalArgumentException("Contact_Details is required");
	        }

	        // Set bidirectional relationship for academic_info
	        if (student.getAcademic_info() != null) {
	            student.getAcademic_info().setStudent(student);
	        } else {
	            throw new IllegalArgumentException("Academic_Info is required");
	        }

	        // Set bidirectional relationship for fees_details
	        if (student.getFees_details() != null && !student.getFees_details().isEmpty()) {
	            for (Fees_Details fee : student.getFees_details()) {
	                fee.setStudent(student);
	            }
	        } else {
	            throw new IllegalArgumentException("Fees_Details is required");
	        }
	     // Save user entity for authentication
	        UserEntity userEntity = new UserEntity();
	        StudentUserPassword userPass = student.getUserPass();
	        Contact_Details details = student.getContact_details();
	        userPass.setEmail(details.getEmail());
	        userPass.setProfileImage(student.getImage());
	        userPass.setPhoneNumber(details.getPhoneNumber());
	        // Save the student entity
	        Student saved = sturepo.save(student);
	        System.out.println("Student saved");

	        
	        userEntity.setEmail(userPass.getEmail());
	        userEntity.setPassword(userPass.getPassword());
	        userEntity.setPhoneNumber(userPass.getPhoneNumber());
	        userEntity.setProfileImage(userPass.getProfileImage());
	        userEntity.setRole(userPass.getRole());
	        userEntity.setUsername(userPass.getUsername());
	        userrepo.save(userEntity);
	        System.out.println("User entity saved");

	        msg = "Student saved with student ID " + saved.getStudentId();
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	        return e.getMessage();
	    }
	    return msg;
	}	@Override
	public Fees_Details feesSubmission(FeesDetailsDTO feeDto) {
//		System.out.println("\n \n"+fees_details);
		Fees_Details feeEntity=null;
//		fees_details.setStudent(fees_details.getStudent());
		Student student = sturepo.findById(feeDto.getStudentId()).get();
		Fees_Details feesDetails = new Fees_Details();
	    feesDetails.setAmount(feeDto.getAmount());
	    feesDetails.setFee_type(feeDto.getFee_type());
	    feesDetails.setPayment_mode(feeDto.getPayment_mode());
	    feesDetails.setStudent(student);

	    // Update the Student's feesDetails collection
	    if (student.getFees_details() == null) {
	        student.setFees_details(new HashSet<Fees_Details>());
	    }
	    student.getFees_details().add(feesDetails);
		sturepo.save(student);
		return feeEntity;
		
	}
	@Transactional
    public String enrollStaff(Teacher teacher) {
        try {
            if (teacher == null) {

                throw new IllegalArgumentException("Teacher cannot be null");
            }

            // Ensure bidirectional relationships are set
            if (teacher.getTeacher_user_pass() != null) {
                teacher.getTeacher_user_pass().setTeacher(teacher);
            } else {

                throw new IllegalArgumentException("Teacher_User_Pass is required");
            }
            if (teacher.getTeacher_contact() != null) {
                teacher.getTeacher_contact().setTeacher(teacher);
            } else {
                throw new IllegalArgumentException("Teacher_Contact_Details is required");
            }
            if (teacher.getProfessional_Details() != null) {
                teacher.getProfessional_Details().setTeacher(teacher);
            } else {
                throw new IllegalArgumentException("Teacher_Professional_Details is required");
            }

            // Copy data to UserEntity
            UserEntity userEntity = new UserEntity();
            Teacher_User_Pass teacher_user_pass = teacher.getTeacher_user_pass();
            Teacher_Contact_Details details = teacher.getTeacher_contact();
            userEntity.setUsername(teacher_user_pass.getUsername());
            userEntity.setEmail(details.getEmail());
            userEntity.setPassword(teacher_user_pass.getPassword()); // Hash password
            userEntity.setRole(teacher_user_pass.getRole());
            userEntity.setProfileImage(teacher.getImage());
            userEntity.setPhoneNumber(details.getPhoneNumber());
            teacher_user_pass.setEmail(details.getEmail());
            teacher_user_pass.setPorfileImage(teacher.getImage()); // Note: Fix spelling in entity later

            // Save Teacher (cascades to related entities) and UserEntity
            Teacher savedTeacher = teaRepo.save(teacher);
            userrepo.save(userEntity);

            return "Teacher is saved with id " + savedTeacher.getTeacherId();
        } catch (Exception e) {

            throw new RuntimeException("Error enrolling teacher: " + e.getMessage(), e);
        }
    }
	@Override
	public List<Class> getClassese() {
		List<Class> all = classRepo.findAll();

		return all;
	}
	@Override
	public String saveHallOfFame(HallOfFameEntity hallOfFame) {
		// TODO Auto-generated method stub
		HallOfFameEntity fameEntity = hallOfFameRepo.save(hallOfFame);
		if(fameEntity!=null) {
			return "Person with name of "+fameEntity.getName()+"saved";
		}
		return "Something error occured";
	}
	public Timetable saveTimetable(Timetable timetable) {
    	Timetable byPeriodAndClassName = repository.findByPeriodAndClassName(timetable.getPeriod(), timetable.getClassName());
    	if(byPeriodAndClassName==null) {
    			return repository.save(timetable);
    		}
    	throw new RuntimeException("That Entry is already in TimeTable");
    	}

    public void deleteTimetable(Long id) {
        repository.deleteById(id);
    }
    public String addHallOfFame(HallOfFameEntity hallofFame) {
		HallOfFameEntity save = hallOfFameRepo.save(hallofFame);
		if (save!=null) {
			return hallofFame.getName()+" saved in Database";
		}
		throw new RuntimeException("Some Error occured");
	}
	@Override
	public String addAdmin(UserEntity userEntity) {
		try {
			UserEntity save = userrepo.save(userEntity);
			return "Admin Save";
		} catch (Exception e) {
			throw new RuntimeException("Some Error Occured");
		}
	}
	@Override
	public boolean checkUsernameExists(String username) {
		return userrepo.existsByUsername(username);
		
	}
	@Override
	public Announcement  createAnnouncement(Announcement annoucement) {
		
		return annoucRepo.save(annoucement);
	}
	@Scheduled(fixedRate = 86400000) // Run daily (24 hours = 86,400,000 ms)
    public void deleteOldAnnouncements() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Announcement> oldAnnouncements = annoucRepo.findAll().stream()
                .filter(announcement -> announcement.getCreatedDate().isBefore(oneWeekAgo))
                .toList();
        annoucRepo.deleteAll(oldAnnouncements);
    }
	


}
