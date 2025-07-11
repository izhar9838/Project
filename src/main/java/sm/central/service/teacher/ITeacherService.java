package sm.central.service.teacher;

import java.util.List;

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

public interface ITeacherService {
	public List<Class> getClassese();
	public String postBlog(BlogPost blog);
	public String uploadNotes(Notes notes);
	public String assignAssignment(String username,AssignmentDTO assignmentDto);
	public List<CheckAssignmentDto> getForChecking(String username);

	public String markAssignment(MarkAssignment markAssignment, Long submissionId);

	List<AttendenceStudent> getStudentForAttencdence(String classId, String section);

	String markAttendance(AttendanceRequest request);

    void uploadMarks(MarksEntryDTO marksEntryDTO);

	List<Subject> findAllSubject();

	List<Examination> findAllExamination();
}
