package sm.central.service.student;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import sm.central.dto.student.AssignmentDTO;
import sm.central.dto.student.AttendanceSummary;
import sm.central.dto.student.SubmitAssignDto;
import sm.central.model.content.Notes;
import sm.central.model.content.Timetable;

public interface IStudentService {
	public Map<String ,Object> getStudentByUsername(String username);
	public List<Notes> getNotesByClassLevel(String username);
	public List<Timetable> getTimeTableOfSpecificClass(String username);
	public List<AssignmentDTO> getAssignments(String username);
	public String submitAssignment(String username, SubmitAssignDto submitDto);
	AttendanceSummary getAttendanceSummary(String studentId);
	Map<String, Object> fetchFeesDetailsPerPage(String studentId, int page);

    List<Object> getResults(String studentId);
}
