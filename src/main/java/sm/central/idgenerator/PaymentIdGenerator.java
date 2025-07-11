package sm.central.idgenerator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

public class PaymentIdGenerator implements IdentifierGenerator {

    private static final long serialVersionUID = 1L;
    private static final String PREFIX="PAY";

    private static final int MAX_DIGITS=4;

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        // Query the last used ID
       String id= UUID.randomUUID().toString().replaceAll("-","").toUpperCase();

        return PREFIX+id.substring(0,8) ;

    }

}
