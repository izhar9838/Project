package sm.central.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import sm.central.dto.user.*;
import sm.central.model.content.Announcement;
import sm.central.model.content.BlogPost;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.model.token.RefreshToken;
import sm.central.repository.UserRepository;
import sm.central.security.JwtUtil;
import sm.central.security.model.CustomUserDetails;
import sm.central.security.model.LoginResponse;
import sm.central.security.model.UserEntity;
import sm.central.service.token.RefreshTokenService;
import sm.central.service.user.IUserService;
import sm.central.userService.ForgotPasswordService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/public")
public class UserController {
    @Autowired
    private RefreshTokenService refreshTokenService;

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
    @Autowired
    private UserDetailsService userDetailsService;
	
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
            RefreshToken refreshToken=refreshTokenService.createRefreshToken(username);
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
            response.put("refreshToken", refreshToken.getToken());
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
    @PostMapping(path = "/logout")
    public ResponseEntity<?> logOut(@Valid @RequestBody LogoutRequest logoutRequest){
        String refreshToken = logoutRequest.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        try {
            refreshTokenService.deleteByToken(refreshToken);
            return ResponseEntity.ok("Logged out successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to logout: " + e.getMessage());
        }
    }
    @PostMapping("/auth/refresh-token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        try {
            return refreshTokenService.findByToken(refreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(refreshTokenEntity -> {
                        UserEntity user = freeUserService.findByUsername(refreshTokenEntity.getUsername());
                        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                        String accessToken = jwtUtil.generateToken(userDetails, user.getRole());
                        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshTokenEntity);
                        Map<String, Object> response = new HashMap<>();
                        response.put("accessToken", accessToken);
                        response.put("refreshToken", newRefreshToken.getToken());
                        response.put("user", user);
                        return new ResponseEntity<>(response, HttpStatus.OK);
                    })
                    .orElseGet(() -> ResponseEntity.status(401).body(Map.of("Msg", "Invalid Refresh Token")));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("Msg", e.getMessage() != null ? e.getMessage() : "Invalid or expired refresh token"));
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
    @GetMapping("/user/profile-image")
    public ResponseEntity<?> getProfileImage() {
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        // Decode JWT to get "sub"
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
    public ResponseEntity<?> getAccountInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // e.g., belal123
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth)
                .orElse(null); // e.g., STUDENT

        if (role == null) {
            return new ResponseEntity<>(Map.of("message", "No role assigned"), HttpStatus.BAD_REQUEST);
        }
    	UserInfoDto userInfo = freeUserService.getUserInfo(username, role);
    	return new ResponseEntity<>(userInfo,HttpStatus.OK);
    	
    }
    @GetMapping(path = "/edit-profile",produces = "application/json")
    public ResponseEntity<?> editProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // e.g., belal123
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.startsWith("ROLE_") ? auth.substring(5) : auth)
                .orElse(null); // e.g., STUDENT

        if (role == null) {
            return new ResponseEntity<>(Map.of("message", "No role assigned"), HttpStatus.BAD_REQUEST);
        }
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
    @GetMapping(path = "/health", produces = "application/json")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "up"));
    }

}
