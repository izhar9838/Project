package sm.central.repp.timetable;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.timetable.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
	public Timetable findByPeriodAndClassName(String period,String className);
}