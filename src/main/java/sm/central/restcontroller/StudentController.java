package sm.central.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import sm.central.dto.AssignmentDTO;
import sm.central.dto.SubmitAssignDto;
import sm.central.model.content.Assignment;
import sm.central.model.content.Notes;
import sm.central.model.content.Timetable;
import sm.central.model.student.Student;
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
		Student existStudent = stuService.getStudentByUsername(username);
		
		return new ResponseEntity<>(existStudent,HttpStatus.OK);
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
}
