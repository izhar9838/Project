package sm.central.service.admin;



import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sm.central.model.classes.Class;
import sm.central.model.hallofFame.HallOfFameEntity;
import sm.central.model.staff.Teacher;
import sm.central.model.staff.Teacher_Contact_Details;
import sm.central.model.staff.Teacher_User_Pass;
import sm.central.model.student.Contact_Details;
import sm.central.model.student.Fees_Details;
import sm.central.model.student.Student;
import sm.central.model.student.StudentUserPassword;
import sm.central.model.timetable.Timetable;
import sm.central.repo.hallofFame.IHallOfFameRepo;
import sm.central.repository.UserRepository;
import sm.central.repository.classes.ClassRepository;
import sm.central.repository.staff.ITeacherRepo;
import sm.central.repository.student.IContactDetailsRepo;
import sm.central.repository.student.IFeesRepo;
import sm.central.repository.student.IStudentRepo;
import sm.central.repp.timetable.TimetableRepository;
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
//	@Autowired
//	private UserRepository userRepo;
	@Override
	public String enrollStudent(Student student) {
		String msg=null;
		try {
			System.out.println("enroll service");
			Student save = sturepo.save(student);
			System.out.println("student saved");
			UserEntity userEntity=new UserEntity();
			StudentUserPassword userPass = student.getUserPass();
			Contact_Details details = student.getContact_details();
			userPass.setEmail(details.getEmail());
			userPass.setProfileImage(student.getImage());
			BeanUtils.copyProperties(userPass, userEntity);
			userrepo.save(userEntity);
			System.out.println("user enttity saved");
			msg="Student save with student id "+save.getStudentId();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
		return msg;
	}
	@Override
	public Fees_Details feesSubmission(Fees_Details fees_details) {
		Fees_Details feeEntity=null;
		try {
			if (fees_details!=null) {
				feeEntity = feerepo.save(fees_details);
				
			}
		} catch (Exception e) {
			throw new RuntimeException("Some Error occured");
		}
		return feeEntity;
		
	}
	public String enrollStaff(Teacher teacher) {
		String msg=null;
		try {
			
			UserEntity userEntity = new UserEntity();
			Teacher_User_Pass teacher_user_pass = teacher.getTeacher_user_pass();
			Teacher_Contact_Details details = teacher.getTeacher_contact();
			teacher_user_pass.setEmail(details.getEmail());
			teacher_user_pass.setPorfileImage(teacher.getImage());
			userEntity.setUsername(teacher_user_pass.getUsername());
			userEntity.setEmail(teacher_user_pass.getEmail());
			userEntity.setPassword(teacher_user_pass.getPassword());
			userEntity.setRole(teacher_user_pass.getRole());
			userEntity.setProfileImage(teacher.getImage());
			Teacher save = teaRepo.save(teacher);
			msg="Teacher is saved with id "+save.getTeacherId();
			userrepo.save(userEntity);
			
		} catch (Exception e) {
			// TODO: handle exception
			return e.getMessage();
		}
		return msg;
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


}
