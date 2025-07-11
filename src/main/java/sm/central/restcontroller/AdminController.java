package sm.central.restcontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sm.central.dto.admin.CheckPaymentDto;
import sm.central.dto.admin.FeeRecieptDto;
import sm.central.dto.admin.FeesDetailsDTO;
import sm.central.model.classes.Class;

import sm.central.model.content.Announcement;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.model.staff.Teacher;
import sm.central.model.student.Student;
import sm.central.security.model.UserEntity;
import sm.central.service.admin.IAdminService;





@RestController
@RequestMapping("/api/admin")
public class AdminController {
	@Autowired
	private IAdminService adminService;
	
	@PostMapping(value = "/enrollStudent",consumes = "application/json")
	public ResponseEntity<?> enrollStudent(@RequestBody Student student){
		String	msg = adminService.enrollStudent(student);
			

		return new ResponseEntity<String>(msg,HttpStatus.CREATED);
	}
	@PostMapping(path = "/feesSubmission",consumes = "application/json")
	public ResponseEntity<?> feesSubmission(@RequestBody FeesDetailsDTO feeDto){
		if (feeDto.getPayment_mode().equals("UPI")){
            try {
               String qrCode= adminService.upiPayment(feeDto.getStudentId(),feeDto.getAmount());

				return new ResponseEntity<>(Map.of("qrCode",qrCode),HttpStatus.OK);
            } catch (WriterException e) {
                throw new RuntimeException(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
			FeeRecieptDto feeRecieptDto = adminService.feesSubmission(feeDto);
			return new ResponseEntity<>(feeRecieptDto,HttpStatus.OK);
		}
    }
	@PostMapping(path = "/confirmPayment",produces = "application/json")
	public ResponseEntity<?> confirmPayment(@RequestParam String paymentId, @RequestBody FeesDetailsDTO feesDetailsDTO){
		FeeRecieptDto feeRecieptDto=adminService.confirmPayment(paymentId,feesDetailsDTO);
		return null;
	}
	@PostMapping(path = "/enrollTeacher",consumes = "application/json")
	public ResponseEntity<?> enrollTeacher(@RequestBody Teacher teacher){
		String msg = adminService.enrollStaff(teacher);
		
		return new ResponseEntity<>(msg,HttpStatus.CREATED);
	}
	@GetMapping(path = "/classes",produces = "application/json")
	public ResponseEntity<?> getClasses(){
		List<Class> all = adminService.getClassese();
		System.out.println("/n All classes :"+all);
		
		return new ResponseEntity<>(all,HttpStatus.OK);
	}
	@PostMapping(path="createTimeTable",consumes = "application/json")
    public Timetable createTimetable(@RequestBody Timetable timetable) {
        return adminService.saveTimetable(timetable);
    }

    @DeleteMapping("/{id}")
    public void deleteTimetable(@PathVariable Long id) {
        adminService.deleteTimetable(id);
    }
    @PostMapping(path="/add-hallOfFame",consumes = "application/json")
    public ResponseEntity<?> saveHallofFame(@RequestBody HallOfFameEntity hallofFame){
    	String ofFame = adminService.saveHallOfFame(hallofFame);
    	
    	return new ResponseEntity<String>(ofFame,HttpStatus.OK);
    }
    @PostMapping(path = "/add-admin",consumes = "application/json")
    public ResponseEntity<?> addAdmin(@RequestBody UserEntity useEntity){
    	String msg = adminService.addAdmin(useEntity);
    	return new ResponseEntity<>(msg,HttpStatus.OK);
    }
    @PostMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        boolean exists = adminService.checkUsernameExists(username);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "Username already exists" : "Username is available");

        return ResponseEntity.ok(response);
    }
    @PostMapping(path = "/createAnnoucement",consumes = "application/json")
    public ResponseEntity<?> createAnnoucement(@RequestBody Announcement annoucement){
    	Announcement announcement1 = adminService.createAnnouncement(annoucement);
    	String msg="Announcement Create for"+announcement1.getTitle();
    	return new ResponseEntity<>(msg,HttpStatus.CREATED);
    }
	@GetMapping(path = "/check-payment",produces = "application/json")
	public ResponseEntity<?> checkPayment(@RequestParam String paymentId){
		CheckPaymentDto checkPaymentDto =adminService.checkPayment(paymentId);
		return new ResponseEntity<>(checkPaymentDto,HttpStatus.OK);
	}
	@GetMapping(path = "/searchUser")
	public ResponseEntity<?> searchUser(@RequestParam String query){
        Map<String,Object> userDetails= null;
        try {
            userDetails = adminService.findUserByQuery(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(userDetails,HttpStatus.OK);

	}
	@GetMapping(path = "/getFeesDetails",produces = "application/json")
    public ResponseEntity<?> getFeesDetailsPerPage(@RequestParam String studentId,@RequestParam(defaultValue = "0") int page){
			Map<String,Object> fetchFeesDetailsPerPage=adminService.fetchFeesDetailsPerPage(studentId,page);
		return new ResponseEntity<>(fetchFeesDetailsPerPage,HttpStatus.OK);
	}
	@PostMapping(path = "/cancelPayment")
	public ResponseEntity<?> cancelPayment(@RequestParam String paymentId){
		String msg=adminService.cancelPayment(paymentId);
		return ResponseEntity.ok(msg);
	}
	public ResponseEntity<?> addSubject(@RequestParam String subject){
        try {
            adminService.addSubject(subject);
            return ResponseEntity.ok().body("Add Success");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
