package sm.central.idgenerator;

import java.time.LocalDate;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class StudentIdGenerator implements IdentifierGenerator {

	private static final long serialVersionUID = 1L;
	private static final String PREFIX="STUD";

	private static final int MAX_DIGITS=4;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		// Query the last used ID
        String query = "SELECT s.studentId FROM Student s ORDER BY s.studentId DESC";
        @SuppressWarnings("deprecation")
		String lastId = (String) session.createQuery(query)
                .setMaxResults(1)
                .uniqueResult();

        // If no previous ID exists, start with 0001
        if (lastId == null) {
            return PREFIX + "0001";
        }

        // Extract the numeric part and increment
        int number = Integer.parseInt(lastId.replace(PREFIX, "")) + 1;
        String numberStr = String.valueOf(number);
        int paddingLength = MAX_DIGITS - numberStr.length();
        int Year=LocalDate.now().getYear();
        // Build the new ID with dynamic padding
        return PREFIX + Year+"0".repeat(paddingLength) + numberStr;
		
	}

}
