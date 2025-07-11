package sm.central.model.result;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String subject;
}
