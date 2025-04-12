package sm.central.repository.student;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.student.Contact_Details;


public interface IContactDetailsRepo extends JpaRepository<Contact_Details, Integer> {

}
