package sm.central.service.student;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sm.central.dto.AssignmentDTO;
import sm.central.dto.SubmitAssignDto;
import sm.central.model.content.Assignment;
import sm.central.model.content.Notes;
import sm.central.model.content.SubmitAssignment;
import sm.central.model.content.Timetable;
import sm.central.model.student.Student;
import sm.central.repository.content.IAssigment;
import sm.central.repository.content.INotesRepository;
import sm.central.repository.content.ISubmitAssignment;
import sm.central.repository.content.TimetableRepository;
import sm.central.repository.student.IStudentRepo;

@Service
public class StudentServiceImpl implements IStudentService {
	@Autowired
	private IStudentRepo stuRepo;
	@Autowired
	INotesRepository noteRepo;
	@Autowired
	TimetableRepository timeRepo;
	@Autowired
	private IAssigment assignmentRepo;
	@Autowired
	private ISubmitAssignment submitAssignmentRepo;

	@Override
	public Student getStudentByUsername(String username) {
		Optional<Student> existStudent = stuRepo.findByUsernameWithDetails(username);
		if (existStudent.isPresent()) {
			return existStudent.get();
		}
		else {
			throw new RuntimeException("Student is not fetched");
		}
	}

	@Override
	public List<Notes> getNotesByClassLevel(String username) {
		Student existStudent = getStudentByUsername(username);
		if (existStudent.getAcademic_info() == null || existStudent.getAcademic_info().getStandard() == null) {
	        throw new IllegalStateException("Student's class level is not defined for username: " + username);
	    }
		String classLevel=existStudent.getAcademic_info().getStandard();
		List<Notes> existNotes = noteRepo.findByClassLevel(classLevel);
		return existNotes;
	}

	@Override
	public List<Timetable> getTimeTableOfSpecificClass(String username) {
		// TODO Auto-generated method stub
		Student existStudent = getStudentByUsername(username);
		if (existStudent.getAcademic_info() == null || existStudent.getAcademic_info().getStandard() == null) {
	        throw new IllegalStateException("Student's class level is not defined for username: " + username);
	    }
		String className=existStudent.getAcademic_info().getStandard();
		List<Timetable> listTimeTable = timeRepo.findByClassName(className);
		
		return listTimeTable;
	}

	@Override
	public List<AssignmentDTO> getAssignments(String username) {
		Student existStudent=getStudentByUsername(username);
		if (existStudent.getAcademic_info() == null || existStudent.getAcademic_info().getStandard() == null) {
			throw new IllegalStateException("Student's class level is not defined for username: " + username);
		}
		String classLevel=existStudent.getAcademic_info().getStandard();
		List<Assignment> assignments= assignmentRepo.findByClassNo(classLevel);
		List<AssignmentDTO> response = assignments.stream().map(assignment -> {
			boolean submitted = submitAssignmentRepo.existsByAssignmentIdAndStudentId(
					assignment.getId(), existStudent.getStudentId());
			return new AssignmentDTO(
					assignment.getId(),
					assignment.getTitle(),
					assignment.getClassNo(),
					assignment.getMimeType(),
					assignment.getAssignment(),
					submitted
			);
		}).collect(Collectors.toList());
		return response;
	}

	@Override
	public String submitAssignment(String username, SubmitAssignDto submitDto) {
		Optional<Student> student=stuRepo.findByUsernameWithDetails(username);
		if (student.isEmpty()) {
			throw new RuntimeException("Student not found");
		}

		Assignment assignment = assignmentRepo.findById(submitDto.getAssignmentId())
				.orElseThrow(() -> new RuntimeException("Assignment not found"));

		// Validate classNo
		String classNo = student.get().getAcademic_info().getStandard();
		if (!assignment.getClassNo().equals(classNo)) {
			throw new RuntimeException("Assignment does not belong to your class");
		}

		// Check for existing submission
		if (submitAssignmentRepo.existsByAssignmentIdAndStudentId(
				submitDto.getAssignmentId(), student.get().getStudentId())) {
			throw new RuntimeException("Assignment already submitted");
		}

		SubmitAssignment submission = new SubmitAssignment();
		submission.setStudentId(student.get().getStudentId());
		submission.setAssignmentId(submitDto.getAssignmentId());
		submission.setClassNo(classNo);
		submission.setSubmitAssignment(submitDto.getSubmitAssignment());
		submission.setMimeType(submitDto.getMimeType());

		submitAssignmentRepo.save(submission);
		return "Assignment Submitted Successfully";
	}

}
