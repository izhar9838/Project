package sm.central.service.teacher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sm.central.dto.student.AssignmentDTO;
import sm.central.dto.student.AttendenceStudent;
import sm.central.dto.student.CheckAssignmentDto;
import sm.central.dto.teacher.AttendanceRequest;
import sm.central.dto.teacher.MarkAssignment;
import sm.central.dto.teacher.MarksEntryDTO;
import sm.central.exception.custom.MarksAlreadyExistException;
import sm.central.exception.custom.StudentNotFoundException;
import sm.central.model.classes.Class;
import sm.central.model.content.Assignment;
import sm.central.model.content.BlogPost;
import sm.central.model.content.Notes;
import sm.central.model.content.SubmitAssignment;
import sm.central.model.result.Examination;
import sm.central.model.result.StudentResult;
import sm.central.model.result.Subject;
import sm.central.model.student.Attendance;
import sm.central.model.student.Student;
import sm.central.repository.classes.ClassRepository;
import sm.central.repository.content.IAssigment;
import sm.central.repository.content.IBlogRepository;
import sm.central.repository.content.INotesRepository;
import sm.central.repository.content.ISubmitAssignment;
import sm.central.repository.result.IExaminationRepo;
import sm.central.repository.result.IStudentResultRepo;
import sm.central.repository.result.ISubjectRepo;
import sm.central.repository.staff.ITeacherRepo;
import sm.central.repository.student.IAcademicInfo;
import sm.central.repository.student.IAttendanceRepo;
import sm.central.repository.student.IStudentRepo;

