package co.parameta.technical.test.soap.util.helper;

import co.parameta.technical.test.commons.pojo.AdditionalEmployeeInformationPojo;
import co.parameta.technical.test.commons.pojo.ExtraInformationPojo;
import com.parameta.technical.test.soap.gen.ExtraInformation;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static co.parameta.technical.test.soap.util.constant.Constant.*;

public final class GeneralSoapUtil {

    private GeneralSoapUtil() {
    }
    public static Date toDate(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        if (date == null) {
            return null;
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        try {
            return DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException("Error converting Date to XMLGregorianCalendar", e);
        }
    }

    public static boolean isNullOrBlank(Object value) {
        if (value == null) {
            return true;
        }

        if (value instanceof String str) {
            return str.trim().isEmpty();
        }

        if (value instanceof Collection<?> col) {
            return col.isEmpty();
        }

        if (value instanceof Map<?, ?> map) {
            return map.isEmpty();
        }

        if(value instanceof Integer val){
            return val <= 0;
        }

        return false;
    }




    public static AdditionalEmployeeInformationPojo buildAdditionalInformation(
            Map<String, Integer> timeAtCompany,
            Map<String, Integer> ageEmployee
    ) {
        AdditionalEmployeeInformationPojo info = new AdditionalEmployeeInformationPojo();
        info.setTimeLinkedToCompany(toExtraInformationPojo(timeAtCompany));
        info.setCurrentAgeEmployee(toExtraInformationPojo(ageEmployee));
        return info;
    }

    public static ExtraInformationPojo toExtraInformationPojo(Map<String, Integer> data) {
        ExtraInformationPojo extra = new ExtraInformationPojo();
        extra.setYears(data.get(YEARS));
        extra.setMonths(data.get(MONTHS));
        extra.setDays(data.get(DAYS));
        return extra;
    }


    public static ExtraInformation toExtraInformation(Map<String, Integer> data) {
        ExtraInformation extra = new ExtraInformation();
        extra.setYears(data.get(YEARS));
        extra.setMonths(data.get(MONTHS));
        extra.setDays(data.get(DAYS));
        return extra;
    }
}
