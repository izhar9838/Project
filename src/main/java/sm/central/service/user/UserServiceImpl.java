package sm.central.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import sm.central.dto.ChangePasswordDto;
import sm.central.dto.UpdateUserDto;
import sm.central.dto.UserInfoDto;
import sm.central.model.content.Announcement;
import sm.central.model.content.BlogPost;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.model.staff.Teacher;
import sm.central.model.staff.Teacher_Contact_Details;
import sm.central.model.staff.Teacher_User_Pass;
import sm.central.model.student.Contact_Details;
import sm.central.model.student.Student;
import sm.central.model.student.StudentUserPassword;
import sm.central.repository.UserRepository;
import sm.central.repository.content.IAnnoucementRepo;
import sm.central.repository.content.IBlogRepository;
import sm.central.repository.content.IHallOfFameRepo;
import sm.central.repository.content.TimetableRepository;
import sm.central.repository.staff.ITeacherRepo;
import sm.central.repository.student.IStudentRepo;
import sm.central.security.JwtUtil;
import sm.central.security.model.CustomUserDetails;
import sm.central.security.model.UserEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl  implements IUserService{
    @Autowired
    private TimetableRepository repository;
    @Autowired
    private IHallOfFameRepo hallRepo;
    @Autowired
    private IBlogRepository blogRepo;
    @Autowired
    private ITeacherRepo teacherRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private IStudentRepo stuRepo;
    @Autowired
    private IAnnoucementRepo annoucRepo;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Timetable> getAllTimetables() {
        return repository.findAll();
    }


	@Override
	public List<HallOfFameEntity> getAllHallOfFame() {
		// TODO Auto-generated method stub
		return hallRepo.findAll();
	}


	@Override
	public List<BlogPost> getAllBlogPost() {
		// TODO Auto-generated method stub
		return blogRepo.findAll();
	}


	@Override
	public BlogPost getBlog(Long id) {
		// TODO Auto-generated method stub
		return blogRepo.findById(id).get();
	}


	public UserInfoDto getUserInfo(String username, String role) {
		if (role.equalsIgnoreCase("teacher")) {
			if (username == null || username.isEmpty()) {
//	            logger.warn("Username is null or empty");
	            throw new IllegalArgumentException("Username cannot be null or empty");
	        }

	        Optional<Teacher> teacherOptional = teacherRepo.findByUsernameWithDetails(username);
	        if (teacherOptional.isEmpty()) {
//	            logger.warn("No Teacher found for username: {}", username);
	            throw new EntityNotFoundException("No Teacher found for username: " + username);
	        }
			Optional<Teacher> optional = teacherRepo.findByUsernameWithDetails(username);
			Teacher teacher = optional.get();
			UserInfoDto infoDto = new UserInfoDto();
			infoDto.setId(teacher.getTeacherId());
			infoDto.setFullName(teacher.getFirstName()+""+teacher.getLastName());
			infoDto.setRole(role);
			infoDto.setPassword(teacher.getTeacher_user_pass().getPassword());
			infoDto.setUsername(username);
			infoDto.setImage(teacher.getImage());
			infoDto.setEmail(teacher.getTeacher_user_pass().getEmail());
			infoDto.setPhoneNumber(teacher.getTeacher_contact().getPhoneNumber());
			return infoDto;
		}
		else if (role.equalsIgnoreCase("admin")) {
			UserEntity userEntity = userRepo.findByUsername(username);
			UserInfoDto infoDto=new UserInfoDto();
			infoDto.setFullName("Izhar Ali");
			infoDto.setEmail(userEntity.getEmail());
			infoDto.setId("SUPER ADMIN");
			infoDto.setImage(userEntity.getProfileImage());
			infoDto.setRole(userEntity.getRole());
//			infoDto.setPassword(userEntity.getPassword());
			infoDto.setUsername(username);
			infoDto.setPhoneNumber(userEntity.getPhoneNumber());
			return infoDto;
		}
		else if (role.equals("student")) {
			if (username == null || username.isEmpty()) {
	            throw new IllegalArgumentException("Username cannot be null or empty");
	        }
			Optional<Student> studentOptional = stuRepo.findByUsernameWithDetails(username);
			if (studentOptional.isEmpty()) {
//	            logger.warn("No Teacher found for username: {}", username);
	            throw new EntityNotFoundException("No Teacher found for username: " + username);
	        }
			Student student = studentOptional.get();
			UserInfoDto infoDto = new UserInfoDto();
			infoDto.setId(student.getStudentId());
			infoDto.setFullName(String.format("%s %s", student.getFirstName(), student.getLastName()));
			System.out.println(" \n \n \n \n \n Full name is "+infoDto.getFullName());
			infoDto.setRole(role);
			infoDto.setPassword(student.getUserPass().getPassword());
			infoDto.setUsername(username);
			infoDto.setImage(student.getImage());
			infoDto.setEmail(student.getContact_details().getEmail());
			infoDto.setPhoneNumber(student.getContact_details().getPhoneNumber());
			return infoDto;
			
		}
		 throw new RuntimeException("Some Server Error Occured") ;
	}


	public UserEntity updateProfile(UpdateUserDto userDto,String username) {
		UserEntity exitsUser = userRepo.findByUsername(username);
		String role=exitsUser.getRole();
		if (role.equalsIgnoreCase("teacher")) {
			Teacher existteacher = teacherRepo.findByUsernameWithDetails(username).get();
			existteacher.setFirstName(userDto.getFirstName());
			existteacher.setLastName(userDto.getLastName());
			Teacher_Contact_Details teacher_contact = existteacher.getTeacher_contact();
			existteacher.getTeacher_contact().setTeacher(existteacher);
			
			teacher_contact.setPhoneNumber(userDto.getPhoneNumber());
			teacher_contact.setEmail(userDto.getEmail());
			existteacher.setImage(userDto.getProfileImage());
			Teacher_User_Pass teacher_user_pass = existteacher.getTeacher_user_pass();
			existteacher.getTeacher_user_pass().setTeacher(existteacher);
			
			teacher_user_pass.setEmail(userDto.getEmail());
			teacher_user_pass.setPhoneNumber(userDto.getPhoneNumber());
			teacher_user_pass.setPorfileImage(userDto.getProfileImage());
			
			exitsUser.setEmail(userDto.getEmail());
			exitsUser.setPhoneNumber(userDto.getPhoneNumber());
			exitsUser.setProfileImage(userDto.getProfileImage());
			teacherRepo.save(existteacher);
			UserEntity newUser = userRepo.save(exitsUser);

			return newUser;
			
		}
		else if (role.equalsIgnoreCase("admin")) {
			exitsUser.setEmail(userDto.getEmail());
			exitsUser.setPhoneNumber(userDto.getPhoneNumber());
			exitsUser.setProfileImage(userDto.getProfileImage());
			UserEntity newUser = userRepo.save(exitsUser);
			return newUser;
		}
		else if(role.equals("student")) {
			Student existStudent = stuRepo.findByUsernameWithDetails(username).get();
			existStudent.setFirstName(userDto.getFirstName());
			existStudent.setLastName(userDto.getLastName());
			Contact_Details contact_details = existStudent.getContact_details();
			existStudent.getContact_details().setStudent(existStudent);
			
			contact_details.setPhoneNumber(userDto.getPhoneNumber());
			contact_details.setEmail(userDto.getEmail());
			existStudent.setImage(userDto.getProfileImage());
			StudentUserPassword userPass = existStudent.getUserPass();
			existStudent.getUserPass().setStudent(existStudent);
			
			userPass.setEmail(userDto.getEmail());
			userPass.setPhoneNumber(userDto.getPhoneNumber());
			userPass.setProfileImage(userDto.getProfileImage());
			
			exitsUser.setEmail(userDto.getEmail());
			exitsUser.setPhoneNumber(userDto.getPhoneNumber());
			exitsUser.setProfileImage(userDto.getProfileImage());
			stuRepo.save(existStudent);
			UserEntity newUser = userRepo.save(exitsUser);
			return newUser;
		}
		
		throw new RuntimeException("Some Server Error Occured");
	}


	@Override
	public String changePassword(ChangePasswordDto changeDto,String username) {
		 UserEntity existUser = userRepo.findByUsername(username);
		 if (existUser.getPassword().equals(changeDto.getCurrentPassword())) {
			 if (existUser.getRole().equalsIgnoreCase("teacher")) {
					Teacher teacher = teacherRepo.findByUsernameWithDetails(username).get();
					teacher.getTeacher_user_pass().setPassword(changeDto.getNewPassword());
					existUser.setPassword(changeDto.getNewPassword());
					userRepo.save(existUser);
					teacherRepo.save(teacher);
					return "Password Changed Successfully";
					
				}
			 else if (existUser.getRole().equalsIgnoreCase("admin")) {
				existUser.setPassword(changeDto.getNewPassword());
				userRepo.save(existUser);
				return "Password Changed Successfully";
			}
			 else if ( existUser.getRole().equals("student")) {
				 existUser.setPassword(changeDto.getNewPassword());
				 Student existStudent = stuRepo.findByUsernameWithDetails(username).get();
				 StudentUserPassword userPass = existStudent.getUserPass();
				 existStudent.getUserPass().setStudent(existStudent);
				 userPass.setPassword(changeDto.getNewPassword());
				 stuRepo.save(existStudent);
				 userRepo.save(existUser);
				 return "Password Changed Succesfully";
			 }
			
		}
		throw new RuntimeException("Some Server Erro Occured");
	}


	@Override
	public List<Announcement> getAnnouncements() {
		// TODO Auto-generated method stub
		return annoucRepo.findAll();
	}

	

    
}