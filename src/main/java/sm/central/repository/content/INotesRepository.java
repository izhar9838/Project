package sm.central.repository.content;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sm.central.model.content.Notes;

public interface INotesRepository extends JpaRepository<Notes, Long> {
	public List<Notes> findByClassLevel(String classLevel);

}
