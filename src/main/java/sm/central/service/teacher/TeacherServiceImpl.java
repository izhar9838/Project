package sm.central.service.teacher;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sm.central.model.classes.Class;
import sm.central.model.content.Assignment;
import sm.central.model.content.BlogPost;
import sm.central.model.content.Notes;
import sm.central.repository.classes.ClassRepository;
import sm.central.repository.content.IAssigment;
import sm.central.repository.content.IBlogRepository;
import sm.central.repository.content.INotesRepository;
@Service
public class TeacherServiceImpl implements ITeacherService {
	@Autowired
	private ClassRepository classRepo;
	@Autowired
	private IBlogRepository blogRepo;
	@Autowired
	private INotesRepository noteRepo;
	@Autowired
	private IAssigment assigmentRepo;
	@Override
	public List<Class> getClassese() {
		List<Class> all = classRepo.findAll();

		return all;
	}
	@Override
	public String postBlog(BlogPost blog) {
		// TODO Auto-generated method stub
		BlogPost blogPost = blogRepo.save(blog);
		if (blogPost!=null) {
			return "Post Publish Sucessfully";
		}
		else {
			throw new RuntimeException("Some Erro occured");
		}
	}
	@Override
	public String uploadNotes(Notes notes) {
		Notes savenote = noteRepo.save(notes);
		if (savenote!=null) {
			return "Notes Upload Successfully";
		}
		else {
			throw new RuntimeException("Some Eror occured");
		}
	}

	@Override
	public String assignAssignment(Assignment assignment) {
		Assignment assignment1=assigmentRepo.save(assignment);
		if (assignment!=null){
			return "Assignment saved Successfully";
		}
		throw new RuntimeException("some error occured");
	}
}
