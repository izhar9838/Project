package sm.central.repository.content;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.content.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
	public Timetable findByPeriodAndClassName(String period,String className);
	public List<Timetable> findByClassName(String className);
}