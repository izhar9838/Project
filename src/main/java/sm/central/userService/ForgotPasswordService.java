package sm.central.userService;

//UserService.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import sm.central.dto.EmailRequestDto;
import sm.central.dto.OtpDto;
import sm.central.dto.ResetPasswordDto;
import sm.central.model.staff.Teacher;
import sm.central.model.staff.Teacher_User_Pass;
import sm.central.model.student.Student;
import sm.central.model.student.StudentUserPassword;
import sm.central.repository.OtpRepository;
import sm.central.repository.UserRepository;
import sm.central.repository.staff.ITeacherRepo;
import sm.central.repository.student.IStudentRepo;
import sm.central.security.model.OtpEntity;
import sm.central.security.model.UserEntity;
import sm.central.util.EmailService;

import java.util.Random;

@Service
public class ForgotPasswordService {
 
 @Autowired
 private UserRepository userRepository;
 @Autowired
 private  IStudentRepo stuRepo;
 @Autowired
 private ITeacherRepo teaRepo;
 @Autowired
 private OtpRepository otpRepository;
 
@Autowired
private EmailService emailService;
 
 // Generate 6-digit OTP
 public String generateOTP() {
     Random random = new Random();
     int otp = 100000 + random.nextInt(900000); // Generates number between 100000 and 999999
     return String.valueOf(otp);
 }
 
 // Request OTP for forgot password
 public String requestForgotPassword(EmailRequestDto request) {
     UserEntity user = userRepository.findByEmail(request.getEmail());
     if (user == null) {
         throw new RuntimeException("User not found");
//         return "User not found";
     }
     
     String otp = generateOTP();
     System.out.println("otp generated");
     OtpEntity otpEntity = otpRepository.findByEmail(request.getEmail());
     System.out.println("finding otpentity"+otpEntity);
     if (otpEntity == null) {
    	 System.out.println("if block if otp entity is null");
         otpEntity = new OtpEntity();
         otpEntity.setEmail(request.getEmail());
     }
     System.out.println("setting otp to otpentity");
     otpEntity.setOtp(otp);
     
     otpEntity.setOtpExpiry(System.currentTimeMillis() + 300000); // 5 minutes expiry
     System.out.println("setting expiration time");
     OtpEntity save = otpRepository.save(otpEntity);
     System.out.println("otp entity save "+save);
     
     // In a real application, send OTP via email/SMS here
     System.out.println("OTP for " + request.getEmail() + ": " + otp);
     emailService.sendOtpEmail(request.getEmail(), otp);
     return "OTP sent to your email";
 }
 
 // Verify OTP
 public String verifyOTP(OtpDto request) {
	 System.out.println("verify otp");
     OtpEntity otpEntity = otpRepository.findByEmail(request.getEmail());
     System.out.println("finding otpentity"+otpEntity);
     if (otpEntity == null) {
         throw new RuntimeException("No OTP found for this email");
     }
     
     if (otpEntity.getOtpExpiry() < System.currentTimeMillis()) {
         throw new RuntimeException("OTP expired");
     }
     
     if (!otpEntity.getOtp().equals(request.getOtp())) {
         throw new RuntimeException("Invalid OTP");
     }
     
     return "OTP verified successfully";
 }
 
 // Reset password
 public String resetPassword(ResetPasswordDto request) {
     UserEntity user = userRepository.findByEmail(request.getEmail());
     System.out.println("otp is : =="+request.getOtp());
     if (user == null) {
         throw new RuntimeException("User not found");
     }
     
     OtpEntity otpEntity = otpRepository.findByEmail(request.getEmail());
     if (otpEntity == null) {
         throw new RuntimeException("No OTP found for this email");
     }
     
     if (otpEntity.getOtpExpiry() < System.currentTimeMillis()) {
         throw new RuntimeException("OTP expired");
     }
     
     if (!otpEntity.getOtp().equals(request.getOtp())) {
         throw new RuntimeException("Invalid OTP");
     }
     

	String username = user.getUsername();

	String role = user.getRole();
	if (role.equals("admin")) {
		 user.setPassword(request.getNewPassword());
	     userRepository.save(user);
	     otpRepository.delete(otpEntity);
	     return "Password reset successfully";
	}
	else if(role.equals("teacher")) {
		Teacher existTeacher = teaRepo.findByUsernameWithDetails(username).get();
		Teacher_User_Pass teacher_user_pass = existTeacher.getTeacher_user_pass();
		existTeacher.getTeacher_user_pass().setTeacher(existTeacher);
		teacher_user_pass.setPassword(request.getNewPassword());
		teaRepo.save(existTeacher);
		userRepository.save(user);
	    otpRepository.delete(otpEntity);
	    return "Password reset successfully";
	}
	else if (role.equals("student")) {
		Student existStudent = stuRepo.findByUsernameWithDetails(username).get();
		StudentUserPassword userPass = existStudent.getUserPass();
		existStudent.getUserPass().setStudent(existStudent);
		userPass.setPassword(request.getNewPassword());
		stuRepo.save(existStudent);
		userRepository.save(user);
	    otpRepository.delete(otpEntity);
	    return "Password reset successfully";
		
	}
    
     
     throw new RuntimeException("Some Server Error Occured");
 }
}