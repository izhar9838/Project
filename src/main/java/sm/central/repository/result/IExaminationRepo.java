package sm.central.repository.result;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.central.model.result.Examination;

import java.util.Optional;

public interface IExaminationRepo extends JpaRepository<Examination,Integer> {
    Optional<Examination> findByType(String type);
}
