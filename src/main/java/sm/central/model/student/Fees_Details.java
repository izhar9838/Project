package sm.central.model.student;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.CurrentTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@ToString(exclude = "student")
public class Fees_Details implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "paymentIdGenerator",strategy = "sm.central.idgenerator.PaymentIdGenerator")
	@GeneratedValue(generator = "paymentIdGenerator")
	private String paymentId;
	private Long amount;
	private String [] fee_type;
	private String payment_mode;
	@CreationTimestamp
	private LocalDateTime feeSubmitTime;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	@JsonIgnore
	private Student student;

}
