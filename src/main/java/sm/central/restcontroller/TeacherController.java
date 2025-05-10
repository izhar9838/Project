package sm.central.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sm.central.model.classes.Class;
import sm.central.model.content.Assignment;
import sm.central.model.content.BlogPost;
import sm.central.model.content.Notes;
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
	public ResponseEntity<?> assignAssignment(@RequestBody Assignment assignment){
		String msg=teacherService.assignAssignment(assignment);
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
}
