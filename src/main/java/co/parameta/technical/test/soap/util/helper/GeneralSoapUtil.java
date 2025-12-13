package co.parameta.technical.test.soap.util.helper;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class GeneralSoapUtil {

    private GeneralSoapUtil() {
    }
    public static Date toDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

    public static Map<String, Integer> diff(Date startDate, Date endDate) {

        Map<String, Integer> result = new HashMap<>();
        result.put("years", 0);
        result.put("months", 0);
        result.put("days", 0);

        if (startDate == null || endDate == null) {
            return result;
        }

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTime(startDate);
        end.setTime(endDate);

        if (end.before(start)) {
            Calendar temp = start;
            start = end;
            end = temp;
        }

        int years = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);
        int months = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        int days = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (days < 0) {
            end.add(Calendar.MONTH, -1);
            days += end.getActualMaximum(Calendar.DAY_OF_MONTH);
            months--;
        }

        if (months < 0) {
            months += 12;
            years--;
        }

        result.put("years", years);
        result.put("months", months);
        result.put("days", days);

        return result;
    }

}
