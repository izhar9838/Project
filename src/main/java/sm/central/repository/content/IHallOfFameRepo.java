package sm.central.repository.content;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.content.HallOfFameEntity;

public interface IHallOfFameRepo extends JpaRepository<HallOfFameEntity, Integer> {

}
