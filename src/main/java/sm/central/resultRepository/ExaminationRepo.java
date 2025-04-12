package sm.central.resultRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.result.Examination;

public interface ExaminationRepo extends JpaRepository<Examination, Long> {

}