@Service
public class TeacherServiceImpl implements ITeacherService {
	@Autowired
	private ClassRepository classRepo;
	@Autowired
	private IBlogRepository blogRepo;
	@Autowired
	private INotesRepository noteRepo;
	@Autowired
	private ISubmitAssignment submitAssignment;
	@Autowired
	private IAssigment assigmentRepo;
	@Autowired
	private ITeacherRepo teacherRepo;
	@Autowired
	private IStudentRepo stuRepo;
	@Autowired
	private IAcademicInfo iAcademicInfo;
	@Autowired
	private ISubjectRepo subjectRepo;
	@Autowired
	private IExaminationRepo examinationRepo;
	@Autowired
	private IStudentResultRepo studentResultRepo;
	@Autowired
	private IAttendanceRepo iAttendanceRepo;
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
			throw new RuntimeException("Some Error occur");
		}
	}
	@Override
	public String uploadNotes(Notes notes) {
		Notes savenote = noteRepo.save(notes);
		if (savenote!=null) {
			return "Notes Upload Successfully";
		}
		else {
			throw new RuntimeException("Some Error occur");
		}
	}

	@Override
	public String assignAssignment(String username,AssignmentDTO assignmentDto) {
		String teacherId=teacherRepo.findByUsernameWithDetails(username).get().getTeacherId();
		Assignment assignment=new Assignment();
		assignment.setTeacherId(teacherId);
		assignment.setAssignment(assignmentDto.getAssignment());
		assignment.setTitle(assignmentDto.getTitle());
		assignment.setMimeType(assignmentDto.getMimeType());
		assignment.setClassNo(assignmentDto.getClassNo());
		Assignment assignment1=assigmentRepo.save(assignment);
		if (assignment!=null){
			return "Assignment saved Successfully";
		}
		throw new RuntimeException("some error occur");

	}

	@Override
	public List<CheckAssignmentDto> getForChecking(String username) {
		List<SubmitAssignment> submittedAssigment=submitAssignment.findAll();
		String teacherId=teacherRepo.findByUsernameWithDetails(username).get().getTeacherId();
		List<CheckAssignmentDto> getForChecking=submittedAssigment.stream().map(submitAssignment1 -> {
			boolean isThisTeacher=assigmentRepo.existsByTeacherIdAndId(teacherId,submitAssignment1.getAssignmentId());
			String studentName=null;
			Optional<Student> studentOpt = stuRepo.findById(submitAssignment1.getStudentId());
			if (studentOpt.isPresent()) {
				Student student = studentOpt.get();
				 studentName = student.getFirstName() + " " + student.getLastName();
			} else {
				// Handle case where student is not found
				throw new StudentNotFoundException("Student not found");
			}
			String section=stuRepo.findById(submitAssignment1.getStudentId()).get().getAcademic_info().getSection();

			return new CheckAssignmentDto(
					submitAssignment1.getSubmissionId(),
					submitAssignment1.getStudentId(),
					studentName,
					submitAssignment1.getAssignment().getTitle(),
					submitAssignment1.isChecked(),
					section,
					submitAssignment1.getAssignmentId(),
					submitAssignment1.getClassNo(),
					submitAssignment1.getSubmitAssignment(),
					submitAssignment1.getMimeType(),
					isThisTeacher
			);
		}).collect(Collectors.toList());
        return getForChecking.stream().filter(submission -> !submission.isChecked() && submission.isThisTeacher()).collect(Collectors.toList());
	}

	@Override
	public String markAssignment(MarkAssignment markAssignment, Long submissionId) {
		try {
			SubmitAssignment submitAssign=submitAssignment.findById(submissionId).get();
			submitAssign.setMarks(markAssignment.getMarks());
			System.out.println(markAssignment.getMarks());
			submitAssign.setChecked(true);
			submitAssignment.save(submitAssign);
			return "Mark Assign Successfully";
		}catch (Exception e){
			throw new RuntimeException("Some  error occur");
		}
	}

	@Override
	public List<AttendenceStudent> getStudentForAttencdence(String classId, String section) {
			List<Student>  liststudents=iAcademicInfo.findStudentsByStandardAndSection(classId,section);
			if (liststudents.isEmpty()){
				throw new RuntimeException("No Student Found");
			}
        return liststudents.stream().map(student -> {

            return new AttendenceStudent(
					student.getStudentId(),
					student.getFirstName(),
                    student.getLastName(),
                    student.getAcademic_info().getRollNo());
        }).collect(Collectors.toList());
	}

	@Override
	public String markAttendance(AttendanceRequest request) {
		LocalDate today = LocalDate.now();
		for (AttendanceRequest.AttendanceEntry entry : request.getAttendance()) {
			Student student = stuRepo.findById(entry.getStudentId())
					.orElseThrow(() -> new IllegalArgumentException("Student not found: " + entry.getStudentId()));

			Attendance attendance = new Attendance();
			attendance.setDate(today);
			attendance.setPresent(entry.isPresent());
			attendance.setStudent(student);

			iAttendanceRepo.save(attendance);
		}

		return "Attendance Mark Successfully";
	}
	@Transactional
	public void uploadMarks(MarksEntryDTO marksEntryDTO) {
		// Validate subject
		Optional<Subject> subjectOpt = subjectRepo.findBySubject(marksEntryDTO.getSubjectName());
		if (!subjectOpt.isPresent()) {
			throw new IllegalArgumentException("Invalid subject name: " + marksEntryDTO.getSubjectName());
		}
		Subject subject = subjectOpt.get();

		// Validate examination
		Optional<Examination> examinationOpt = examinationRepo.findByType(marksEntryDTO.getExamTerm());
		if (!examinationOpt.isPresent()) {
			throw new IllegalArgumentException("Invalid exam term: " + marksEntryDTO.getExamTerm());
		}
		Examination examination = examinationOpt.get();

		// Validate marks
		for (MarksEntryDTO.StudentMarkDTO markDTO : marksEntryDTO.getMarks()) {
			if (markDTO.getMarks() == null || markDTO.getMarks() < 0 || markDTO.getMarks() > 100) {
				throw new IllegalArgumentException("Invalid marks for student " + markDTO.getStudentId() + ": must be between 0 and 100");
			}
		}

		// Process marks
		List<StudentResult> results = new ArrayList<>();
		for (MarksEntryDTO.StudentMarkDTO markDTO : marksEntryDTO.getMarks()) {
			Optional<Student> studentOpt = stuRepo.findById(markDTO.getStudentId());
			if (!studentOpt.isPresent()) {
				throw new IllegalArgumentException("Invalid student ID: " + markDTO.getStudentId());
			}
			Student student = studentOpt.get();

			// Check if marks already exist for this student, subject, and examination
			StudentResult existingResult = studentResultRepo.findByStudentAndSubjectAndExamination(student, subject, examination);
			if (existingResult != null) {
				throw new MarksAlreadyExistException("Marks already exist for student " + student.getFirstName()+" "+student.getLastName() + " for subject " + subject.getSubject() + " and exam term " + examination.getType());
			}

			StudentResult result = new StudentResult();
			result.setStudent(student);
			result.setSubject(subject);
			result.setExamination(examination);
			result.setMarks((double) markDTO.getMarks());
			result.setGrade(calculateGrade(markDTO.getMarks()));
			results.add(result);
		}
		studentResultRepo.saveAll(results);
	}

	@Override
	public List<Subject> findAllSubject() {

		return subjectRepo.findAll();
	}

	@Override
	public List<Examination> findAllExamination() {
		return examinationRepo.findAll();
	}

	public String calculateGrade(Integer marks){
		if (marks < 0 || marks > 50) {
			throw new RuntimeException("Marks not grater than 50");
		}

		if (marks < 17) {
			return "F (Fail)";
		} else if (marks <= 24) {
			return "D";
		} else if (marks <= 32) {
			return "C";
		} else if (marks <= 40) {
			return "B";
		} else {
			return "A";
		}
	}
}
