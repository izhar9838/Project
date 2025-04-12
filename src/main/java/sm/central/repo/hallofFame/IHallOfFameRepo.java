package sm.central.repo.hallofFame;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.hallofFame.HallOfFameEntity;

public interface IHallOfFameRepo extends JpaRepository<HallOfFameEntity, Integer> {

}
