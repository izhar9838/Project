package sm.central.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sm.central.model.hallofFame.HallOfFameEntity;
import sm.central.model.timetable.Timetable;
import sm.central.repo.hallofFame.IHallOfFameRepo;
import sm.central.repp.timetable.TimetableRepository;

import java.util.List;

@Service
public class UserServiceImpl  implements IUserService{
    @Autowired
    private TimetableRepository repository;
    @Autowired
    private IHallOfFameRepo hallRepo;


    public List<Timetable> getAllTimetables() {
        return repository.findAll();
    }


	@Override
	public List<HallOfFameEntity> getAllHallOfFame() {
		// TODO Auto-generated method stub
		return hallRepo.findAll();
	}

	

    
}