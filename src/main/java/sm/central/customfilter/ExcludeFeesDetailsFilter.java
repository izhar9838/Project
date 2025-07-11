package sm.central.customfilter;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class ExcludeFeesDetailsFilter {
    public static FilterProvider excludeFeesDetails() {
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAllExcept("fees_details","results");
        return new SimpleFilterProvider().addFilter("studentFilter", filter);
    }

}
