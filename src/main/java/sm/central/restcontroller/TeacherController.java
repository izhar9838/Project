package sm.central.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import sm.central.dto.student.AssignmentDTO;
import sm.central.dto.student.AttendenceStudent;
import sm.central.dto.student.CheckAssignmentDto;
import sm.central.dto.teacher.AttendanceRequest;
import sm.central.dto.teacher.MarkAssignment;
import sm.central.dto.teacher.MarksEntryDTO;
import sm.central.model.classes.Class;
import sm.central.model.content.BlogPost;
import sm.central.model.content.Notes;
import sm.central.model.result.Examination;
import sm.central.model.result.Subject;
import sm.central.service.teacher.ITeacherService;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {
	@Autowired
	private ITeacherService teacherService;
	
	@GetMapping(path = "/classes",produces = "application/json")
	public ResponseEntity<?> getClasses(){
		List<Class> all = teacherService.getClassese();
		System.out.println("/n All classes :"+all);
		
		return new ResponseEntity<>(all,HttpStatus.OK);
	}
	@PostMapping(path = "/postBlog" ,consumes = "application/json")
	public ResponseEntity<?> postBlog(@RequestBody BlogPost blog){
		String postBlog = teacherService.postBlog(blog);
		System.out.println(postBlog);
		return new ResponseEntity<>(postBlog,HttpStatus.OK);
	}
	@PostMapping(path="/upload-notes",consumes = "application/json")
	public ResponseEntity<?> uploadContent(@RequestBody Notes notes){
		String msg = teacherService.uploadNotes(notes);
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
	@PostMapping(path = "assignAssignment" ,consumes = "application/json")
	public ResponseEntity<?> assignAssignment(@RequestBody AssignmentDTO assignmentDto){
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		String msg=teacherService.assignAssignment(username,assignmentDto);
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
	@GetMapping(path="/getForChecking",produces = "application/json")
	public ResponseEntity<?> checkAssignment(){
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		List<CheckAssignmentDto> checkAssignmentDtos=teacherService.getForChecking(username);
		return new ResponseEntity<>(checkAssignmentDtos,HttpStatus.OK);
	}
	@PatchMapping(path="/markAssignment/{submissionId}",consumes = "application/json")
	public ResponseEntity<?> submitCheckedAssignment(@RequestBody MarkAssignment markAssignment,@PathVariable Long submissionId){
		String msg=teacherService.markAssignment(markAssignment,submissionId);
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
	@GetMapping(path = "/getStudents",produces = "application/json")
	public ResponseEntity<?> getStudentForAttendence(@RequestParam String classId,@RequestParam String section){
		List<AttendenceStudent>listStudents=teacherService.getStudentForAttencdence(classId,section);
		return new ResponseEntity<>(listStudents,HttpStatus.OK);
	}
	@PostMapping(path = "/markAttendance")
	public ResponseEntity<?> markAttendance(@RequestBody AttendanceRequest request){
		String msg=teacherService.markAttendance(request);
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
	@GetMapping(path = "/subjects",produces = "application/json")
	public ResponseEntity<?> getSubjects(){

		List<Subject> subjectList=teacherService.findAllSubject();
		return new ResponseEntity<>(subjectList,HttpStatus.OK);
	}
	@GetMapping(path = "/examinations",produces = "application/json")
	public ResponseEntity<?> getExamination(){
		List<Examination> examinationList=teacherService.findAllExamination();
		return new ResponseEntity<>(examinationList,HttpStatus.OK);
	}

	@PostMapping("/upload-marks")
	public ResponseEntity<Void> uploadMarks(@RequestBody MarksEntryDTO marksEntryDTO) {
		try {
			System.out.println(marksEntryDTO);
			teacherService.uploadMarks(marksEntryDTO);
			return ResponseEntity.ok().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

}
