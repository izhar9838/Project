package sm.central.service.user;

import java.util.List;

import sm.central.model.hallofFame.HallOfFameEntity;
import sm.central.model.timetable.Timetable;

public interface IUserService {
	public List<Timetable> getAllTimetables();
	public List<HallOfFameEntity> getAllHallOfFame();
	
}
