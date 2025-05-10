package sm.central.service.teacher;

import java.util.List;

import sm.central.model.classes.Class;
import sm.central.model.content.Assignment;
import sm.central.model.content.BlogPost;
import sm.central.model.content.Notes;

public interface ITeacherService {
	public List<Class> getClassese();
	public String postBlog(BlogPost blog);
	public String uploadNotes(Notes notes);
	public String assignAssignment(Assignment assignment);

}
