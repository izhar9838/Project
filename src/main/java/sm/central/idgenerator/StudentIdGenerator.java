package sm.central.idgenerator;

import java.io.Serializable;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class StudentIdGenerator implements IdentifierGenerator {

    private static final long serialVersionUID = 1L;
    private static final String PREFIX = "STUD";
    private static final int MAX_DIGITS = 4;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
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
        // Pad the number with leading zeros to match MAX_DIGITS
        String numberStr = String.format("%0" + MAX_DIGITS + "d", number);

        return PREFIX + numberStr;
    }
}