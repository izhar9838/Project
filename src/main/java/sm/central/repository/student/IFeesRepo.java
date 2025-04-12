package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.student.Fees_Details;

public interface IFeesRepo extends JpaRepository<Fees_Details, Integer> {

}
