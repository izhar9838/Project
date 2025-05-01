package sm.central.service.student;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sm.central.model.content.Notes;
import sm.central.model.content.Timetable;
import sm.central.model.student.Student;
import sm.central.repository.content.INotesRepository;
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

}
