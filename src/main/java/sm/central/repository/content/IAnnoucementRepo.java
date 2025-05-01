package sm.central.repository.content;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.content.Announcement;

public interface IAnnoucementRepo extends JpaRepository<Announcement, Long> {

}
