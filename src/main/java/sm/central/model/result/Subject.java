package sm.central.model.result;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Subject {
	@Id
	private Long id;
	@Column(unique = true)
	private String subject;
}
