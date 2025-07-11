package sm.central.service.student;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sm.central.dto.student.AssignmentDTO;
import sm.central.dto.student.SubmitAssignDto;
import sm.central.exception.custom.AssignmentNotFoundException;
import sm.central.exception.custom.StudentNotFoundException;
import sm.central.customfilter.ExcludeFeesDetailsFilter;
import sm.central.model.content.Assignment;
import sm.central.model.content.Notes;
import sm.central.model.content.SubmitAssignment;
import sm.central.model.content.Timetable;
import sm.central.model.student.Fees_Details;
import sm.central.model.student.Student;
import sm.central.repository.content.IAssigment;
import sm.central.repository.content.INotesRepository;
import sm.central.repository.content.ISubmitAssignment;
import sm.central.repository.content.TimetableRepository;
import sm.central.repository.result.IStudentResultRepo;
import sm.central.repository.student.IFeesRepo;
import sm.central.repository.student.IStudentRepo;
import sm.central.repository.student.IStudentUserPass;

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
	@Autowired
	private IStudentUserPass studentUserPass;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private IStudentResultRepo studentResultRepo;

	@Autowired
	private IFeesRepo feesRepo;
	@Override
	public Map<String,Object> getStudentByUsername(String username) {
		Optional<Student> existStudent = stuRepo.findByUsernameWithDetails(username);
		if (existStudent.isPresent()) {
            try {
				Student student=existStudent.get();
				Map<String ,Object> response=Map.of("student",student);
				String serialized=objectMapper.writer(ExcludeFeesDetailsFilter.excludeFeesDetails()).writeValueAsString(response);
				return objectMapper.readValue(serialized,Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
		else {
			throw new StudentNotFoundException("Student Not Found with username"+username);
		}
	}

	@Override
	public List<Notes> getNotesByClassLevel(String username) {
		Student existStudent = studentUserPass.findByUsername(username).get().getStudent();
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
		Student existStudent = studentUserPass.findByUsername(username).get().getStudent();
		if (existStudent.getAcademic_info() == null || existStudent.getAcademic_info().getStandard() == null) {
	        throw new IllegalStateException("Student's class level is not defined for username: " + username);
	    }
		String className=existStudent.getAcademic_info().getStandard();
		List<Timetable> listTimeTable = timeRepo.findByClassName(className);
		
		return listTimeTable;
	}

	@Override
	public List<AssignmentDTO> getAssignments(String username) {
		Student existStudent=studentUserPass.findByUsername(username).get().getStudent();
		if (existStudent.getAcademic_info() == null || existStudent.getAcademic_info().getStandard() == null) {
			throw new IllegalStateException("Student's class level is not defined for username: " + username);
		}
		String classLevel=existStudent.getAcademic_info().getStandard();
		List<Assignment> assignments= assignmentRepo.findByClassNo(classLevel);
		List<AssignmentDTO> response = assignments.stream().map(assignment -> {
			boolean submitted = submitAssignmentRepo.existsByAssignmentIdAndStudentId(
					assignment.getId(), existStudent.getStudentId());
			Integer marks = null;
			if (submitted) {
				SubmitAssignment submission = submitAssignmentRepo.findByAssignmentIdAndStudentId(
						assignment.getId(), existStudent.getStudentId());
				if (submission != null && submission.isChecked()) {
					marks = submission.getMarks();
				}
			}

			return new AssignmentDTO(
					assignment.getId(),
					assignment.getTitle(),
					assignment.getClassNo(),
					marks,
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
			throw new StudentNotFoundException("Student not found");
		}

		Assignment assignment = assignmentRepo.findById(submitDto.getAssignmentId())
				.orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));

		// Validate classNo
		String classNo = student.get().getAcademic_info().getStandard();
		if (!assignment.getClassNo().equals(classNo)) {
			throw new AssignmentNotFoundException("Assignment does not belong to your class");
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

	@Override
	public Map<String, Object> fetchFeesDetailsPerPage(String studentId, int page) {
		Pageable pageable=PageRequest.of(page,5);
	Page<Fees_Details> feesDetails =feesRepo.findByStudentStudentId(studentId,pageable);
		if (feesDetails.isEmpty()) {
			return Map.of("message", "No fees details found for student ID: " + studentId, "currentPage", 0, "totalPage", 0, "totalElement", 0);
		}
		return Map.of("feesDetails", feesDetails.getContent(), "currentPage", feesDetails.getNumber(), "totalPage", feesDetails.getTotalPages(), "totalElement", feesDetails.getTotalElements());
	}

	@Override
	public List<Object> getResults(String studentId) {
		Optional<Student> optional=stuRepo.findById(studentId);
		if (!optional.isPresent()){
			throw new StudentNotFoundException("Student not found");
		}
		return studentResultRepo.findResultsByStudentId(studentId);

	}

}
