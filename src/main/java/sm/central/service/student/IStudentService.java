package sm.central.service.student;
import java.util.List;

import sm.central.dto.AssignmentDTO;
import sm.central.dto.SubmitAssignDto;
import sm.central.model.content.Assignment;
import sm.central.model.content.Notes;
import sm.central.model.content.Timetable;
import sm.central.model.student.Student;

public interface IStudentService {
	public Student getStudentByUsername(String username);
	public List<Notes> getNotesByClassLevel(String username);
	public List<Timetable> getTimeTableOfSpecificClass(String username);
	public List<AssignmentDTO> getAssignments(String username);
	public String submitAssignment(String username, SubmitAssignDto submitDto);
}
