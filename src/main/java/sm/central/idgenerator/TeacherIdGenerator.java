package sm.central.idgenerator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class TeacherIdGenerator  implements IdentifierGenerator{

	private static final long serialVersionUID = 1L;
	private static final String PREFIX="STAFF";
	private static final int MAX_DIGITS=4;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		 String query = "SELECT t.teacherId FROM Teacher t ORDER BY t.teacherId DESC";
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

	        // Build the new ID with dynamic padding
	        return PREFIX + "0".repeat(paddingLength) + numberStr;
	}

}
