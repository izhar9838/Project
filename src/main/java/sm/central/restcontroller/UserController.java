package sm.central.restcontroller;



import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sm.central.dto.EmailRequestDto;
import sm.central.dto.OtpDto;
import sm.central.dto.ResetPasswordDto;
import sm.central.model.hallofFame.HallOfFameEntity;
import sm.central.model.timetable.Timetable;
import sm.central.repository.UserRepository;
import sm.central.security.JwtUtil;
import sm.central.security.model.CustomUserDetails;
import sm.central.security.model.LoginResponse;
import sm.central.security.model.UserEntity;
import sm.central.service.user.IUserService;
import sm.central.userService.ForgotPasswordService;



@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class UserController {

	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
    private IUserService freeUserService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ForgotPasswordService userService;
	
	@PostMapping(path = "/login",consumes = "application/json")
	public ResponseEntity<?> loginUser(@RequestBody UserEntity user){
		String username=user.getUsername();
		String password=user.getPassword();
		String role=user.getRole();
		System.out.println(username+"  "+password+"  "+role);
		try {
            // Validate input parameters
			System.out.println("inside try block");
            if (username == null || password == null || role == null || 
                username.trim().isEmpty() || password.trim().isEmpty() || role.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "All fields (username, password, role) are required");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            System.out.println("Authentication Start");
            // Authenticate username and password
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            System.out.println("cusotmuserdetails");

            // Get user details
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            System.out.println(userDetails);

            // Check if the requested role matches the user's role
            if (!userDetails.getRole().equalsIgnoreCase(role)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "You don't have this role");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }
            System.out.println("generating token");
            // Generate JWT token if all checks pass
            String token = jwtUtil.generateToken(userDetails, role);
            System.out.println("token generated successfully;");
            //getting user whole data from db
            UserEntity wholeUser = userRepo.findByUsername(username);
            //prepare login response data for sending;
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setId(wholeUser.getId());
            loginResponse.setUsername(wholeUser.getUsername());
            loginResponse.setEmail(wholeUser.getEmail());
            loginResponse.setPasswrod(wholeUser.getPassword());
            loginResponse.setRole(wholeUser.getRole());
            String base64Image = wholeUser.getProfileImage() != null ? 
                    Base64.getEncoder().encodeToString(wholeUser.getProfileImage()) : null;
            loginResponse.setProfileImage(base64Image);
            // Prepare success response
            Map<String, Object> response = new HashMap<>();
            response.put("token",token);
            response.put("user", loginResponse);
            
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid username or password");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "An error occurred during authentication");
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
		
	}
	@PostMapping(path="/forgot-password" ,consumes = "application/json")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailRequestDto request) {
		String msg = userService.requestForgotPassword(request);
		
        return new ResponseEntity<>(msg,HttpStatus.OK);
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOTP(@RequestBody OtpDto request) {
        return ResponseEntity.ok(userService.verifyOTP(request));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDto request) {
        return ResponseEntity.ok(userService.resetPassword(request));
    }
		
    @GetMapping("/getTimeTable")
    public List<Timetable> getAllTimetables() {
        return freeUserService.getAllTimetables();
    }
    @GetMapping("/getHallOfFame")
	public List<HallOfFameEntity> getAllHallOfFame(){
		return freeUserService.getAllHallOfFame();
	}
    @GetMapping("/api/user/profile-image")
    public ResponseEntity<?> getProfileImage(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtil.extractUsername(token); // Decode JWT to get "sub"
        UserEntity byUsername = userRepo.findByUsername(username);
        byte []profileImage=byUsername.getProfileImage();
        return ResponseEntity.ok(Collections.singletonMap("profileImage", profileImage));
    }

}
