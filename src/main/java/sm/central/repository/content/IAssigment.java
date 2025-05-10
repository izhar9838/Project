package sm.central.repository.content;

import org.springframework.data.jpa.repository.JpaRepository;
import sm.central.model.content.Assignment;

import java.util.List;

public interface IAssigment extends JpaRepository<Assignment,Long> {
    public List<Assignment> findByClassNo(String classNo);
}
