package sm.central.model.content;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String classLevel;
	private String description;
	private String mimeType;
//	@Lob for mysql
@Column(columnDefinition = "BYTEA")//for postgresql
private byte[] notes;
}
