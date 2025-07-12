package sm.central.restcontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import sm.central.dto.student.AssignmentDTO;
import sm.central.dto.student.AttendanceSummary;
import sm.central.dto.student.SubmitAssignDto;
import sm.central.model.content.Notes;
import sm.central.model.content.Timetable;
import sm.central.repository.content.INotesRepository;
import sm.central.service.student.IStudentService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
	@Autowired
	IStudentService stuService;
	@Autowired
	INotesRepository noteRepo;
	@GetMapping(path = "/getprofile",produces = "application/json")
	public ResponseEntity<?> getStudentByUsername(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		Map<String,Object> resMap = stuService.getStudentByUsername(username);

		return new ResponseEntity<>(resMap,HttpStatus.OK);
	}
	@GetMapping(path = "/getNotes",produces = "application/json")
	public ResponseEntity<?> getNotes(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Notes> notes = stuService.getNotesByClassLevel(username);
		return new ResponseEntity<>(notes,HttpStatus.OK);
	}
	@GetMapping(path = "/schedule",produces = "application/json")
	public ResponseEntity<?> getTimeTableOfSpecificClass(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Timetable> listTable = stuService.getTimeTableOfSpecificClass(username);
		
		return new ResponseEntity<>(listTable,HttpStatus.OK);
	}
	@GetMapping(path = "getAssignments" ,produces = "application/json")
	public ResponseEntity<?> getAssignment(){
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		List<AssignmentDTO> response=stuService.getAssignments(username);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	@PostMapping(path = "/submitAssignment",consumes = "application/json")
	public ResponseEntity<?> submitAssignment(@RequestBody SubmitAssignDto submitAssignDto){
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		String msg=stuService.submitAssignment(username,submitAssignDto);
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
	@GetMapping(path = "/getFeesDetails",produces = "application/json")
	public ResponseEntity<?> getFeesDetailsPerPage(@RequestParam String studentId,@RequestParam(defaultValue = "0") int page){
		Map<String,Object> fetchFeesDetailsPerPage=stuService.fetchFeesDetailsPerPage(studentId,page);
		return new ResponseEntity<>(fetchFeesDetailsPerPage,HttpStatus.OK);
	}
	@GetMapping("/{studentId}/results")
	public ResponseEntity<?> getResults(@PathVariable String studentId){
		List<Object> results=stuService.getResults(studentId);
		System.out.println(results.toString());
		return new ResponseEntity<>(results,HttpStatus.OK);
	}
	@GetMapping(path = "/getAttendance",produces = "application/json")
	public ResponseEntity<?> getAttendanceSummary(@RequestParam String studentId){
		AttendanceSummary attendanceSummary=stuService.getAttendanceSummary(studentId);
		System.out.println(attendanceSummary);
		return new ResponseEntity<>(attendanceSummary,HttpStatus.OK);
	}
}
