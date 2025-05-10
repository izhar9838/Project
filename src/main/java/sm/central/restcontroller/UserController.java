package sm.central.restcontroller;



import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import sm.central.dto.ChangePasswordDto;
import sm.central.dto.EmailRequestDto;
import sm.central.dto.OtpDto;
import sm.central.dto.ResetPasswordDto;
import sm.central.dto.UpdateUserDto;
import sm.central.dto.UserInfoDto;
import sm.central.model.content.Announcement;
import sm.central.model.content.BlogPost;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.repository.UserRepository;
import sm.central.security.JwtUtil;
import sm.central.security.model.CustomUserDetails;
import sm.central.security.model.LoginResponse;
import sm.central.security.model.UserEntity;
import sm.central.service.user.IUserService;
import sm.central.userService.ForgotPasswordService;



@RestController
@RequestMapping("/api/public")
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
		try {
            // Validate input parameters
            if (username == null || password == null || role == null ||
                username.trim().isEmpty() || password.trim().isEmpty() || role.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "All fields (username, password, role) are required");
                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
            }
            // Authenticate username and password
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );

            // Get user details
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Check if the requested role matches the user's role
            if (!userDetails.getRole().equalsIgnoreCase(role)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "You don't have this role");
                return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
            }
            // Generate JWT token if all checks pass
            String token = jwtUtil.generateToken(userDetails, role);
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
    @GetMapping(path = "getBlogs" ,produces = "application/json")
    public ResponseEntity<?> getAllBlogPost(){
    	List<BlogPost> allBlogPost = freeUserService.getAllBlogPost();
    	if (allBlogPost!=null) {

			return new ResponseEntity<>(allBlogPost,HttpStatus.OK);
		}
    	throw new RuntimeException("Some Error occured in server");
    }
    @GetMapping(path="/blog/{id:\\d+}", produces = "application/json")
    public ResponseEntity<BlogPost> getBlog(@PathVariable Long id) {
        System.out.println("Fetching blog with ID: " + id);
        if (id <= 0) {
            System.out.println("Invalid blog ID: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BlogPost blog = freeUserService.getBlog(id);
            return new ResponseEntity<>(blog, HttpStatus.OK);

    }
    @GetMapping(path = "/accountInfo",produces = "application/json")
    public ResponseEntity<?> getAccountInfo(@RequestHeader("Authorization") String token){
    	String jwtToken = token.substring(7);
    	String username = jwtUtil.extractUsername(jwtToken);
    	String role = jwtUtil.extractRole(jwtToken);
    	UserInfoDto userInfo = freeUserService.getUserInfo(username, role);
    	return new ResponseEntity<>(userInfo,HttpStatus.OK);
    	
    }
    @GetMapping(path = "/edit-profile",produces = "application/json")
    public ResponseEntity<?> editProfile(@RequestHeader("Authorization") String token){
    	String jwtToken = token.substring(7);
    	String username = jwtUtil.extractUsername(jwtToken);
    	String role = jwtUtil.extractRole(jwtToken);
    	UserInfoDto userInfo = freeUserService.getUserInfo(username, role);
    	return new ResponseEntity<>(userInfo,HttpStatus.OK);
    	
    }
    @PutMapping(path="/updateProfile",produces = "application/json")
    public ResponseEntity<?> updateProfile(@RequestBody UpdateUserDto userDto){
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	UserEntity updateProfile = freeUserService.updateProfile(userDto, username);
    	return new ResponseEntity<>(updateProfile,HttpStatus.OK);

    }
    @PatchMapping(path = "/change-password",produces = "application/json")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changeDto){
    	String username = SecurityContextHolder.getContext().getAuthentication().getName();
    	String msg = freeUserService.changePassword(changeDto, username);
    	return new ResponseEntity<>(msg,HttpStatus.OK);
    }
    @GetMapping(path = "/announcements",produces = "application/json")
    public ResponseEntity<?> getAnnoucement(){
    	List<Announcement> lisAnnouc = freeUserService.getAnnouncements();
    	return new ResponseEntity<>(lisAnnouc,HttpStatus.OK);
    }

}
