package sm.central.service.student;
import java.util.List;

import sm.central.model.content.Notes;
import sm.central.model.content.Timetable;
import sm.central.model.student.Student;

public interface IStudentService {
	public Student getStudentByUsername(String username);
	public List<Notes> getNotesByClassLevel(String username);
	public List<Timetable> getTimeTableOfSpecificClass(String username);
}
