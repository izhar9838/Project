package sm.central.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sm.central.model.classes.Class;
import sm.central.model.hallofFame.HallOfFameEntity;
import sm.central.model.staff.Teacher;
import sm.central.model.student.Fees_Details;
import sm.central.model.student.Student;
import sm.central.model.timetable.Timetable;
import sm.central.service.admin.IAdminService;

import sm.central.service.user.IUserService;



@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class AdminController {
	@Autowired
	private IAdminService adminService;
	
	@PostMapping(value = "/enrollStudent",consumes = "application/json")
	public ResponseEntity<?> enrollStudent(@RequestBody Student student){
//		System.out.println(student);
		System.out.println("enter enroll restconroller");
		
			
		String	msg = adminService.enrollStudent(student);
			
		System.out.println("service has been called from rest controller");
		
		return new ResponseEntity<String>(msg,HttpStatus.CREATED);
	}
	@PostMapping(path = "/feesSubmission",consumes = "application/json")
	public ResponseEntity<?> feesSubmission(@RequestBody Fees_Details fees_Details){
		Fees_Details feeEntity=null;
		try {
			 feeEntity = adminService.feesSubmission(fees_Details);
		} catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException("Some Error Occured");
		}
		return new ResponseEntity<>(feeEntity,HttpStatus.CREATED);
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
    
}
